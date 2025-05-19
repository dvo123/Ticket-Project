package com.example.controller;

import com.example.exception.TicketExportException;
import com.example.model.Ticket;
import com.example.service.TicketExportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.io.File;
import java.util.List;

@Controller
@RequestMapping("/api")
public class TicketExportController {

    private static final Logger logger = LoggerFactory.getLogger(TicketExportController.class);
    private final TicketExportService ticketExportService;

    @Autowired
    public TicketExportController(TicketExportService ticketExportService) {
        this.ticketExportService = ticketExportService;
    }

    @GetMapping(value = "/export", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<FileSystemResource> exportTicketsApi() {
        logger.info("Received API request to export tickets");
        File excelFile = ticketExportService.exportTicketsToExcel();
        FileSystemResource resource = new FileSystemResource(excelFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.xlsx");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add("X-Export-Status", "Generated output.xlsx successfully!");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @GetMapping("/export-page")
    public String showExportPage(Model model) {
        try {
            List<Ticket> tickets = ticketExportService.getTickets();
            model.addAttribute("tickets", tickets);
            model.addAttribute("message", "Review tickets below and click to export");
        } catch (Exception e) {
            logger.error("Error fetching tickets for preview", e);
            model.addAttribute("message", "Error loading tickets: " + e.getMessage());
            model.addAttribute("tickets", List.of());
        }
        return "export";
    }

    @GetMapping(value = "/export-web", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity<FileSystemResource> exportTicketsWeb() {
        logger.info("Received web request to export tickets");
        File excelFile = ticketExportService.exportTicketsToExcel();
        FileSystemResource resource = new FileSystemResource(excelFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=output.xlsx");
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        headers.add("X-Export-Status", "Generated output.xlsx successfully!");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(excelFile.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @ExceptionHandler(TicketExportException.class)
    public ResponseEntity<ErrorResponse> handleExportException(TicketExportException e) {
        logger.error("Export error: {}", e.getMessage(), e);
        return ResponseEntity.status(500)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorResponse(e.getMessage(), e.getCause() != null ? e.getCause().getMessage() : "No details"));
    }

    private static class ErrorResponse {
        private final String message;
        private final String details;

        public ErrorResponse(String message, String details) {
            this.message = message;
            this.details = details;
        }

        @SuppressWarnings("unused")
		public String getMessage() { return message; }
        @SuppressWarnings("unused")
		public String getDetails() { return details; }
    }
}