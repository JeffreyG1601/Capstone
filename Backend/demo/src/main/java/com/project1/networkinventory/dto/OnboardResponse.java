package com.project1.networkinventory.dto;

public class OnboardResponse {
    public Long customerId;
    public Long deploymentTaskId;
    public String message;

    public OnboardResponse(Long cid, Long taskId, String msg) {
        this.customerId = cid;
        this.deploymentTaskId = taskId;
        this.message = msg;
    }
}
