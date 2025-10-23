package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "technician")
public class Technician {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long technicianId;

    private String name;
    private String contact;
    private String region;

    // Getters and Setters
    public Long getTechnicianId() { return technicianId; }
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}
