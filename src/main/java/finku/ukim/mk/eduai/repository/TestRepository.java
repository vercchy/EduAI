package finku.ukim.mk.eduai.repository;

import finku.ukim.mk.eduai.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TestRepository extends JpaRepository<Test, Long> {

    List<Test> findBySubjectId(Long subjectId);
}