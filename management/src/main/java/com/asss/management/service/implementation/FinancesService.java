package com.asss.management.service.implementation;

import com.asss.management.dao.FinancesRepo;
import com.asss.management.entity.Events;
import com.asss.management.entity.Finances;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.dto.EventsDTO;
import com.asss.management.service.dto.FinancesDTO;
import com.asss.management.service.mapper.FinancesMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class FinancesService {

    private final JwtService jwtService;
    private final FinancesRepo financesRepo;
    private final FinancesMapper financesMapper;

    // Retrieves all finances
    public List<FinancesDTO> getFinances() {
        List<Finances> financesList = financesRepo.findAll();
        List<FinancesDTO> financesDTOList = financesMapper.entitiesToDTOs(financesList);
        return financesDTOList;
    }

    // Retrieves a event by ID
    public FinancesDTO getFinancesById(Integer id) {
        Finances finances = financesRepo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "finance entry with id " + id + " does not exist"));
        FinancesDTO financesDTO = financesMapper.entityToDTO(finances);
        return financesDTO;
    }

    public List<FinancesDTO> getFinancesForStudent(String token){
        String userEmail = jwtService.extractUsername(token);

        List<Finances> finances = financesRepo.financesForStudent(userEmail);
        List<FinancesDTO> financesDTOList = financesMapper.entitiesToDTOs(finances);
        return financesDTOList;
    }


}
