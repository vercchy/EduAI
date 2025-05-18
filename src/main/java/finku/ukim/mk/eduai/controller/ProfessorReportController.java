package finku.ukim.mk.eduai.controller;
import finku.ukim.mk.eduai.dto.ProfessorReportDto;
import finku.ukim.mk.eduai.exception.ResourceNotFoundException;
import finku.ukim.mk.eduai.model.Test;
import finku.ukim.mk.eduai.service.impl.PdfExportService;
import finku.ukim.mk.eduai.service.impl.ProfessorReportService;
import finku.ukim.mk.eduai.service.impl.TestService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.module.ResolutionException;
import java.util.List;

@RestController
@RequestMapping("/professor-report")
public class ProfessorReportController {

    private final ProfessorReportService professorReportService;
    private final PdfExportService pdfExportService;
    private final TestService testService;

    public ProfessorReportController(ProfessorReportService professorReportService, PdfExportService pdfExportService, TestService testService) {
        this.professorReportService = professorReportService;
        this.pdfExportService = pdfExportService;
        this.testService = testService;
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<ProfessorReportDto> getProfessorReport(
            @PathVariable Long subjectId) {
        List<Test> tests = testService.getTestsForSubject(subjectId);
        if (tests.isEmpty())
            throw new ResourceNotFoundException("No tests found for subject");
        Test lastTest = tests.get(tests.size() - 1);
        return ResponseEntity.ok(professorReportService.getProfessorReport(subjectId, lastTest.getId()));
    }

    @GetMapping(value = "{subjectId}/{testId}/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> exportProfessorReportPdf(@PathVariable Long subjectId, @PathVariable Long testId) {
        ProfessorReportDto report = professorReportService.getProfessorReport(subjectId, testId);
        byte[] pdf = pdfExportService.exportProfessorReport(report);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=professor_report.pdf")
                .body(pdf);
    }

}

