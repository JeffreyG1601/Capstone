// src/main/java/com/project1/networkinventory/controller/AuditLogController.java
package com.project1.networkinventory.controller;

import com.project1.networkinventory.dto.AuditLogDTO;
import com.project1.networkinventory.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/audit-logs")
public class AuditLogController {

    private final AuditLogService svc;
    public AuditLogController(AuditLogService svc){ this.svc = svc; }

    @GetMapping
    public Page<AuditLogDTO> list(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String actionType,
        @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
        @RequestParam(defaultValue="0") int page,
        @RequestParam(defaultValue="20") int size
    ){
        return svc.search(username, actionType, from, to, PageRequest.of(page, size));
    }

    @GetMapping("/export/pdf")
    public ResponseEntity<byte[]> exportPdf(
        @RequestParam(required = false) String username,
        @RequestParam(required = false) String actionType,
        @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
        @RequestParam(required = false) @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) LocalDateTime to
    ) throws Exception {
        List<AuditLogDTO> rows = svc.findAllForExport(username, actionType, from, to);
        // simple PDF generation using iText or OpenPDF (assume dependency present)
        com.lowagie.text.Document doc = new com.lowagie.text.Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        com.lowagie.text.pdf.PdfWriter.getInstance(doc, baos);
        doc.open();
        doc.add(new com.lowagie.text.Paragraph("Audit Report"));
        doc.add(new com.lowagie.text.Paragraph("Generated: " + LocalDateTime.now().toString()));
        com.lowagie.text.pdf.PdfPTable table = new com.lowagie.text.pdf.PdfPTable(5);
        table.addCell("Timestamp"); table.addCell("User"); table.addCell("Action"); table.addCell("IP"); table.addCell("Description");
        for(var r: rows){
            table.addCell(r.getTimestamp().toString());
            table.addCell(r.getUsername());
            table.addCell(r.getActionType());
            table.addCell(r.getIpAddress());
            table.addCell(r.getDescription()==null? "": r.getDescription());
        }
        doc.add(table);
        doc.close();
        byte[] pdf = baos.toByteArray();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("audit-report.pdf").build());
        return new ResponseEntity<>(pdf, headers, HttpStatus.OK);
    }
}
