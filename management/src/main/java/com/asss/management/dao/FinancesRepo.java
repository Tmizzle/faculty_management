package com.asss.management.dao;

import com.asss.management.entity.Finances;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinancesRepo extends JpaRepository<Finances, Integer> {
}
