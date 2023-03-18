package com.asss.management.service.implementation;

import com.asss.management.dao.EventsRepo;
import com.asss.management.entity.Events;
import com.asss.management.entity.Student;
import com.asss.management.service.dto.EventsDTO;
import com.asss.management.service.dto.StudentDTO;
import com.asss.management.service.mapper.EventsMapper;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
}
