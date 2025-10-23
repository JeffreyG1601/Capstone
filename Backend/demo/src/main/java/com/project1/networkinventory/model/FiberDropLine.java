package com.project1.networkinventory.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import com.project1.networkinventory.enums.FiberDropStatus;

@Data
@Entity
@Table(name = "fiber_drop_line")
public class FiberDropLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "line_id")
    private Long lineId; // CHANGED to Long ✅

    @ManyToOne
    @JoinColumn(name = "from_splitter_id")
    private Splitter fromSplitter;

    @ManyToOne
    @JoinColumn(name = "to_customer_id")
    private Customer toCustomer;

    @Column(name = "length_meters", precision = 6, scale = 2)
    private BigDecimal lengthMeters;

    @Enumerated(EnumType.STRING)
    private FiberDropStatus status;

    // Getters and Setters
    public Long getLineId() { return lineId; }
    public void setLineId(Long lineId) { this.lineId = lineId; }

    public Splitter getFromSplitter() { return fromSplitter; }
    public void setFromSplitter(Splitter fromSplitter) { this.fromSplitter = fromSplitter; }

    public Customer getToCustomer() { return toCustomer; }
    public void setToCustomer(Customer toCustomer) { this.toCustomer = toCustomer; }

    public BigDecimal getLengthMeters() { return lengthMeters; }
    public void setLengthMeters(BigDecimal lengthMeters) { this.lengthMeters = lengthMeters; }

    public FiberDropStatus getStatus() { return status; }
    public void setStatus(FiberDropStatus status) { this.status = status; }
}
