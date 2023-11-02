package kz.iitu.edu.activity.monitoring.exception;

public class ExtraChiefEditorNotInProjectException extends NotFoundApiException {
    public ExtraChiefEditorNotInProjectException(String chiefEditorId, Long projectId) {
        super("Chief editor " + chiefEditorId + " is not assigned as extra to project " + projectId);
    }
}
