package com.safetransact.controller;

import com.safetransact.service.EngineService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/engine")
public class EngineController {

    private final EngineService engineService;

    public EngineController(EngineService engineService) {
        this.engineService = engineService;
    }

    // Akademik Modül Test Linki: http://localhost:8080/api/engine/assign?advisorId=1&studentId=99
    @GetMapping("/assign")
    public String testAdvisorAssignment(@RequestParam Long advisorId, @RequestParam Long studentId) {
        return engineService.assignAdvisorSecurely(advisorId, studentId);
    }

    // Fintech Modül Test Linki: http://localhost:8080/api/engine/pay?walletId=1&amount=50.00
    @GetMapping("/pay")
    public String testPayment(@RequestParam Long walletId, @RequestParam java.math.BigDecimal amount) {
        return engineService.processPaymentSecurely(walletId, amount);
    }
}