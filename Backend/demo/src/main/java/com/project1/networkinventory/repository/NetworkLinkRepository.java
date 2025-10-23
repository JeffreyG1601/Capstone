package com.project1.networkinventory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project1.networkinventory.model.NetworkLink;
import java.util.List;

public interface NetworkLinkRepository extends JpaRepository<NetworkLink, Long> {
    List<NetworkLink> findBySourceDeviceId(Long deviceId);
    List<NetworkLink> findByTargetDeviceId(Long deviceId);
}
