package finku.ukim.mk.eduai.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import finku.ukim.mk.eduai.dto.ProfessorReportDto;
import finku.ukim.mk.eduai.dto.StudentReportDto;
import finku.ukim.mk.eduai.model.Response;
import finku.ukim.mk.eduai.model.TestAttempt;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.stream.Collectors;

@Service
public class PdfExportService {

    public byte[] exportStudentReport(StudentReportDto report) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            document.add(new Paragraph("Student Report"));
            document.add(new Paragraph("Correct Percentage: " + report.getCorrectPercentage()));
            document.add(new Paragraph("Score Trend: " + report.getScoreTrend()));
            document.add(new Paragraph("Weak Areas: " + report.getWeakAreas().toString()));
            document.add(new Paragraph("Hardest Question: " + report.getHardestQuestion().getQuestionText()));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }

    public byte[] exportProfessorReport(ProfessorReportDto report) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Document document = new Document();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph header = new Paragraph("Professor Report", headerFont);
            header.setAlignment(Paragraph.ALIGN_CENTER);
            header.setSpacingAfter(20f);
            document.add(header);

            Font sectionTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            document.add(new Paragraph("Average Correct %:", sectionTitleFont));
            document.add(new Paragraph(String.valueOf(report.getAverageCorrectPercent())));
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Score Trend:", sectionTitleFont));
            if (report.getScoreTrend() != null && !report.getScoreTrend().isEmpty()) {
                for (var trend : report.getScoreTrend()) {
                    document.add(new Paragraph("  • " + trend.getDate() + ": " + trend.getAverageScore()));
                }
            } else {
                document.add(new Paragraph("  No data available."));
            }
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Weak Areas:", sectionTitleFont));
            if (report.getWeakAreas() != null && !report.getWeakAreas().isEmpty()) {
                for (var weak : report.getWeakAreas()) {
                    document.add(new Paragraph("  • Q" + weak.getQuestionId() + ": " + weak.getQuestionText()
                            + " (avg score: " + weak.getAverageScore() + ")"));
                }
            } else {
                document.add(new Paragraph("  None detected."));
            }
            document.add(Chunk.NEWLINE);

            document.add(new Paragraph("Hardest Question:", sectionTitleFont));
            if (report.getHardestQuestion() != null) {
                document.add(new Paragraph("  Q" + report.getHardestQuestion().getId() + ": "
                        + report.getHardestQuestion().getText()));
            } else {
                document.add(new Paragraph("  N/A"));
            }
            document.add(Chunk.NEWLINE);

            if (report.getResponses() != null && !report.getResponses().isEmpty()) {

                document.add(new Paragraph("Test Attempts Table:", sectionTitleFont));

                PdfPTable table = new PdfPTable(3);
                table.setWidthPercentage(100);
                table.setSpacingBefore(10f);

                Font tableHeaderFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
                table.addCell(new Phrase("Student Name", tableHeaderFont));
                table.addCell(new Phrase("Total Score", tableHeaderFont));
                table.addCell(new Phrase("Submission Date", tableHeaderFont));

                report.getResponses().stream()
                        .map(Response::getTestAttempt)
                        .collect(Collectors.toMap(
                                TestAttempt::getId,
                                ta -> ta,
                                (a, b) -> a
                        ))
                        .values().stream()
                        .sorted(Comparator.comparing(TestAttempt::getSubmissionDate))
                        .forEach(attempt -> {
                            String studentName = attempt.getStudent().getUser().getFullName();
                            String score = String.valueOf(attempt.getTotalScore());
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                            String date = attempt.getSubmissionDate().format(formatter);

                            table.addCell(studentName);
                            table.addCell(score);
                            table.addCell(date);
                        });
                document.add(table);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }
}
