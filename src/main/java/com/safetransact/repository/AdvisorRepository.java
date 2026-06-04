package com.safetransact.repository;

import com.safetransact.model.Advisor;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface AdvisorRepository extends JpaRepository<Advisor, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Advisor a WHERE a.id = :id")
    Optional<Advisor> findByIdWithPessimisticLock(@Param("id") Long id);
}