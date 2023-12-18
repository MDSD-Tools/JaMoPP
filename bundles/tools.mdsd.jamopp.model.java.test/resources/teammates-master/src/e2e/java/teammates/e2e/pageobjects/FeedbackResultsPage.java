package teammates.e2e.pageobjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import teammates.common.datatransfer.FeedbackParticipantType;
import teammates.common.datatransfer.attributes.FeedbackQuestionAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseAttributes;
import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.datatransfer.questions.FeedbackConstantSumQuestionDetails;
import teammates.common.datatransfer.questions.FeedbackConstantSumResponseDetails;
import teammates.common.datatransfer.questions.FeedbackMcqQuestionDetails;
import teammates.common.datatransfer.questions.FeedbackMsqQuestionDetails;
import teammates.common.datatransfer.questions.FeedbackNumericalScaleQuestionDetails;
import teammates.common.datatransfer.questions.FeedbackQuestionType;
import teammates.common.datatransfer.questions.FeedbackRankOptionsQuestionDetails;
import teammates.common.datatransfer.questions.FeedbackRankOptionsResponseDetails;
import teammates.common.datatransfer.questions.FeedbackResponseDetails;
import teammates.common.datatransfer.questions.FeedbackRubricQuestionDetails;
import teammates.common.datatransfer.questions.FeedbackRubricResponseDetails;
import teammates.common.util.Const;
import teammates.e2e.util.TestProperties;

/**
 * Page Object Model for feedback results page.
 */
public class FeedbackResultsPage extends AppPage {
    private static final String CURRENT_STUDENT_IDENTIFIER = "You";

    @FindBy(id = "course-id")
    private WebElement courseId;

    @FindBy(id = "session-name")
    private WebElement sessionName;

    @FindBy(id = "opening-time")
    private WebElement sessionOpeningTime;

    @FindBy(id = "closing-time")
    private WebElement sessionClosingTime;

    public FeedbackResultsPage(Browser browser) {
        super(browser);
    }

    @Override
    protected boolean containsExpectedPageContents() {
        return getPageTitle().contains("Feedback Session Results");
    }

    public void verifyFeedbackSessionDetails(FeedbackSessionAttributes feedbackSession) {
        assertEquals(getCourseId(), feedbackSession.getCourseId());
        assertEquals(getFeedbackSessionName(), feedbackSession.getFeedbackSessionName());
        assertDateEquals(getOpeningTime(), feedbackSession.getStartTime(), feedbackSession.getTimeZone());
        assertDateEquals(getClosingTime(), feedbackSession.getEndTime(), feedbackSession.getTimeZone());
    }

    public void verifyQuestionDetails(int questionNum, FeedbackQuestionAttributes question) {
        assertEquals(question.getQuestionDetailsCopy().getQuestionText(), getQuestionText(questionNum));
        if (!question.getQuestionType().equals(FeedbackQuestionType.TEXT)) {
            assertEquals(getAdditionalInfoString(question), getAdditionalInfo(questionNum));
        }
    }

    public void verifyResponseDetails(FeedbackQuestionAttributes question, List<FeedbackResponseAttributes> givenResponses,
                                      List<FeedbackResponseAttributes> otherResponses,
                                      Set<String> visibleGivers, Set<String> visibleRecipients) {
        if (!hasDisplayedResponses(question)) {
            return;
        }
        verifyGivenResponses(question, givenResponses);
        verifyOtherResponses(question, otherResponses, visibleGivers, visibleRecipients);
    }

    public void verifyQuestionNotPresent(int questionNum) {
        try {
            getQuestionResponsesSection(questionNum);
            fail("Question " + questionNum + " should not be present.");
        } catch (NoSuchElementException e) {
            // success
        }
    }

    public void verifyNumScaleStatistics(int questionNum, String[] expectedStats) {
        verifyTableRowValues(getNumScaleStatistics(questionNum), expectedStats);
    }

    public void verifyRubricStatistics(int questionNum, String[][] expectedStats, String[][] expectedStatsExcludingSelf,
                                       String[][] expectedStatsPerRecipient) {
        WebElement excludeSelfCheckbox = getRubricExcludeSelfCheckbox(questionNum);
        markOptionAsUnselected(excludeSelfCheckbox);
        verifyTableBodyValues(getRubricStatistics(questionNum), expectedStats);

        markOptionAsSelected(excludeSelfCheckbox);
        verifyTableBodyValues(getRubricStatistics(questionNum), expectedStatsExcludingSelf);

        sortRubricPerRecipientStatsPerCriterion(questionNum, 2);
        verifyTableBodyValues(getRubricPerRecipientStatsPerCriterion(questionNum), expectedStatsPerRecipient);

        sortRubricPerRecipientStatsOverall(questionNum, 2);
        verifyTableBodyValues(getRubricPerRecipientStatsPerCriterion(questionNum), expectedStatsPerRecipient);
    }

    public void verifyContributionStatistics(int questionNum, String[] expectedStats) {
        WebElement questionSection = getQuestionResponsesSection(questionNum);
        assertEquals(questionSection.findElement(By.id("own-view-me")).getText(), expectedStats[0]);
        assertEquals(questionSection.findElement(By.id("own-view-others")).getText().trim(), expectedStats[1]);
        assertEquals(questionSection.findElement(By.id("team-view-me")).getText(), expectedStats[2]);
        assertEquals(questionSection.findElement(By.id("team-view-others")).getText().trim(), expectedStats[3]);
    }

    public void verifyCommentDetails(int questionNum, String commentGiver, String commentEditor, String commentString) {
        WebElement commentField = getCommentField(questionNum, commentString);
        if (commentGiver.isEmpty()) {
            assertTrue(isCommentByResponseGiver(commentField));
        } else {
            assertEquals(commentGiver, getCommentGiver(commentField));
        }
        if (!commentEditor.isEmpty()) {
            assertEquals(commentEditor, getCommentEditor(commentField));
        }
    }

    private boolean hasDisplayedResponses(FeedbackQuestionAttributes question) {
        return !question.getQuestionDetailsCopy().getQuestionType().equals(FeedbackQuestionType.CONTRIB);
    }

    private void verifyGivenResponses(FeedbackQuestionAttributes question, List<FeedbackResponseAttributes> givenResponses) {
        for (FeedbackResponseAttributes response : givenResponses) {
            WebElement responseField = getGivenResponseField(question.getQuestionNumber(), response.getRecipient());
            assertTrue(isResponseEqual(question, responseField, response));
        }
    }

    private void verifyOtherResponses(FeedbackQuestionAttributes question, List<FeedbackResponseAttributes> otherResponses,
                                      Set<String> visibleGivers, Set<String> visibleRecipients) {
        Set<String> recipients = getRecipients(otherResponses);
        for (String recipient : recipients) {
            List<FeedbackResponseAttributes> expectedResponses = otherResponses.stream()
                    .filter(r -> r.getRecipient().equals(recipient)
                        && (question.isResponseVisibleTo(FeedbackParticipantType.RECEIVER)
                        || question.isResponseVisibleTo(FeedbackParticipantType.STUDENTS)
                        || question.isResponseVisibleTo(FeedbackParticipantType.OWN_TEAM_MEMBERS)
                        || question.isResponseVisibleTo(FeedbackParticipantType.RECEIVER_TEAM_MEMBERS)))
                    .collect(Collectors.toList());

            verifyResponseForRecipient(question, recipient, expectedResponses, visibleGivers, visibleRecipients);
        }
    }

    private Set<String> getRecipients(List<FeedbackResponseAttributes> responses) {
        return responses.stream().map(FeedbackResponseAttributes::getRecipient).collect(Collectors.toSet());
    }

    private void verifyResponseForRecipient(FeedbackQuestionAttributes question, String recipient,
                                            List<FeedbackResponseAttributes> otherResponses,
                                            Set<String> visibleGivers, Set<String> visibleRecipients) {
        List<WebElement> responseViews = getAllResponseViews(question.getQuestionNumber());
        for (FeedbackResponseAttributes response : otherResponses) {
            boolean isRecipientVisible = visibleRecipients.contains(response.getGiver())
                    || recipient.equals(CURRENT_STUDENT_IDENTIFIER);
            boolean isGiverVisible = visibleGivers.contains(response.getGiver())
                    || (visibleGivers.contains("RECEIVER") && response.getRecipient().equals(CURRENT_STUDENT_IDENTIFIER))
                    || response.getGiver().equals(CURRENT_STUDENT_IDENTIFIER);
            boolean isGiverVisibleToInstructor = question.getRecipientType() == FeedbackParticipantType.INSTRUCTORS
                    && visibleGivers.contains("INSTRUCTORS");
            if (isRecipientVisible) {
                int recipientIndex = getRecipientIndex(question.getQuestionNumber(), recipient);
                WebElement responseView = responseViews.get(recipientIndex);
                List<WebElement> responsesFields = getAllResponseFields(responseView);
                if (isGiverVisible || isGiverVisibleToInstructor) {
                    int giverIndex = getGiverIndex(responseView, response.getGiver());
                    assertTrue(isResponseEqual(question, responsesFields.get(giverIndex), response));
                } else {
                    assertTrue(isAnyAnonymousResponseEqual(question, responseView, response));
                }
            } else {
                verifyAnonymousResponseView(question, otherResponses, isGiverVisible);
            }
        }
    }

    private void verifyAnonymousResponseView(FeedbackQuestionAttributes question,
                                             List<FeedbackResponseAttributes> expectedResponses,
                                             boolean isGiverVisible) {
        List<WebElement> anonymousViews = getAllResponseViews(question.getQuestionNumber()).stream()
                .filter(v -> isAnonymous(v.findElement(By.id("response-recipient")).getText()))
                .collect(Collectors.toList());
        if (anonymousViews.isEmpty()) {
            fail("No anonymous views found");
        }

        boolean hasCorrectResponses = true;
        for (WebElement responseView : anonymousViews) {
            hasCorrectResponses = true;
            List<WebElement> responseFields = getAllResponseFields(responseView);
            for (FeedbackResponseAttributes response : expectedResponses) {
                if (isGiverVisible) {
                    int giverIndex = getGiverIndex(responseView, response.getGiver());
                    if (!isResponseEqual(question, responseFields.get(giverIndex), response)) {
                        hasCorrectResponses = false;
                        break;
                    }
                } else if (!isAnyAnonymousResponseEqual(question, responseView, response)) {
                    hasCorrectResponses = false;
                    break;
                }
            }
            if (hasCorrectResponses) {
                break;
            }
        }
        assertTrue(hasCorrectResponses);
    }

    private boolean isResponseEqual(FeedbackQuestionAttributes question, WebElement responseField,
                                    FeedbackResponseAttributes response) {
        if (question.getQuestionType().equals(FeedbackQuestionType.RUBRIC)) {
            return isRubricResponseEqual(responseField, response);
        } else {
            return getAnswerString(question, response.getResponseDetailsCopy()).equals(responseField.getText());
        }
    }

    private boolean isRubricResponseEqual(WebElement responseField, FeedbackResponseAttributes response) {
        FeedbackRubricResponseDetails responseDetails = (FeedbackRubricResponseDetails) response.getResponseDetailsCopy();
        List<Integer> answers = responseDetails.getAnswer();
        for (int i = 0; i < answers.size(); i++) {
            WebElement rubricRow = responseField.findElements(By.cssSelector("#rubric-answers tr")).get(i);
            WebElement rubricCell = rubricRow.findElements(By.tagName("td")).get(answers.get(i) + 1);
            if (rubricCell.findElements(By.className("fa-check")).size() == 0) {
                return false;
            }
        }
        return true;
    }

    private boolean isAnonymous(String identifier) {
        return identifier.contains(Const.DISPLAYED_NAME_FOR_ANONYMOUS_PARTICIPANT);
    }

    private boolean isAnyAnonymousResponseEqual(FeedbackQuestionAttributes question, WebElement responseView,
                                                FeedbackResponseAttributes response) {
        List<WebElement> giverNames = responseView.findElements(By.id("response-giver"));
        List<WebElement> responseFields = getAllResponseFields(responseView);
        for (int i = 0; i < giverNames.size(); i++) {
            if (isAnonymous(giverNames.get(i).getText()) && isResponseEqual(question, responseFields.get(i), response)) {
                return true;
            }
        }
        return false;
    }

    private String getCourseId() {
        return courseId.getText();
    }

    private String getFeedbackSessionName() {
        return sessionName.getText();
    }

    private String getOpeningTime() {
        return sessionOpeningTime.getText();
    }

    private String getClosingTime() {
        return sessionClosingTime.getText();
    }

    private void assertDateEquals(String actual, Instant instant, ZoneId timeZone) {
        String dateStrWithAbbr = getDateStringWithAbbr(instant, timeZone);
        String dateStrWithOffset = getDateStringWithOffset(instant, timeZone);

        assertTrue(actual.equals(dateStrWithAbbr) || actual.equals(dateStrWithOffset));
    }

    private String getDateStringWithAbbr(Instant instant, ZoneId timeZone) {
        return DateTimeFormatter
                .ofPattern("EE, dd MMM, yyyy, hh:mm a z")
                .format(instant.atZone(timeZone));
    }

    private String getDateStringWithOffset(Instant instant, ZoneId timeZone) {
        return DateTimeFormatter
                .ofPattern("EE, dd MMM, yyyy, hh:mm a X")
                .format(instant.atZone(timeZone));
    }

    private String getQuestionText(int questionNum) {
        return getQuestionResponsesSection(questionNum).findElement(By.id("question-text")).getText().trim();
    }

    private String getMcqAddInfo(FeedbackMcqQuestionDetails questionDetails) {
        String additionalInfo = "Multiple-choice (single answer) question options:" + TestProperties.LINE_SEPARATOR;
        return appendMultiChoiceInfo(additionalInfo, questionDetails.getGenerateOptionsFor(),
                questionDetails.getMcqChoices(), questionDetails.isOtherEnabled());
    }

    private String getMsqAddInfo(FeedbackMsqQuestionDetails questionDetails) {
        String additionalInfo = "Multiple-choice (multiple answers) question options:" + TestProperties.LINE_SEPARATOR;
        return appendMultiChoiceInfo(additionalInfo, questionDetails.getGenerateOptionsFor(),
                questionDetails.getMsqChoices(), questionDetails.isOtherEnabled());
    }

    private String appendMultiChoiceInfo(String info, FeedbackParticipantType generateOptionsFor, List<String> choices,
                                         boolean isOtherEnabled) {
        StringBuilder additionalInfo = new StringBuilder(info);
        if (generateOptionsFor.equals(FeedbackParticipantType.NONE)) {
            additionalInfo = appendOptions(additionalInfo, choices);
            if (isOtherEnabled) {
                additionalInfo.append(TestProperties.LINE_SEPARATOR).append("Other");
            }
        } else {
            additionalInfo.append("The options for this question is automatically generated from the list of all ")
                    .append(getDisplayGiverName(generateOptionsFor).toLowerCase())
                    .append('.');

        }
        return additionalInfo.toString();
    }

    private String getRubricAddInfo(FeedbackRubricQuestionDetails questionDetails) {
        StringBuilder additionalInfo = new StringBuilder("Rubric question sub-questions:");
        additionalInfo.append(TestProperties.LINE_SEPARATOR);
        return appendOptions(additionalInfo, questionDetails.getRubricSubQuestions()).toString();
    }

    private String getNumScaleAddInfo(FeedbackNumericalScaleQuestionDetails questionDetails) {
        return "Numerical-scale question:" + TestProperties.LINE_SEPARATOR
                + "Minimum value: " + questionDetails.getMinScale()
                + ". Increment: " + questionDetails.getStep()
                + ". Maximum value: " + questionDetails.getMaxScale() + ".";
    }

    private String getRankOptionsAddInfo(FeedbackRankOptionsQuestionDetails questionDetails) {
        StringBuilder additionalInfo = new StringBuilder("Rank (options) question options:");
        additionalInfo.append(TestProperties.LINE_SEPARATOR);
        return appendOptions(additionalInfo, questionDetails.getOptions()).toString();
    }

    private String getConstSumOptionsAddInfo(FeedbackConstantSumQuestionDetails questionDetails) {
        StringBuilder additionalInfo = new StringBuilder("Distribute points (among options) question options:");
        additionalInfo.append(TestProperties.LINE_SEPARATOR);
        additionalInfo = appendOptions(additionalInfo, questionDetails.getConstSumOptions());
        additionalInfo.append(TestProperties.LINE_SEPARATOR);
        if (questionDetails.isPointsPerOption()) {
            additionalInfo.append("Points per option: ");
        } else {
            additionalInfo.append("Total points: ");
        }
        additionalInfo.append(questionDetails.getPoints());
        return additionalInfo.toString();
    }

    private String getConstSumRecipientsAddInfo(FeedbackConstantSumQuestionDetails questionDetails) {
        StringBuilder additionalInfo = new StringBuilder("Distribute points (among recipients) question");
        additionalInfo.append(TestProperties.LINE_SEPARATOR);
        if (questionDetails.isPointsPerOption()) {
            additionalInfo.append("Points per recipient: ");
        } else {
            additionalInfo.append("Total points: ");
        }
        additionalInfo.append(questionDetails.getPoints());
        return additionalInfo.toString();
    }

    private StringBuilder appendOptions(StringBuilder info, List<String> options) {
        StringBuilder additionalInfo = info;
        for (String option : options) {
            additionalInfo.append(option).append(TestProperties.LINE_SEPARATOR);
        }
        return additionalInfo.deleteCharAt(additionalInfo.length() - 1);
    }

    private WebElement getQuestionResponsesSection(int questionNum) {
        return browser.driver.findElement(By.id("question-" + questionNum + "-responses"));
    }

    private void showAdditionalInfo(int qnNumber) {
        WebElement additionalInfoLink = getQuestionResponsesSection(qnNumber).findElement(By.id("additional-info-link"));
        if (additionalInfoLink.getText().equals("[more]")) {
            click(additionalInfoLink);
            waitUntilAnimationFinish();
        }
    }

    private String getAdditionalInfo(int questionNum) {
        showAdditionalInfo(questionNum);
        return getQuestionResponsesSection(questionNum).findElement(By.id("additional-info")).getText();
    }

    private WebElement getGivenResponseField(int questionNum, String receiver) {
        int recipientIndex = getGivenRecipientIndex(questionNum, receiver);
        return getQuestionResponsesSection(questionNum)
                .findElements(By.cssSelector("#given-responses tm-single-response"))
                .get(recipientIndex);
    }

    private int getGivenRecipientIndex(int questionNum, String recipient) {
        List<WebElement> recipients = getQuestionResponsesSection(questionNum)
                .findElements(By.cssSelector("#given-responses #response-recipient"));
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).getText().split("To: ")[1].equals(recipient)) {
                return i;
            }
        }
        throw new AssertionError("Recipient not found: " + recipient);
    }

    private String getAdditionalInfoString(FeedbackQuestionAttributes question) {
        switch (question.getQuestionType()) {
        case TEXT:
            return "";
        case MCQ:
            return getMcqAddInfo((FeedbackMcqQuestionDetails) question.getQuestionDetailsCopy());
        case MSQ:
            return getMsqAddInfo((FeedbackMsqQuestionDetails) question.getQuestionDetailsCopy());
        case RUBRIC:
            return getRubricAddInfo((FeedbackRubricQuestionDetails) question.getQuestionDetailsCopy());
        case NUMSCALE:
            return getNumScaleAddInfo((FeedbackNumericalScaleQuestionDetails) question.getQuestionDetailsCopy());
        case CONTRIB:
            return "Team contribution question";
        case RANK_OPTIONS:
            return getRankOptionsAddInfo((FeedbackRankOptionsQuestionDetails) question.getQuestionDetailsCopy());
        case RANK_RECIPIENTS:
            return "Rank (recipients) question";
        case CONSTSUM_OPTIONS:
            return getConstSumOptionsAddInfo((FeedbackConstantSumQuestionDetails) question.getQuestionDetailsCopy());
        case CONSTSUM_RECIPIENTS:
            return getConstSumRecipientsAddInfo((FeedbackConstantSumQuestionDetails) question.getQuestionDetailsCopy());
        default:
            throw new AssertionError("Unknown question type: " + question.getQuestionType());
        }
    }

    private String getAnswerString(FeedbackQuestionAttributes question, FeedbackResponseDetails response) {
        switch(response.getQuestionType()) {
        case TEXT:
        case NUMSCALE:
        case RANK_RECIPIENTS:
            return response.getAnswerString();
        case MCQ:
        case MSQ:
            return response.getAnswerString().replace(", ", TestProperties.LINE_SEPARATOR);
        case RANK_OPTIONS:
            return getRankOptionsAnsString((FeedbackRankOptionsQuestionDetails) question.getQuestionDetailsCopy(),
                    (FeedbackRankOptionsResponseDetails) response);
        case CONSTSUM:
            return getConstSumOptionsAnsString((FeedbackConstantSumQuestionDetails) question.getQuestionDetailsCopy(),
                    (FeedbackConstantSumResponseDetails) response);
        case RUBRIC:
        case CONTRIB:
            return "";
        default:
            throw new AssertionError("Unknown question type: " + response.getQuestionType());
        }
    }

    private String getRankOptionsAnsString(FeedbackRankOptionsQuestionDetails question,
                                           FeedbackRankOptionsResponseDetails responseDetails) {
        List<String> options = question.getOptions();
        List<Integer> answers = responseDetails.getAnswers();
        List<String> answerStrings = new ArrayList<>();
        for (int i = 1; i <= options.size(); i++) {
            answerStrings.add(i + ": " + options.get(answers.indexOf(i)));
        }
        return String.join(TestProperties.LINE_SEPARATOR, answerStrings);
    }

    private String getConstSumOptionsAnsString(FeedbackConstantSumQuestionDetails question,
                                               FeedbackConstantSumResponseDetails responseDetails) {
        if (question.isDistributeToRecipients()) {
            return responseDetails.getAnswerString();
        }
        List<String> options = question.getConstSumOptions();
        List<Integer> answers = responseDetails.getAnswers();
        List<String> answerStrings = new ArrayList<>();
        for (int i = 0; i < options.size(); i++) {
            answerStrings.add(options.get(i) + ": " + answers.get(i));
        }
        answerStrings.sort(Comparator.naturalOrder());
        return String.join(TestProperties.LINE_SEPARATOR, answerStrings);
    }

    private List<WebElement> getAllResponseViews(int questionNumber) {
        return getQuestionResponsesSection(questionNumber).findElements(By.tagName("tm-student-view-responses"));
    }

    private List<WebElement> getAllResponseFields(WebElement responseView) {
        return responseView.findElements(By.tagName("tm-single-response"));
    }

    private WebElement getNumScaleStatistics(int questionNum) {
        return getQuestionResponsesSection(questionNum).findElement(By.cssSelector("#numscale-statistics tbody tr"));
    }

    private WebElement getRubricExcludeSelfCheckbox(int questionNum) {
        return getQuestionResponsesSection(questionNum).findElement(By.id("exclude-self-checkbox"));
    }

    private WebElement getRubricStatistics(int questionNum) {
        return getQuestionResponsesSection(questionNum).findElement(By.id("rubric-statistics"));
    }

    private WebElement getRubricPerRecipientStatsPerCriterion(int questionNum) {
        return getQuestionResponsesSection(questionNum).findElement(By.id("rubric-recipient-statistics-per-criterion"));
    }

    private void sortRubricPerRecipientStatsPerCriterion(int questionNum, int colNum) {
        click(getRubricPerRecipientStatsPerCriterion(questionNum).findElements(By.tagName("th")).get(colNum - 1));
    }

    private WebElement getRubricPerRecipientStatsOverall(int questionNum) {
        return getQuestionResponsesSection(questionNum).findElement(By.id("rubric-recipient-statistics-overall"));
    }

    private void sortRubricPerRecipientStatsOverall(int questionNum, int colNum) {
        click(getRubricPerRecipientStatsOverall(questionNum).findElements(By.tagName("th")).get(colNum - 1));
    }

    private boolean isCommentByResponseGiver(WebElement commentField) {
        return commentField.findElements(By.id("by-response-giver")).size() > 0;
    }

    private String getCommentGiver(WebElement commentField) {
        String commentGiverDescription = commentField.findElement(By.id("comment-giver-name")).getText();
        return commentGiverDescription.split(" commented")[0];
    }

    private String getCommentEditor(WebElement commentField) {
        String editDescription = commentField.findElement(By.id("last-editor-name")).getText();
        return editDescription.split("edited by ")[1];
    }

    private List<WebElement> getCommentFields(int questionNum) {
        return getQuestionResponsesSection(questionNum).findElements(By.tagName("tm-comment-row"));
    }

    private WebElement getCommentField(int questionNum, String commentString) {
        List<WebElement> commentFields = getCommentFields(questionNum);
        for (WebElement comment : commentFields) {
            if (comment.findElement(By.id("comment-text")).getText().equals(commentString)) {
                return comment;
            }
        }
        throw new AssertionError("Comment field not found");
    }

    private int getGiverIndex(WebElement response, String giver) {
        List<WebElement> givers = response.findElements(By.id("response-giver"));
        for (int i = 0; i < givers.size(); i++) {
            if (givers.get(i).getText().contains(giver)) {
                return i;
            }
        }
        throw new AssertionError("Giver not found: " + giver);
    }

    private int getRecipientIndex(int questionNum, String recipient) {
        List<WebElement> recipients = getQuestionResponsesSection(questionNum).findElements(By.id("response-recipient"));
        for (int i = 0; i < recipients.size(); i++) {
            if (recipients.get(i).getText().split("To: ")[1].equals(recipient)) {
                return i;
            }
        }
        throw new AssertionError("Recipient not found: " + recipient);
    }
}
