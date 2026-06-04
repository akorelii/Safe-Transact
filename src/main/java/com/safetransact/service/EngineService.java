package com.safetransact.service;

import com.safetransact.model.*;
import com.safetransact.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class EngineService {

    private final AdvisorRepository advisorRepository;
    private final WalletRepository walletRepository;

    public EngineService(AdvisorRepository advisorRepository, WalletRepository walletRepository) {
        this.advisorRepository = advisorRepository;
        this.walletRepository = walletRepository;
    }

    
    @Transactional
    public String assignAdvisorSecurely(Long advisorId, Long studentId) {
               Advisor advisor = advisorRepository.findByIdWithPessimisticLock(advisorId)
                .orElseThrow(() -> new RuntimeException("Danisman bulunamadi!"));

        if (advisor.getCurrentUsed() >= advisor.getQuotaLimit()) {
            return "KONTENJAN DOLU! Islem reddedildi (Race Condition engellendi).";
        }


        advisor.setCurrentUsed(advisor.getCurrentUsed() + 1);
        advisorRepository.save(advisor);


        return "SUCCESS: Danisman basariyla atandi. (Veritabanı Sırası: " + advisor.getCurrentUsed() + "/5)";
    }


    @Transactional
    public String processPaymentSecurely(Long walletId, BigDecimal amount) {
        Wallet wallet = walletRepository.findByIdWithPessimisticLock(walletId)
                .orElseThrow(() -> new RuntimeException("Cuzdan bulunamadi!"));

        if (wallet.getBalance().compareTo(amount) < 0) {
            return "YETERSIZ BAKIYE! Islem iptal edildi.";
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        return "SUCCESS: Odeme tık diye gecildi. Yeni bakiye: " + wallet.getBalance();
    }
}
