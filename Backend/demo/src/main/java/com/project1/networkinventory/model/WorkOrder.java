package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.project1.networkinventory.enums.WorkOrderStatus;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String workType;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private User assignedTo;

    @ManyToOne
    @JoinColumn(name = "zone_id")
    private Zone zone;

    @Enumerated(EnumType.STRING)
    private WorkOrderStatus status;

    private LocalDateTime scheduledDate;
    private LocalDateTime completedDate;
    private String notes;

    // Getters and Setters (optional if using Lombok)
}
