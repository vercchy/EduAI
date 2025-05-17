package finku.ukim.mk.eduai.service.interfaces;

import finku.ukim.mk.eduai.dto.ProfessorReportDto;

public interface ProfessorReportServiceInterface {
    ProfessorReportDto getProfessorReport(Long subjectId, Long testId);
}
