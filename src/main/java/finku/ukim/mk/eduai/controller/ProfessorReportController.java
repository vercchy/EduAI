package finku.ukim.mk.eduai.controller;
import finku.ukim.mk.eduai.dto.ProfessorReportDto;
import finku.ukim.mk.eduai.service.impl.PdfExportService;
import finku.ukim.mk.eduai.service.impl.ProfessorReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/professor-report")
public class ProfessorReportController {

    private final ProfessorReportService professorReportService;
    private final PdfExportService pdfExportService;

    public ProfessorReportController(ProfessorReportService professorReportService, PdfExportService pdfExportService) {
        this.professorReportService = professorReportService;
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/{subjectId}/{testId}")
    public ResponseEntity<ProfessorReportDto> getProfessorReport(
            @PathVariable Long subjectId,
            @PathVariable Long testId) {

        ProfessorReportDto report = professorReportService.getProfessorReport(subjectId, testId);
        return ResponseEntity.ok(report);
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

