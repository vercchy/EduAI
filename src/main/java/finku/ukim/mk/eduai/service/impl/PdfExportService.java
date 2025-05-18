package finku.ukim.mk.eduai.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import finku.ukim.mk.eduai.dto.ProfessorReportDto;
import finku.ukim.mk.eduai.dto.StudentReportDto;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

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

            document.add(new Paragraph("Professor Report"));
            document.add(new Paragraph("Average Correct %: " + report.getAverageCorrectPercent()));
            document.add(Chunk.NEWLINE);

            // Score Trend
            document.add(new Paragraph("Score Trend:"));
            if (report.getScoreTrend() != null && !report.getScoreTrend().isEmpty()) {
                for (var trend : report.getScoreTrend()) {
                    document.add(new Paragraph("  • " + trend.getDate() + ": " + trend.getAverageScore()));
                }
            } else {
                document.add(new Paragraph("  No data available."));
            }

            document.add(Chunk.NEWLINE);

            // Weak Areas
            document.add(new Paragraph("Weak Areas:"));
            if (report.getWeakAreas() != null && !report.getWeakAreas().isEmpty()) {
                for (var weak : report.getWeakAreas()) {
                    document.add(new Paragraph("  • Q" + weak.getQuestionId() + ": " + weak.getQuestionText() +
                            " (avg score: " + weak.getAverageScore() + ")"));
                }
            } else {
                document.add(new Paragraph("  None detected."));
            }

            document.add(Chunk.NEWLINE);

            // Hardest Question
            if (report.getHardestQuestion() != null) {
                document.add(new Paragraph("Hardest Question:"));
                document.add(new Paragraph("  Q" + report.getHardestQuestion().getId() + ": " +
                        report.getHardestQuestion().getText()));
            } else {
                document.add(new Paragraph("Hardest Question: N/A"));
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }}

