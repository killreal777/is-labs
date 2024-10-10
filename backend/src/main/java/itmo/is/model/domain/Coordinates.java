package itmo.is.model.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {
    @NotNull
    @Column(nullable = false)
    private Long x;

    @NotEmpty
    @Column(nullable = false)
    private Float y;
}
