package com.asss.management.service.mapper;

import com.asss.management.entity.Events;
import com.asss.management.service.dto.EventsDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EventsMapper {

    EventsDTO entityToDTO (Events events);

    Events DTOToEntity (EventsDTO eventsDTO);

    List<EventsDTO> entitiesToDTOs(List<Events> eventsList);

    List<Events> DTOsToEntities(List<EventsDTO> eventsDTOList);
}
