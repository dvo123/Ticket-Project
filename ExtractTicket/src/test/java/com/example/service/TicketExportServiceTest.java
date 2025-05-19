package com.example.service;

import com.example.exception.TicketExportException;
import com.example.mapper.TicketMapper;
import com.example.model.Ticket;
import com.example.util.ExcelGenerator;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketExportServiceTest {

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private ExcelGenerator excelGenerator;

    @InjectMocks
    private TicketExportService ticketExportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketExportService = new TicketExportService(ticketMapper, excelGenerator, "test-output.xlsx");
    }

    @Test
    void exportTicketsToExcel_success() throws Exception {
        List<Ticket> tickets = List.of(new Ticket());
        when(ticketMapper.findAllTickets()).thenReturn(tickets);
        Workbook mockWorkbook = mock(Workbook.class);
        when(excelGenerator.createWorkbook(tickets)).thenReturn(mockWorkbook);

        File result = ticketExportService.exportTicketsToExcel();

        assertNotNull(result);
        assertEquals("test-output.xlsx", result.getName());
        verify(ticketMapper).findAllTickets();
        verify(excelGenerator).createWorkbook(tickets);
        verify(mockWorkbook).write(any());
        verify(mockWorkbook).close();
    }

    @Test
    void exportTicketsToExcel_noTickets() {
        when(ticketMapper.findAllTickets()).thenReturn(Collections.emptyList());

        assertThrows(TicketExportException.class, () -> ticketExportService.exportTicketsToExcel());
        verify(ticketMapper).findAllTickets();
        verifyNoInteractions(excelGenerator);
    }

    @Test
    void exportTicketsToExcel_databaseError() {
        when(ticketMapper.findAllTickets()).thenThrow(new RuntimeException("DB error"));

        assertThrows(TicketExportException.class, () -> ticketExportService.exportTicketsToExcel());
        verify(ticketMapper).findAllTickets();
        verifyNoInteractions(excelGenerator);
    }
}