package itmo.is.service.security.authorization;

import itmo.is.model.domain.SpaceMarine;
import itmo.is.repository.domain.SpaceMarineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceMarineSecurityService extends OwnerEntitySecurityService<SpaceMarine, Long> {
    private final SpaceMarineRepository spaceMarineRepository;

    @Override
    protected SpaceMarine findById(Long id) {
        return spaceMarineRepository.findById(id).orElseThrow();
    }
}
