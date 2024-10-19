package itmo.is.service;

import itmo.is.model.security.User;
import itmo.is.repository.SpaceMarineRepository;
import itmo.is.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpaceMarineSecurityService {
    private final SpaceMarineRepository spaceMarineRepository;
    private final UserRepository userRepository;

    public boolean isOwner(Long spaceMarineId) {
        var spaceMarine = spaceMarineRepository.findById(spaceMarineId);
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var currentUser = (User) authentication.getPrincipal();

        return spaceMarine.getCreatedBy().getId().equals(currentUser.getId());
    }
}
