package itmo.is.repository;

import itmo.is.model.domain.SpaceMarine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceMarineRepository extends JpaRepository<SpaceMarine, Long> {
    Page<SpaceMarine> findAllByName(@NonNull String name, @NonNull Pageable pageable);

    Page<SpaceMarine> findAllByNameContaining(@NonNull String substring, @NonNull Pageable pageable);

    @Query("SELECT sm.creationDate, COUNT(sm) FROM SpaceMarine sm GROUP BY sm.creationDate")
    List<Object[]> countByCreationDateGrouped();


    Page<SpaceMarine> findAllByLoyal(@NonNull boolean loyal, @NonNull Pageable pageable);
}
