package com.asss.management.controller;

import com.asss.management.service.dto.EventsDTO;
import com.asss.management.service.dto.StudentDTO;
import com.asss.management.service.implementation.EventsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping(path = "/api/events")
@Data
@Tag(name = "Events API", description = "API for managing events")
@CrossOrigin(origins = "*")
public class EventsController {

    private final EventsService eventsService;

    @GetMapping
    public List<EventsDTO> getEvents(){
        return eventsService.getEvents();
    }

    @GetMapping(path = "{id}")
    public EventsDTO getEventByID(@PathVariable("id") Integer id){
        return eventsService.getEventByID(id);
    }
}
