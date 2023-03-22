package com.asss.management.service.implementation;

import com.asss.management.dao.ExamsRepo;
import com.asss.management.entity.Exams;
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
}
