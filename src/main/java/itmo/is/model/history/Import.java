package itmo.is.model.history;
import itmo.is.model.security.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Import {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @CreatedBy
    private User user;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "objects_added")
    private Integer objectsAdded;

    @Column(name = "started_at", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime startedAt;

    @Column(name = "finished_at")
    @LastModifiedDate
    private LocalDateTime finishedAt;
}
