package com.project1.networkinventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Error information for a particular row during bulk upload.
 * rowIndex: zero-based row index in the incoming payload or CSV (or -1 for unknown)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RowError {
    private int rowIndex;
    private String message;
    private Map<String, Object> rowData;
    
	public RowError() {
		super();
	}
	public RowError(int rowIndex, String message, Map<String, Object> rowData) {
		super();
		this.rowIndex = rowIndex;
		this.message = message;
		this.rowData = rowData;
	}
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getRowData() {
		return rowData;
	}
	public void setRowData(Map<String, Object> rowData) {
		this.rowData = rowData;
	}
    
}
