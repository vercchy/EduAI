package finku.ukim.mk.eduai.controller;

import finku.ukim.mk.eduai.dto.StudentReportDto;
import finku.ukim.mk.eduai.service.impl.PdfExportService;
import finku.ukim.mk.eduai.service.impl.StudentReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/student-report")
public class StudentReportController {

    private final StudentReportService studentReportService;
    private final PdfExportService pdfExportService;

    public StudentReportController(StudentReportService studentReportService, PdfExportService pdfExportService) {
        this.studentReportService = studentReportService;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<StudentReportDto> getStudentReport(@PathVariable Long userId) {
        StudentReportDto report = studentReportService.getStudentReport(userId);
        return ResponseEntity.ok(report);
    }

    @GetMapping(value = "/student-report/{userId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportStudentReportPdf(@PathVariable Long userId) {
        StudentReportDto report = studentReportService.getStudentReport(userId);
        byte[] pdf = pdfExportService.exportStudentReport(report);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=student_report.pdf")
                .body(pdf);
    }

}

