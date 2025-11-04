package com.project1.networkinventory.service;

import com.project1.networkinventory.model.Splitter;

import java.util.List;

public interface SplitterService {
    Splitter createSplitter(Splitter splitter);
    List<Splitter> getAllSplitters();
    List<Splitter> getSplittersByFDHId(Long fdhId);
    Splitter getSplitterById(Long id);
    Splitter updateSplitter(Long id, Splitter splitter);
    void deleteSplitter(Long id);
}
