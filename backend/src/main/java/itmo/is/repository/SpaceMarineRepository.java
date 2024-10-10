package itmo.is.repository;

import itmo.is.model.domain.SpaceMarine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpaceMarineRepository extends JpaRepository<SpaceMarine, Integer> {
    List<SpaceMarine> findAll();

    SpaceMarine findById(Long id);

    SpaceMarine save(SpaceMarine spaceMarine);

    void deleteById(Long id);
}
