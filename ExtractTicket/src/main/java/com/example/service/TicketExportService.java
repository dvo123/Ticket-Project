package com.example.service;

import com.example.exception.TicketExportException;
import com.example.mapper.TicketMapper;
import com.example.model.Ticket;
import com.example.util.ExcelGenerator;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class TicketExportService {

	private static final Logger logger = LoggerFactory.getLogger(TicketExportService.class);
	private final TicketMapper ticketMapper;
	private final ExcelGenerator excelGenerator;
	private final String outputPath;

	@Autowired
	public TicketExportService(TicketMapper ticketMapper, ExcelGenerator excelGenerator,
			@Value("${export.output.path:./output.xlsx}") String outputPath) {
		this.ticketMapper = ticketMapper;
		this.excelGenerator = excelGenerator;
		this.outputPath = outputPath;
	}

	@Async
	public CompletableFuture<File> exportTicketsToExcelAsync() {
		logger.debug("Starting async ticket export process");
		try {
			List<Ticket> tickets = ticketMapper.findAllTickets();
			if (tickets.isEmpty()) {
				logger.warn("No tickets found to export");
				throw new TicketExportException("No tickets available for export");
			}

			Workbook workbook = excelGenerator.createWorkbook(tickets);
			File outputFile = new File(outputPath);

			try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
				workbook.write(fileOut);
				logger.info("Excel file written to {}", outputFile.getAbsolutePath());
			} finally {
				workbook.close();
			}

			return CompletableFuture.completedFuture(outputFile);
		} catch (Exception e) {
			logger.error("Async export failed", e);
			throw new TicketExportException("Error during async export", e);
		}
	}

	public File exportTicketsToExcel() {
		return exportTicketsToExcelAsync().join();
	}
	
	public List<Ticket> getTickets() {
	    logger.debug("Fetching tickets for preview");
	    return ticketMapper.findAllTickets();
	}
}