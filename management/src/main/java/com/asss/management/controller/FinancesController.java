package com.asss.management.controller;

import com.asss.management.service.dto.EventsDTO;
import com.asss.management.service.dto.FinancesDTO;
import com.asss.management.service.implementation.FinancesService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/finances")
@Data
@Tag(name = "Finances API", description = "API for managing finances")
@CrossOrigin(origins = "*")
public class FinancesController {

    private final FinancesService financesService;

    @GetMapping
    public List<FinancesDTO> getFinances(){
        return financesService.getFinances();
    }

    @GetMapping(path = "{id}")
    public FinancesDTO getFinancesById(@PathVariable("id") Integer id){
        return financesService.getFinancesById(id);
    }

    @GetMapping(path = "/forStudent/{token}")
    public List<FinancesDTO> getFinancesForStudent(@PathVariable("token") String token){
        return financesService.getFinancesForStudent(token);
    }
}
