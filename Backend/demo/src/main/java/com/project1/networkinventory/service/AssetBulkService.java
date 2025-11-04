package com.project1.networkinventory.service;

import com.project1.networkinventory.dto.AssetBulkRequest;
import com.project1.networkinventory.dto.BulkUploadResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AssetBulkService {
    BulkUploadResult uploadBulk(List<AssetBulkRequest> rows);
    BulkUploadResult uploadFromCsv(MultipartFile file);
}
