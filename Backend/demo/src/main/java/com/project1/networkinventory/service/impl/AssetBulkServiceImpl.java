package com.project1.networkinventory.service.impl;

import com.project1.networkinventory.dto.AssetBulkRequest;
import com.project1.networkinventory.dto.BulkUploadResult;
import com.project1.networkinventory.dto.RowError;
import com.project1.networkinventory.mapper.AssetBulkMapper;
import com.project1.networkinventory.model.Asset;
import com.project1.networkinventory.repository.AssetRepository;
import com.project1.networkinventory.service.AssetBulkService;
import com.project1.networkinventory.enums.AssetStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AssetBulkServiceImpl implements AssetBulkService {

    private final AssetRepository assetRepository;

    public AssetBulkServiceImpl(AssetRepository assetRepository) {
		super();
		this.assetRepository = assetRepository;
	}

	/**
     * Partial success behavior:
     * - Validate each row
     * - Skip rows with errors, collect RowError
     * - Deduplicate serials within incoming payload; mark duplicates as errors
     * - For rows with unique serial and valid fields, create Asset and save (batch saveAll)
     */
    @Override
    @Transactional
    public BulkUploadResult uploadBulk(List<AssetBulkRequest> rows) {
        BulkUploadResult result = new BulkUploadResult();
        if (rows == null || rows.isEmpty()) {
            result.setInsertedCount(0);
            return result;
        }

        List<RowError> errors = new ArrayList<>();
        List<Asset> toSave = new ArrayList<>();

        // track serials in payload to detect duplicates within upload
        Map<String, Integer> payloadSerialIndex = new HashMap<>();
        for (int i = 0; i < rows.size(); i++) {
            AssetBulkRequest r = rows.get(i);
            String serial = (r.getSerialNumber() == null ? "" : r.getSerialNumber().trim());
            if (!serial.isEmpty()) {
                String k = serial.toLowerCase();
                if (payloadSerialIndex.containsKey(k)) {
                    // mark both occurrences as duplicate in payload (first will be overwritten below, mark new)
                    errors.add(new RowError(i, "Duplicate serial in uploaded file: " + serial, Map.of("row", r)));
                    continue;
                } else {
                    payloadSerialIndex.put(k, i);
                }
            }
        }

        for (int i = 0; i < rows.size(); i++) {
            AssetBulkRequest r = rows.get(i);
            // basic validation
            if (r.getModel() == null || r.getModel().trim().isEmpty()) {
                errors.add(new RowError(i, "Missing required field: model", Map.of("row", r)));
                continue;
            }
            if (r.getSerialNumber() == null || r.getSerialNumber().trim().isEmpty()) {
                errors.add(new RowError(i, "Missing required field: serialNumber", Map.of("row", r)));
                continue;
            }
            String serial = r.getSerialNumber().trim();
            // check DB uniqueness
            if (assetRepository.findBySerialNumber(serial).isPresent()) {
                errors.add(new RowError(i, "Serial number already exists: " + serial, Map.of("row", r)));
                continue;
            }

            // parse status tolerant
            AssetStatus status = AssetStatus.fromString(r.getStatus());
            if (r.getStatus() != null && status == null) {
                errors.add(new RowError(i, "Invalid status value: " + r.getStatus(), Map.of("row", r)));
                continue;
            }

            // map to entity
            Asset a = AssetBulkMapper.toEntity(r);
            // ensure status default if null
            if (a.getStatus() == null) a.setStatus(AssetStatus.AVAILABLE);
            toSave.add(a);
        }

        // save batch; if any DB-level constraint triggers, capture and report
        try {
            if (!toSave.isEmpty()) {
                List<Asset> saved = assetRepository.saveAll(toSave);
                result.setInsertedCount(saved.size());
            } else {
                result.setInsertedCount(0);
            }
        } catch (DataIntegrityViolationException ex) {
            // best-effort: try to save individually to isolate failing rows
            int inserted = 0;
            for (int i = 0; i < toSave.size(); i++) {
                Asset a = toSave.get(i);
                try {
                    assetRepository.save(a);
                    inserted++;
                } catch (Exception e) {
                    errors.add(new RowError(-1, "DB save failed for serial " + a.getSerialNumber() + ": " + e.getMessage(), Map.of("asset", a)));
                }
            }
            result.setInsertedCount(inserted);
        }

        result.setErrors(errors);
        return result;
    }

    /**
     * Parse CSV file and delegate to uploadBulk.
     * Expected headers (case-insensitive): assetType,model,serialNumber,status,location
     */
    @Override
    @Transactional
    public BulkUploadResult uploadFromCsv(MultipartFile file) {
        List<AssetBulkRequest> parsed = new ArrayList<>();
        if (file == null || file.isEmpty()) {
            BulkUploadResult r = new BulkUploadResult();
            r.setInsertedCount(0);
            r.setErrors(List.of(new RowError(-1, "Uploaded file is empty", Map.of())));
            return r;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String headerLine = null;
            // read until a non-empty header (skip blank lines)
            while ((headerLine = br.readLine()) != null) {
                headerLine = headerLine.trim();
                if (!headerLine.isEmpty()) break;
            }
            if (headerLine == null) {
                BulkUploadResult r = new BulkUploadResult();
                r.setInsertedCount(0);
                r.setErrors(List.of(new RowError(-1, "CSV contains no header", Map.of())));
                return r;
            }
            String[] headers = Arrays.stream(headerLine.split(",")).map(String::trim).map(String::toLowerCase).toArray(String[]::new);

            String line;
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                rowIndex++;
                if (line == null) break;
                String[] cols = Arrays.stream(line.split(",")).map(String::trim).toArray(String[]::new);
                if (cols.length == 0) continue;
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < headers.length; i++) {
                    String h = headers[i];
                    String val = i < cols.length ? cols[i] : "";
                    map.put(h, val);
                }
                AssetBulkRequest req = new AssetBulkRequest();
                req.setAssetType(Optional.ofNullable(map.get("assettype")).orElse(map.getOrDefault("type", "")));
                req.setModel(map.getOrDefault("model", ""));
                req.setSerialNumber(map.getOrDefault("serialnumber", map.getOrDefault("serial", "")));
                req.setStatus(map.getOrDefault("status", ""));
                req.setLocation(map.getOrDefault("location", ""));
                parsed.add(req);
            }
        } catch (Exception ex) {
            BulkUploadResult r = new BulkUploadResult();
            r.setInsertedCount(0);
            r.setErrors(List.of(new RowError(-1, "Failed to parse CSV: " + ex.getMessage(), Map.of())));
            return r;
        }

        return uploadBulk(parsed);
    }
}
