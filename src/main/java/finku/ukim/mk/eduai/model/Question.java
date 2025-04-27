package finku.ukim.mk.eduai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "question_type")
    private QuestionType questionType;

    @Column(name = "max_points", nullable = false)
    private Float maxPoints;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;
}
