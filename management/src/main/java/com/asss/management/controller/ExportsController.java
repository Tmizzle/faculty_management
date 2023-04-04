package com.asss.management.controller;

import com.asss.management.dao.ExamStatusInfoRepo;
import com.asss.management.dao.ExamsRepo;
import com.asss.management.entity.ExamStatusInfo;
import com.asss.management.entity.Exams;
import com.asss.management.securityConfig.JwtService;
import com.asss.management.service.implementation.ExportsService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@RestController
@RequestMapping(path = "/api/exports")
@Data
@Tag(name = "Exports API", description = "API for managing exports")
@CrossOrigin(origins = "*")
public class ExportsController {

    private final ExportsService exportsService;
    private final ExamsRepo examsRepo;
    private final ExamStatusInfoRepo examStatusInfoRepo;
    private final JwtService jwtService;

    @GetMapping("/download-exam-report-per-event")
    public void downloadClientReport(HttpServletResponse response,
                                     @Parameter(description = "Session token", required = true)
                                     @RequestParam String token,
                                     @Parameter(description = "event ID", example = "10", required = true)
                                     @RequestParam Integer eventID) throws IOException {
        List<Exams> dataList = examsRepo.findExamsForEventByProfesor(token, eventID);
        String fileName = "Exam Report.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        try (OutputStream out = response.getOutputStream()) {
            exportsService.exportExamsPerEventForProfessor(dataList, fileName, token, eventID);
            out.flush();
        }
    }

    @GetMapping("/download-student-exam-info")
    public ResponseEntity downloadStudentExamInfo(HttpServletResponse response,
                                               @Parameter(description = "Session token", required = true)
                                               @RequestParam String token,
                                               @RequestParam boolean all,
                                                  @RequestParam boolean passed,
                                                  @RequestParam boolean unpassed) throws IOException {
        String userEmail = jwtService.extractUsername(token);
        List<ExamStatusInfo> passedExams = examStatusInfoRepo.passedExamsByStudent(userEmail);
        List<ExamStatusInfo> unpassedExams = examStatusInfoRepo.unpassedExamsByStudent(userEmail);
        String fileName = "Student Exam Report.xlsx";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        exportsService.exportExamInfoForStudent(passedExams, unpassedExams, fileName, token, all, passed, unpassed);

        return ResponseEntity.ok("Successfully generated your file");
    }
}
