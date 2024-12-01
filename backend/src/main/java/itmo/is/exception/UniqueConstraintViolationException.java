package itmo.is.exception;

import jakarta.validation.ValidationException;

public class UniqueConstraintViolationException extends ValidationException {
    public UniqueConstraintViolationException(Class<?> entity, String field, String value) {
        super(String.format(
                "Unique constraint violation: Value '%s' of field '%s' for entity '%s' is non unique",
                value, field, entity.getSimpleName()
        ));
    }
}
