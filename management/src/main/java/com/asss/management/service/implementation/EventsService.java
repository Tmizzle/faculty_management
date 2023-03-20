package com.asss.management.service.implementation;

import com.asss.management.dao.EventsRepo;
import com.asss.management.entity.Employee;
import com.asss.management.entity.Enums.Type_of_event;
import com.asss.management.entity.Events;
import com.asss.management.entity.Student;
import com.asss.management.service.dto.EventsDTO;
import com.asss.management.service.dto.StudentDTO;
import com.asss.management.service.mapper.EventsMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Data
public class EventsService {

    private final EventsRepo eventsRepo;
    private final EventsMapper eventsMapper;

    // Retrieves all students
    public List<EventsDTO> getEvents() {
        List<Events> eventsList = eventsRepo.findAll();
        List<EventsDTO> eventsDTOList = eventsMapper.entitiesToDTOs(eventsList);
        return eventsDTOList;
    }

    // Retrieves a event by ID
    public EventsDTO getEventByID(Integer id) {
        Events events = eventsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "event with id " + id + " does not exist"));
        EventsDTO eventsDTO = eventsMapper.entityToDTO(events);
        return eventsDTO;
    }

    public void addNewEvent(Events event){
        List<Events> eventsListCheck = eventsRepo.eventOverlapCheck(event.getEndDate(), event.getStartDate());
        //List<Events> eventsListCheckByType = eventsRepo.eventOverlapCheckForEnrollment(event.getEndDate(), event.getStartDate(), event.getType().toString());

        for (Events e : eventsListCheck) {
            if(e.getType() == event.getType()){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Event of such type for that period already exists");
            }
            if(event.getType() == Type_of_event.EXAM_PERIOD || event.getType() == Type_of_event.EXTRA_EXAM_PERIOD){
                if(e.getType() == Type_of_event.EXAM_PERIOD || e.getType() == Type_of_event.EXTRA_EXAM_PERIOD){
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Event of such type for that period already exists 2");
                }
            }
        }

        Date startDate = event.getStartDate();
        Date endDate = event.getEndDate();

        LocalDate startLocalDate = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate endLocalDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        long daysBetween = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);
        int days = (int) daysBetween + 1;
        if(event.getType() == Type_of_event.EXTRA_EXAM_PERIOD && days > 7){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Extra exam periods can't be longer then 7 days");
        }
        eventsRepo.save(event);

        if(event.getType() == Type_of_event.EXAM_REGISTRATION){
            Events extraEvent = new Events();
            extraEvent.setName(event.getName());
            extraEvent.setType(Type_of_event.EXAM_REGISTRATION_LATE);
            LocalDate localStartDate = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate extraStartDate = localStartDate.plusDays(1);
            LocalDate extraEndDate = localStartDate.plusDays(2);
            Date extraStartDateAsDate = Date.from(extraStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date extraEndDateAsDate = Date.from(extraEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            extraEvent.setStartDate(extraStartDateAsDate);
            extraEvent.setEndDate(extraEndDateAsDate);

            eventsRepo.save(extraEvent);
        }
    }
    @Transactional
    public void updateEvent(Integer id,
                              String name,
                              Date startDate,
                              Date endDate) {
        Events event = eventsRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Specified event has not been found."));
        if (name != null && !Objects.equals(event.getName(), name)) {
            event.setName(name);
        }
        if (startDate != null && endDate != null && event.getType() == Type_of_event.EXAM_REGISTRATION) {
            int numberOfEvents = 0;
            List<Events> eventsListCheck = eventsRepo.eventOverlapCheck(startDate, endDate);
            for (Events e : eventsListCheck) {
                if (e.getType() == event.getType()) {
                    numberOfEvents++;
                    }
                }
                if (numberOfEvents > 1) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "Event of such type for that period already exists");
                }
                LocalDate localStartDate = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate extraStartDate = localStartDate.plusDays(1);
                LocalDate extraEndDate = localStartDate.plusDays(2);
                Date extraStartDateAsDate = Date.from(extraStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                Date extraEndDateAsDate = Date.from(extraEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

                LocalDate findLateEvent = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate findLateEventPlusOne = findLateEvent.plusDays(1);
                Date findLateEventFinal = Date.from(findLateEventPlusOne.atStartOfDay(ZoneId.systemDefault()).toInstant());

                Events LateExamPeriodUpdate = eventsRepo.lateExamPeriodUpdate(findLateEventFinal);

                LateExamPeriodUpdate.setStartDate(extraStartDateAsDate);
                LateExamPeriodUpdate.setEndDate(extraEndDateAsDate);

                event.setStartDate(startDate);
                event.setEndDate(endDate);

            }
        }
}
