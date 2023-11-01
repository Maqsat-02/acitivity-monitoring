package kz.iitu.edu.activity.monitoring.exception;

public class ChiefEditorBusyInProjectException extends BadRequestApiException {
    public ChiefEditorBusyInProjectException(String chiefEditorId, Long projectId) {
        super("Chief editor " + chiefEditorId + " is currently busy in project " + projectId);
    }
}
