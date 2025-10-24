package com.project1.networkinventory.repository;

import com.project1.networkinventory.model.Splitter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SplitterRepository extends JpaRepository<Splitter, Long> {
    List<Splitter> findByFiberDistributionHub_Id(Long fdhId);
}
