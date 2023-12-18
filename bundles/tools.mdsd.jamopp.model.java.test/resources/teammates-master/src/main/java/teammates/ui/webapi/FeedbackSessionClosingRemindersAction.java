package teammates.ui.webapi;

import java.util.List;

import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.util.EmailWrapper;
import teammates.common.util.Logger;
import teammates.common.util.RequestTracer;

/**
 * Cron job: schedules feedback session closing emails to be sent.
 */
class FeedbackSessionClosingRemindersAction extends AdminOnlyAction {

    private static final Logger log = Logger.getLogger();

    @Override
    public JsonResult execute() {
        List<FeedbackSessionAttributes> sessions = logic.getFeedbackSessionsClosingWithinTimeLimit();

        for (FeedbackSessionAttributes session : sessions) {
            RequestTracer.checkRemainingTime();
            List<EmailWrapper> emailsToBeSent = emailGenerator.generateFeedbackSessionClosingEmails(session);
            try {
                taskQueuer.scheduleEmailsForSending(emailsToBeSent);
                logic.updateFeedbackSession(
                        FeedbackSessionAttributes
                                .updateOptionsBuilder(session.getFeedbackSessionName(), session.getCourseId())
                                .withSentClosingEmail(true)
                                .build());
            } catch (Exception e) {
                log.severe("Unexpected error", e);
            }
        }
        return new JsonResult("Successful");
    }

}
