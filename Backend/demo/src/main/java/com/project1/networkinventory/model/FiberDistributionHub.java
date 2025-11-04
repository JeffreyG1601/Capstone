package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "fdh")
public class FiberDistributionHub {

    @Id
    @Column(name = "fdh_id")
    private Long id;

    private String name;
    private String location;
    private String region;

    @Column(name = "max_ports")
    private Integer maxPorts;

    @OneToMany(mappedBy = "fiberDistributionHub", cascade = CascadeType.ALL)
    private List<Splitter> splitters;

    @ManyToOne
    @JoinColumn(name = "headend_id")
    private Headend headend;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public Integer getMaxPorts() { return maxPorts; }
    public void setMaxPorts(Integer maxPorts) { this.maxPorts = maxPorts; }

    public List<Splitter> getSplitters() { return splitters; }
    public void setSplitters(List<Splitter> splitters) { this.splitters = splitters; }

    public Headend getHeadend() { return headend; }
    public void setHeadend(Headend headend) { this.headend = headend; }
}
