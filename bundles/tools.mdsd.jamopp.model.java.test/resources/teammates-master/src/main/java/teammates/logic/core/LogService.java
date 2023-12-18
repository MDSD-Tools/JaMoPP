package teammates.logic.core;

import java.util.List;

import teammates.common.datatransfer.ErrorLogEntry;
import teammates.common.datatransfer.FeedbackSessionLogEntry;
import teammates.common.datatransfer.QueryLogsResults;
import teammates.common.datatransfer.logs.QueryLogsParams;

/**
 * An interface used for logs operations such as reading/writing.
 */
public interface LogService {

    /**
     * Gets the list of recent error- or higher level logs.
     */
    List<ErrorLogEntry> getRecentErrorLogs();

    /**
     * Gets the list of logs satisfying the given criteria.
     */
    QueryLogsResults queryLogs(QueryLogsParams queryLogsParams);

    /**
     * Creates a feedback session log.
     */
    void createFeedbackSessionLog(String courseId, String email, String fsName, String fslType);

    /**
     * Gets the feedback session logs as filtered by the given parameters.
     */
    List<FeedbackSessionLogEntry> getFeedbackSessionLogs(String courseId, String email,
            long startTime, long endTime, String fsName);
}
