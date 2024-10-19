package itmo.is.service;

import itmo.is.model.domain.SpaceMarine;
import itmo.is.model.security.Role;
import itmo.is.model.security.User;
import itmo.is.repository.SpaceMarineRepository;
import itmo.is.repository.UserRepository;
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
        var currentUser = getCurrentUser();
        return isOwner(currentUser, spaceMarine);
    }

    public boolean hasEditRights(Long spaceMarineId) {
        var spaceMarine = spaceMarineRepository.findById(spaceMarineId);
        var currentUser = getCurrentUser();
        return hasEditRights(currentUser, spaceMarine);
    }

    private boolean hasEditRights(User user, SpaceMarine spaceMarine) {
        boolean isOwner = isOwner(user, spaceMarine);
        boolean isAdminAndIsAdminEditAllowed = user.getRole() == Role.ROLE_ADMIN && spaceMarine.isAdminEditAllowed();

        return isOwner || isAdminAndIsAdminEditAllowed;
    }

    private boolean isOwner(User user, SpaceMarine spaceMarine) {
        return spaceMarine.getCreatedBy().getId().equals(user.getId());
    }

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
