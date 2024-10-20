package itmo.is.repository;

import itmo.is.model.domain.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ChapterRepository extends JpaRepository<Chapter, Long> {
    Page<Chapter> findAllByName(@NonNull String name, @NonNull Pageable pageable);

    Page<Chapter> findAllByParentLegion(@NonNull String parentLegion, @NonNull Pageable pageable);

    Page<Chapter> findAllByNameAndParentLegion(@NonNull String name, @NonNull String parentLegion, @NonNull Pageable pageable);
}
