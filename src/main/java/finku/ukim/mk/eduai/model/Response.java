package finku.ukim.mk.eduai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float score = 0f;

    @ManyToOne
    @JoinColumn(name = "test_attempt_id", nullable = false)
    private TestAttempt testAttempt;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    public Response(TestAttempt testAttempt, Question question) {
        this.testAttempt = testAttempt;
        this.question = question;
    }
}
