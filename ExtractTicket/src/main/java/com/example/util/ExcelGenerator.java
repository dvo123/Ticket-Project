package com.example.util;

import com.example.model.Ticket;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExcelGenerator {
    private static final Logger logger = LoggerFactory.getLogger(ExcelGenerator.class);
    private static final String[] HEADERS = {
            "ID", "TITLE", "CREATED USER's USERNAME", "CREATED USER", "CREATE TIME",
            "FINISHED TIME", "LOCATION_ID", "STATUS", "Approver", "Request Description",
            "Benefit/ Reason", "Impacted applications"
    };

    private final String fontName;
    private final IndexedColors headerBackground;

    @Autowired
    public ExcelGenerator(
            @Value("${excel.font.name:Times New Roman}") String fontName,
            @Value("${excel.header.background:GREY_25_PERCENT}") String headerBackground) {
        this.fontName = fontName;
        this.headerBackground = IndexedColors.valueOf(headerBackground);
    }

    public Workbook createWorkbook(List<Ticket> tickets) {
        logger.debug("Creating Excel workbook with {} tickets", tickets.size());
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Tickets");

        createHeaderRow(sheet, workbook);
        populateDataRows(sheet, workbook, tickets);

        return workbook;
    }

    private void createHeaderRow(Sheet sheet, Workbook workbook) {
        Row headerRow = sheet.createRow(0);
        CellStyle headerStyle = createHeaderStyle(workbook);

        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
            cell.setCellStyle(headerStyle);
            sheet.autoSizeColumn(i);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName(fontName);
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(headerBackground.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private void populateDataRows(Sheet sheet, Workbook workbook, List<Ticket> tickets) {
        int rowNum = 1;
        CellStyle dataStyle = workbook.createCellStyle();
        Font dataFont = workbook.createFont();
        dataFont.setFontName(fontName);
        dataStyle.setFont(dataFont);

        for (Ticket ticket : tickets) {
            Row row = sheet.createRow(rowNum++);
            setCellValue(row, 0, ticket.getId(), dataStyle);
            setCellValue(row, 1, ticket.getTitle(), dataStyle);
            setCellValue(row, 2, ticket.getCreatedUsersUsername(), dataStyle);
            setCellValue(row, 3, ticket.getCreatedUser(), dataStyle);
            setCellValue(row, 4, ticket.getCreateTime(), dataStyle);
            setCellValue(row, 5, ticket.getFinishedTime(), dataStyle);
            setCellValue(row, 6, ticket.getLocationId(), dataStyle);
            setCellValue(row, 7, ticket.getStatus(), dataStyle);
            setCellValue(row, 8, ticket.getApprover(), dataStyle);
            setCellValue(row, 9, ticket.getRequestDescription(), dataStyle);
            setCellValue(row, 10, ticket.getBenefitReason(), dataStyle);
            setCellValue(row, 11, ticket.getImpactedApplications(), dataStyle);
        }
    }

    private void setCellValue(Row row, int column, Object value, CellStyle style) {
        Cell cell = row.createCell(column);
        if (value instanceof String str) {
            cell.setCellValue(str);
        } else if (value instanceof Integer num) {
            cell.setCellValue(num);
        } else {
            cell.setCellValue(value != null ? value.toString() : "");
        }
        cell.setCellStyle(style);
    }
}