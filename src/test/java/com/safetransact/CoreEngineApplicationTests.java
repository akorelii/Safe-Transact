package com.safetransact;

import com.safetransact.model.Advisor;
import com.safetransact.repository.AdvisorRepository;
import com.safetransact.service.EngineService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class CoreEngineApplicationTests {

	@Autowired
	private EngineService engineService;

	@Autowired
	private AdvisorRepository advisorRepository;

	@Test
	void testConcurrentAssignment() throws InterruptedException {
		Advisor advisor = advisorRepository.findById(1L).orElseThrow();
		advisor.setCurrentUsed(0);
		advisorRepository.save(advisor);

		int threadCount = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(1);

		for (int i = 0; i < threadCount; i++) {
			final long studentId = 100 + i;
			executorService.submit(() -> {
				try {
					latch.await();
					engineService.assignAdvisorSecurely(1L, studentId);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}

		System.out.println(">>> TÜM THREAD'LER HAZIR. AYNI ANDA TETİK ÇEKİLİYOR! <<<");
		latch.countDown();

		Thread.sleep(3000);

		Advisor finalAdvisor = advisorRepository.findById(1L).orElseThrow();
		System.out.println("===============================================");
		System.out.println("KONTENJAN SINIRI: " + finalAdvisor.getQuotaLimit());
		System.out.println("YAZILAN CURRENT_USED DEĞERİ: " + finalAdvisor.getCurrentUsed());
		System.out.println("===============================================");
	}
}