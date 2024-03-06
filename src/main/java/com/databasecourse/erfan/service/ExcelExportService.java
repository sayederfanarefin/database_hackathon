package com.databasecourse.erfan.service;

import com.databasecourse.erfan.web.dto.GradeCenterItemDto;
import com.databasecourse.erfan.web.dto.UserDashScoreBreakDownDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ExcelExportService {
    public String generateAndSaveExcelFile(List<GradeCenterItemDto> gradeCenterItems, String filePath) throws IOException {
        // Create a new Excel workbook
        Workbook workbook = new XSSFWorkbook();

        // Create a sheet
        Sheet sheet = workbook.createSheet("Grade Center Items");

        // Create a header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("First Name");
        headerRow.createCell(0).setCellValue("Last Name");
        headerRow.createCell(0).setCellValue("Email");
        headerRow.createCell(1).setCellValue("Total Points");
        headerRow.createCell(2).setCellValue("Task Title");
        headerRow.createCell(3).setCellValue("Task ID");
        headerRow.createCell(4).setCellValue("Time Taken");
        headerRow.createCell(5).setCellValue("Complete");
        headerRow.createCell(6).setCellValue("Max Attempts");
        headerRow.createCell(7).setCellValue("Attempts");
        headerRow.createCell(8).setCellValue("Points");

        // Populate the Excel sheet with data
        int rowNum = 1;
        for (GradeCenterItemDto gradeCenterItem : gradeCenterItems) {
            for (UserDashScoreBreakDownDto scoreBreakDown : gradeCenterItem.getUserDashScoreBreakDownDtoList()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(gradeCenterItem.getUser().getFirstName());
                row.createCell(0).setCellValue(gradeCenterItem.getUser().getLastName());
                row.createCell(0).setCellValue(gradeCenterItem.getUser().getEmail());
                row.createCell(1).setCellValue(gradeCenterItem.getTotalPoints());
                row.createCell(2).setCellValue(scoreBreakDown.getTaskTitle());
                row.createCell(3).setCellValue(scoreBreakDown.getTaskId());
                row.createCell(4).setCellValue(scoreBreakDown.getTimeTaken());
                row.createCell(5).setCellValue(scoreBreakDown.isComplete() ? "Yes" : "No");
                row.createCell(6).setCellValue(scoreBreakDown.getMaxAttempts());
                row.createCell(7).setCellValue(scoreBreakDown.getAttempts());
                row.createCell(8).setCellValue(scoreBreakDown.getPoints());
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream(filePath)) {
            workbook.write(outputStream);
        }

        return filePath;
    }

}
