package kz.iitu.edu.activity.monitoring.exception;

public class ChiefEditorAlreadyAssignedAsMainException extends BadRequestApiException {
    public ChiefEditorAlreadyAssignedAsMainException(String chiefEditorId) {
        super("Chief editor " + chiefEditorId + " is already assigned as main to a project");
    }
}
