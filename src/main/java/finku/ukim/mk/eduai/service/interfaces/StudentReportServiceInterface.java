package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.StudentReportDto;

public interface StudentReportServiceInterface {
    StudentReportDto getStudentReport(Long userId);
}
