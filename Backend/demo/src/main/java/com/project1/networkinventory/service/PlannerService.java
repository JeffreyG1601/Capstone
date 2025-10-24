package com.project1.networkinventory.service;

import com.project1.networkinventory.dto.OnboardRequest;
import com.project1.networkinventory.dto.OnboardResponse;

public interface PlannerService {
    OnboardResponse onboardCustomer(OnboardRequest req) throws Exception;
}
