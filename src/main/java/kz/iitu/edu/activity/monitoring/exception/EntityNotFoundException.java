package kz.iitu.edu.activity.monitoring.exception;

public class EntityNotFoundException extends NotFoundApiException {
    public EntityNotFoundException(String entityName, Long entityId) {
        this(entityName, Long.toString(entityId));
    }

    public EntityNotFoundException(String entityName, String entityId) {
        super(entityName + " " + entityId + " not found");
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
