package finku.ukim.mk.eduai.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "choice_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChoiceResponse {

    @Id
    @Column(name = "response_id")
    private Long responseId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "response_id")
    private Response response;

    public ChoiceResponse(Response response) {
        this.response = response;
    }
}
