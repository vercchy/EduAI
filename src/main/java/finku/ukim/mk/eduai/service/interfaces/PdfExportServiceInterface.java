package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.ProfessorReportDto;
import finku.ukim.mk.eduai.dto.StudentReportDto;

public interface PdfExportServiceInterface {

    byte[] exportStudentReport(StudentReportDto report);

    byte[] exportProfessorReport(ProfessorReportDto report);
}

