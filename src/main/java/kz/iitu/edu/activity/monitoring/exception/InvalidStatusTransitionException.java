package kz.iitu.edu.activity.monitoring.exception;

public class InvalidStatusTransitionException extends ForbiddenApiException{
    public InvalidStatusTransitionException(String userRole, String entityToUpdate, String fromStatus, String toStatus) {
        super(userRole + " is not allowed to update status of " + entityToUpdate + " from " + fromStatus + " to " + toStatus);
    }
}
