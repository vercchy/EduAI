package finku.ukim.mk.eduai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comment;

    private Float score = 0f;

    @Column(name = "is_ai_generated")
    private Boolean isAIGenerated = true;

    @OneToOne
    @JoinColumn(name = "open_ended_response_id", nullable = false)
    private OpenEndedResponse openEndedResponse;
}
