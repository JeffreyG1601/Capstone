package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "headend")
public class Headend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "headend_id")
    private Long id;

    private String name;
    private String location;
    private String region;

    @OneToMany(mappedBy = "headend", cascade = CascadeType.ALL)
    private List<FiberDistributionHub> fdhList;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public List<FiberDistributionHub> getFdhList() { return fdhList; }
    public void setFdhList(List<FiberDistributionHub> fdhList) { this.fdhList = fdhList; }
}
