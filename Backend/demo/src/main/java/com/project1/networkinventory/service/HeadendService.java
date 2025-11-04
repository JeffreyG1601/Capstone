package com.project1.networkinventory.service;

import com.project1.networkinventory.model.Headend;

import java.util.List;

public interface HeadendService {
    Headend createHeadend(Headend headend);
    List<Headend> getAllHeadends();
    Headend getHeadendById(Long id);
    Headend updateHeadend(Long id, Headend headend);
    void deleteHeadend(Long id);
}
