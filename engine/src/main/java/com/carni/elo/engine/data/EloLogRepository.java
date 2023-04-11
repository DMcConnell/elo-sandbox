package com.carni.elo.engine.data;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EloLogRepository extends JpaRepository<EloLog, Long> {
}