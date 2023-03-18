package com.asss.management.dao;

import com.asss.management.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventsRepo extends JpaRepository<Events, Integer> {
}
