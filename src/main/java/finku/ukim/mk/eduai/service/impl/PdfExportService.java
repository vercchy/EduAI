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
            document.add(new Paragraph("Score Trend: " + report.getScoreTrend()));
            document.add(new Paragraph("Weak Areas: " + report.getWeakAreas().toString()));
            document.add(new Paragraph("Hardest Question: " + report.getHardestQuestion().getText()));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out.toByteArray();
    }
}

