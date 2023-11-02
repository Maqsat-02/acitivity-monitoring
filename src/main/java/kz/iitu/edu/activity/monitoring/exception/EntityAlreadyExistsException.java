package kz.iitu.edu.activity.monitoring.exception;

public class EntityAlreadyExistsException extends BadRequestApiException {
//    public EntityAlreadyExistsException(String entityName, String uniqueFieldName, String uniqueFieldValue) {
//        super(entityName + " with " + uniqueFieldName + " '" + uniqueFieldValue + "' already exists");
//    }

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}
