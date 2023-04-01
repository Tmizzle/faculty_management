package com.asss.management.dao;

import com.asss.management.entity.Enums.Type_of_event;
import com.asss.management.entity.Events;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface EventsRepo extends JpaRepository<Events, Integer> {


    @Query(value = "SELECT * FROM events " +
            "WHERE events.start_date <= ?1 AND events.end_date >= ?2 ; ",
            nativeQuery = true)
    List<Events> eventOverlapCheck(Date endDate, Date startDate);

    @Query(value = "SELECT * FROM events " +
            "WHERE ?1 BETWEEN events.start_date AND events.end_date ; ",
            nativeQuery = true)
    Events checkIfEventOngoing(Date curentDate);

    @Query(value = "SELECT * FROM events " +
            "WHERE ?1 BETWEEN events.start_date AND events.end_date " +
            "AND events.type = 'FIRST_SEMESTER' OR events.type = 'SECOND_SEMESTER' ; ",
            nativeQuery = true)
    List<Events> checkForSemesterOverlap(Date curentDate);

    @Query(value = "SELECT * FROM events " +
            "WHERE events.start_date = ?1 " +
            "AND events.type = 'EXAM_REGISTRATION_LATE' ;",
            nativeQuery = true)
    Events lateExamPeriodUpdate(Date startDate);

    @Query(value = "SELECT * FROM events " +
            "WHERE events.start_date <= ?1 AND events.end_date >= ?1 " +
            "AND events.id = ?2 ;",
            nativeQuery = true)
    Events ongoingEvent(Date currentDate, Integer eventID);

}
