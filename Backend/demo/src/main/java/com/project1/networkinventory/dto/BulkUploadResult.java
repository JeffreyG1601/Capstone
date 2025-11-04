package com.project1.networkinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkUploadResult {
    private int insertedCount;
    private List<RowError> errors;
	public int getInsertedCount() {
		return insertedCount;
	}
	public void setInsertedCount(int insertedCount) {
		this.insertedCount = insertedCount;
	}
	public List<RowError> getErrors() {
		return errors;
	}
	public void setErrors(List<RowError> errors) {
		this.errors = errors;
	}
    
}
