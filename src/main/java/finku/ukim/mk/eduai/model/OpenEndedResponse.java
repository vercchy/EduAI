package finku.ukim.mk.eduai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "open_ended_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OpenEndedResponse {

    @Id
    @Column(name = "response_id")
    private Long responseId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "response_id")
    private Response response;

    @Column(name = "answer_text")
    private String answerText;

    public OpenEndedResponse(Response response, String answerText) {
        this.response = response;
        this.answerText = answerText;
    }
}
