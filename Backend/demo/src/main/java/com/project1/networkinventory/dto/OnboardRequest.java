package com.project1.networkinventory.dto;

import java.time.LocalDate;

public class OnboardRequest {
    // Customer
    public String name;
    public String address;
    public String neighborhood;
    public String plan;
    public String connectionType; // "Wired" or "Wireless"
    // Network selection
    public Long fdhId;       // optional if auto-mapped, recommended
    public Long splitterId;
    public Integer assignedPort;
    // Assets (IDs of assets to assign)
    public Long ontAssetId;
    public Long routerAssetId;
    // Task assignment
    public Long technicianId; // optional — planner can pick a tech
    public LocalDate scheduledDate; // optional
}
