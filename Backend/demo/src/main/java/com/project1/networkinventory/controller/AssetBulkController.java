package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.AssetBulkRequest;
import com.project1.networkinventory.dto.BulkUploadResult;
import com.project1.networkinventory.service.AssetBulkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/assets/bulk")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AssetBulkController {

    private final AssetBulkService assetBulkService;

    public AssetBulkController(AssetBulkService assetBulkService) {
		super();
		this.assetBulkService = assetBulkService;
	}

	// POST /api/assets/bulk (JSON array payload)
    @PostMapping
    public ResponseEntity<BulkUploadResult> uploadJson(@RequestBody List<AssetBulkRequest> rows) {
        BulkUploadResult result = assetBulkService.uploadBulk(rows);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    // POST /api/assets/bulk/upload (multipart CSV)
    @PostMapping("/upload")
    public ResponseEntity<BulkUploadResult> uploadCsv(@RequestParam("file") MultipartFile file) {
        BulkUploadResult result = assetBulkService.uploadFromCsv(file);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
