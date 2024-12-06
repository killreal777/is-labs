package itmo.is.repository.history;

import itmo.is.model.history.ImportLog;
import itmo.is.model.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ImportLogRepository<T extends ImportLog> extends JpaRepository<T, Long> {
    Page<T> findAllByUser(User user, Pageable pageable);
}
