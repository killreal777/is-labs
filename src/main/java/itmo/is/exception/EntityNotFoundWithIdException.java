package itmo.is.exception;

import jakarta.persistence.EntityNotFoundException;

public class EntityNotFoundWithIdException extends EntityNotFoundException {
    public EntityNotFoundWithIdException(Class<?> entityClass, Long id) {
        super(entityClass.getSimpleName() + " not found with id " + id);
    }
}
