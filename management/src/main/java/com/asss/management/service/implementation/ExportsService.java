package com.asss.management.service.implementation;

import com.asss.management.dao.ExamStatusInfoRepo;
import com.asss.management.dao.ExamsRepo;
import com.asss.management.dao.StudentRepo;
import com.asss.management.entity.ExamStatusInfo;
import com.asss.management.entity.Exams;
import com.asss.management.entity.Student;
import com.asss.management.securityConfig.JwtService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@Data
@RequiredArgsConstructor
public class ExportsService {

    private final ExamsRepo examsRepo;
    private final JwtService jwtService;
    private final ExamStatusInfoRepo examStatusInfoRepo;
    private final StudentRepo studentRepo;

    public void exportClientHours(List<Exams> dataList, String fileName, String token, Integer eventID) throws IOException {
        String userEmail = jwtService.extractUsername(token);

        List<Exams> examsList = examsRepo.findExamsForEventByProfesor(userEmail, eventID);

        // create workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        CellStyle style = workbook.createCellStyle();

        // create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Professor name");
        headerRow.createCell(1).setCellValue("Student name");
        headerRow.createCell(2).setCellValue("Student email");
        headerRow.createCell(3).setCellValue("Student index");
        headerRow.createCell(4).setCellValue("Student course");
        headerRow.createCell(5).setCellValue("Event name");
        headerRow.createCell(6).setCellValue("Subject name");

        // fill data rows
        int rowNum = 1;
        int total = 0;

        for (Exams exports : examsList) {
            Row dataRow = sheet.createRow(rowNum++);
            total++;
            dataRow.createCell(0).setCellValue(exports.getProfesor().getFirstName() + " " + exports.getProfesor().getLastName());
            dataRow.createCell(1).setCellValue(exports.getStudent().getFirstName() + " " + exports.getStudent().getLastName());
            dataRow.createCell(2).setCellValue(exports.getStudent().getEmail());
            dataRow.createCell(3).setCellValue(exports.getStudent().getIndex());
            dataRow.createCell(4).setCellValue(exports.getStudent().getCourseOfStudies().toString());
            dataRow.createCell(5).setCellValue(exports.getEvent().getName());
            dataRow.createCell(6).setCellValue(exports.getSubject().getName());
        }
        Row totalRow = sheet.createRow(rowNum++);
        totalRow.createCell(0).setCellValue("TOTAL:");
        totalRow.createCell(1).setCellValue(total);

        // set column widths
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 8000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);
        sheet.setColumnWidth(5, 6000);
        sheet.setColumnWidth(6, 6000);

        // generate file and download
        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public void exportExamInfoForStudent(List<ExamStatusInfo> passedExams, List<ExamStatusInfo> unpassedExams, String fileName, String token, boolean all, boolean passed, boolean unpassed) throws IOException {
        String userEmail = jwtService.extractUsername(token);

        passedExams = examStatusInfoRepo.passedExamsByStudent(userEmail);
        unpassedExams = examStatusInfoRepo.unpassedExamsByStudent(userEmail);

        Student student = studentRepo.findByEmail(userEmail);

        // create workbook and sheet
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");
        CellStyle style = workbook.createCellStyle();

        Row topRow = sheet.createRow(0);
        topRow.createCell(0).setCellValue("Exam info for student: " + student.getFirstName() + " " + student.getLastName());
        topRow.setHeight((short) 500);

        Row emptyRow2 = sheet.createRow(1);
        emptyRow2.createCell(0).setCellValue("");

        // create header row
        Row headerRow = sheet.createRow(2);
        headerRow.createCell(0).setCellValue("Subject name");
        headerRow.createCell(1).setCellValue("ESPB");
        headerRow.createCell(2).setCellValue("Grade");
        headerRow.createCell(3).setCellValue("Status");

        // fill data rows
        int rowNum = 3;
        int espbPassed = 0;
        if(all){
            for (ExamStatusInfo examStatusInfo : passedExams) {
                Row dataRow = sheet.createRow(rowNum++);
                espbPassed += examStatusInfo.getSubject().getEspb();
                dataRow.createCell(0).setCellValue(examStatusInfo.getSubject().getName());
                dataRow.createCell(1).setCellValue(examStatusInfo.getSubject().getEspb());
                dataRow.createCell(2).setCellValue(examStatusInfo.getGrade());
                dataRow.createCell(3).setCellValue(examStatusInfo.getStatus().toString());
            }
            Row totalRow = sheet.createRow(rowNum++);
            totalRow.createCell(0).setCellValue("TOTAL ESPB:");
            totalRow.createCell(1).setCellValue(espbPassed);
            Row emptyRow = sheet.createRow(rowNum++);
            emptyRow.createCell(0).setCellValue("");
            for (ExamStatusInfo examStatusInfo : unpassedExams) {
                Row dataRow = sheet.createRow(rowNum++);
                espbPassed += examStatusInfo.getSubject().getEspb();
                dataRow.createCell(0).setCellValue(examStatusInfo.getSubject().getName());
                dataRow.createCell(1).setCellValue(examStatusInfo.getSubject().getEspb());
                dataRow.createCell(2).setCellValue(examStatusInfo.getGrade());
                dataRow.createCell(3).setCellValue(examStatusInfo.getStatus().toString());
            }
        }
        if(passed){
            for (ExamStatusInfo examStatusInfo : passedExams) {
                Row dataRow = sheet.createRow(rowNum++);
                espbPassed += examStatusInfo.getSubject().getEspb();
                dataRow.createCell(0).setCellValue(examStatusInfo.getSubject().getName());
                dataRow.createCell(1).setCellValue(examStatusInfo.getSubject().getEspb());
                dataRow.createCell(2).setCellValue(examStatusInfo.getGrade());
                dataRow.createCell(3).setCellValue(examStatusInfo.getStatus().toString());
            }
            Row totalRow = sheet.createRow(rowNum++);
            totalRow.createCell(0).setCellValue("TOTAL ESPB:");
            totalRow.createCell(1).setCellValue(espbPassed);
        }
        if(unpassed){
            for (ExamStatusInfo examStatusInfo : unpassedExams) {
                Row dataRow = sheet.createRow(rowNum++);
                espbPassed += examStatusInfo.getSubject().getEspb();
                dataRow.createCell(0).setCellValue(examStatusInfo.getSubject().getName());
                dataRow.createCell(1).setCellValue(examStatusInfo.getSubject().getEspb());
                dataRow.createCell(2).setCellValue(examStatusInfo.getGrade());
                dataRow.createCell(3).setCellValue(examStatusInfo.getStatus().toString());
            }
        }

        // set column widths
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);

        // generate file and download
        FileOutputStream outputStream = new FileOutputStream(fileName);
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
