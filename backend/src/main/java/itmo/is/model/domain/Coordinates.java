package itmo.is.model.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Embeddable
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
    @NotNull
    @Column(nullable = false)
    private Long x;

    @NotNull
    @Column(nullable = false)
    private Float y;
}
