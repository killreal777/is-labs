package itmo.is.repository;

import itmo.is.model.domain.SpaceMarine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpaceMarineRepository extends JpaRepository<SpaceMarine, Integer> {
    SpaceMarine findById(Long id);

    SpaceMarine save(SpaceMarine spaceMarine);

    void deleteById(Long id);
}
