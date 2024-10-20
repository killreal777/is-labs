package itmo.is.model.domain;

import itmo.is.model.security.OwnedEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "chapters")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Chapter extends OwnedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "parent_legion")
    private String parentLegion;

    @Min(0) @Max(1000)
    @Column(name = "marines_count")
    private long marinesCount;

    public void incrementMarinesCount() {
        marinesCount++;
    }

    public void decrementMarinesCount() {
        if (marinesCount == 0) {
            throw new IllegalStateException("Cannot decrement marines count");
        }
        marinesCount--;
    }
}
