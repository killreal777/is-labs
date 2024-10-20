package itmo.is.service;

import itmo.is.model.domain.SpaceMarine;
import itmo.is.repository.SpaceMarineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceMarineSecurityService extends OwnerEntitySecurityService<SpaceMarine, Long> {
    private final SpaceMarineRepository spaceMarineRepository;

    @Override
    protected SpaceMarine findById(Long id) {
        return spaceMarineRepository.findById(id);
    }
}
