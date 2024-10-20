package itmo.is.service.security.authorization;

import itmo.is.model.security.OwnedEntity;
import itmo.is.model.security.Role;
import itmo.is.model.security.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public abstract class OwnerEntitySecurityService<T extends OwnedEntity, ID> {

    protected abstract T findById(ID id);

    public boolean isOwner(ID ownedEntityId) {
        var entity = findById(ownedEntityId);
        var currentUser = getCurrentUser();

        return isOwner(currentUser, entity);
    }

    public boolean hasEditRights(ID ownedEntityId) {
        var entity = findById(ownedEntityId);
        var currentUser = getCurrentUser();

        return hasEditRights(currentUser, entity);
    }

    private boolean hasEditRights(User user, OwnedEntity entity) {
        boolean isOwner = isOwner(user, entity);
        boolean isAdminAndIsAdminEditAllowed = user.getRole() == Role.ROLE_ADMIN && entity.isAdminEditAllowed();

        return isOwner || isAdminAndIsAdminEditAllowed;
    }

    private boolean isOwner(User user, OwnedEntity entity) {
        return entity.getOwner().getId().equals(user.getId());
    }

    private User getCurrentUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
