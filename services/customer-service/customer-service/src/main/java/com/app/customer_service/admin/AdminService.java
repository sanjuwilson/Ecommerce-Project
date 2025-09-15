package com.app.customer_service.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final AdminRepo adminRepo;

    public Integer save(Administrator administrator) {
        return adminRepo.save(administrator).getId();
    }
}
