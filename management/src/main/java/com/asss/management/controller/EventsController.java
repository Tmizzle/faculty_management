package com.asss.management.controller;

import com.asss.management.entity.Events;
import com.asss.management.service.dto.EventsDTO;
import com.asss.management.service.implementation.EventsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.util.Date;
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

    @PostMapping
    public ResponseEntity addNewEvent(@RequestBody Events event){
        eventsService.addNewEvent(event);

        return ResponseEntity.ok("Added a new event successfully");
    }

    @PutMapping(path = "{id}")
    public ResponseEntity updateEvent(
            @PathVariable("id") Integer id,
            @Parameter(description = "Event name") @RequestParam(required = false) String name,
            @Parameter(description = "Start date of the event") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @Parameter(description = "End date of the event") @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate
    ) {
        eventsService.updateEvent(id, name, startDate, endDate);

        return ResponseEntity.ok("Event updated successfully");
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity deleteProjectMember(@Parameter(description = "ID of the event", example = "1") @PathVariable("id") Integer eventID ){
        eventsService.deleteEvent(eventID);

        return ResponseEntity.ok("Event successfully removed from that project");
    }
}
