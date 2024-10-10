package itmo.is.model.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "chapters")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_legion")
    private String parentLegion;

    @Min(1) @Max(1000)
    @Column(name = "marines_count")
    private long marinesCount;
}
