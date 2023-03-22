package com.asss.management.controller;

import com.asss.management.dao.ExamsRepo;
import com.asss.management.entity.Exams;
import com.asss.management.service.implementation.ExportsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/api/exports")
@Data
@Tag(name = "Exports API", description = "API for managing exports")
@CrossOrigin(origins = "*")
public class ExportsController {

    private final ExportsService exportsService;
    private final ExamsRepo examsRepo;

    @GetMapping("/download-exam-report-per-event")
    public ResponseEntity downloadClientReport(HttpServletResponse response,
                                               @Parameter(description = "Session token", required = true)
                                               @RequestParam String token,
                                               @Parameter(description = "event ID", example = "10", required = true)
                                               @RequestParam Integer eventID) throws IOException {
        List<Exams> dataList = examsRepo.findExamsForEventByProfesor(token, eventID);
        String fileName = "Exam Report.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        exportsService.exportClientHours(dataList, fileName, token, eventID);

        return ResponseEntity.ok("Successfully generated your file");
    }
}
