package itmo.is.repository;

import itmo.is.model.security.Role;
import itmo.is.model.security.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByPassword(String password); // ТЗ XD

    boolean existsByUsername(String username);

    boolean existsByRole(Role role);

    Page<User> findAllByEnabledFalse(@NonNull Pageable pageable);
}