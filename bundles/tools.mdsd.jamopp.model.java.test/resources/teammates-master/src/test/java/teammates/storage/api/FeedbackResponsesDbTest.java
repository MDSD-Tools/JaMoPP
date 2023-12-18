package teammates.storage.api;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Sets;

import teammates.common.datatransfer.AttributesDeletionQuery;
import teammates.common.datatransfer.DataBundle;
import teammates.common.datatransfer.attributes.FeedbackQuestionAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseAttributes;
import teammates.common.datatransfer.questions.FeedbackResponseDetails;
import teammates.common.datatransfer.questions.FeedbackTextResponseDetails;
import teammates.common.exception.EntityAlreadyExistsException;
import teammates.common.exception.EntityDoesNotExistException;
import teammates.common.exception.InvalidParametersException;
import teammates.common.util.FieldValidator;
import teammates.common.util.JsonUtils;
import teammates.test.AssertHelper;
import teammates.test.BaseTestCaseWithLocalDatabaseAccess;
import teammates.test.ThreadHelper;

/**
 * SUT: {@link FeedbackResponsesDb}.
 */
public class FeedbackResponsesDbTest extends BaseTestCaseWithLocalDatabaseAccess {

    private final FeedbackResponsesDb frDb = FeedbackResponsesDb.inst();
    private final FeedbackQuestionsDb fqDb = FeedbackQuestionsDb.inst();
    private DataBundle dataBundle;
    private Map<String, FeedbackResponseAttributes> fras;

    @BeforeClass
    public void beforeClass() throws Exception {
        dataBundle = getTypicalDataBundle();
        // Add questions to DB
        Set<String> keys = dataBundle.feedbackQuestions.keySet();
        for (String i : keys) {
            fqDb.createEntity(dataBundle.feedbackQuestions.get(i));
        }
    }

    @BeforeMethod
    public void beforeMethod() throws Exception {
        dataBundle = getTypicalDataBundle();
        addQuestionsAndResponsesToDb();
        fras = dataBundle.feedbackResponses;
    }

    private void addQuestionsAndResponsesToDb() throws Exception {
        // Add responses for corresponding question to DB
        Set<String> keys = dataBundle.feedbackResponses.keySet();
        for (String i : keys) {
            FeedbackResponseAttributes fra = dataBundle.feedbackResponses.get(i);

            // Update feedbackQuestionId for response
            FeedbackQuestionAttributes fqa = fqDb.getFeedbackQuestion(fra.getFeedbackSessionName(),
                    fra.getCourseId(), Integer.parseInt(fra.getFeedbackQuestionId()));
            fra.setFeedbackQuestionId(fqa.getId());
            frDb.createEntity(fra);
        }
    }

    @Test
    public void testGetGiverSetThatAnswerFeedbackSession_emptyResponses_shouldReturnEmptySet() {
        Set<String> giverSet = frDb.getGiverSetThatAnswerFeedbackSession("courseA", "session");

        assertTrue(giverSet.isEmpty());
    }

    @Test
    public void testGetGiverSetThatAnswerFeedbackSession_giverIsUser_shouldReturnCorrectIdentifier() {
        Set<String> giverSet = frDb.getGiverSetThatAnswerFeedbackSession("idOfTypicalCourse1", "First feedback session");

        assertEquals(Sets.newHashSet("student1InCourse1@gmail.tmt", "student2InCourse1@gmail.tmt",
                "student5InCourse1@gmail.tmt", "student3InCourse1@gmail.tmt", "instructor1@course1.tmt"),
                giverSet);
    }

    @Test
    public void testTimestamp() throws Exception {

        ______TS("success : created");

        FeedbackResponseAttributes fra = getNewFeedbackResponseAttributes();

        // remove possibly conflicting entity from the database
        deleteResponse(fra);

        frDb.createEntity(fra);
        verifyPresentInDatabase(fra);

        String feedbackQuestionId = fra.getFeedbackQuestionId();
        String giverEmail = fra.getGiver();
        String recipientEmail = fra.getRecipient();

        FeedbackResponseAttributes feedbackResponse =
                frDb.getFeedbackResponse(feedbackQuestionId, giverEmail, recipientEmail);

        // Assert dates are now.
        AssertHelper.assertInstantIsNow(feedbackResponse.getCreatedAt());
        AssertHelper.assertInstantIsNow(feedbackResponse.getUpdatedAt());

        ______TS("success : update lastUpdated");

        // wait for very briefly so that the update timestamp is guaranteed to change
        ThreadHelper.waitFor(5);

        String newRecipientEmail = "new-email@tmt.com";
        feedbackResponse.setRecipient(newRecipientEmail);
        frDb.updateFeedbackResponse(
                FeedbackResponseAttributes.updateOptionsBuilder(feedbackResponse.getId())
                        .withRecipient(newRecipientEmail)
                        .build());

        FeedbackResponseAttributes updatedFr = frDb.getFeedbackResponse(feedbackQuestionId, giverEmail, newRecipientEmail);

        // Assert lastUpdate has changed, and is now.
        assertFalse(feedbackResponse.getUpdatedAt().equals(updatedFr.getUpdatedAt()));
        AssertHelper.assertInstantIsNow(updatedFr.getUpdatedAt());

    }

    @Test
    public void testDeleteFeedbackResponse() {
        ______TS("non-existent id");

        frDb.deleteFeedbackResponse("not-existent");

        ______TS("standard success case");

        FeedbackResponseAttributes fra = fras.get("response1ForQ1S1C1");
        fra = frDb.getFeedbackResponse(fra.getFeedbackQuestionId(), fra.getGiver(), fra.getRecipient());
        assertNotNull(fra);

        frDb.deleteFeedbackResponse(fra.getId());

        assertNull(frDb.getFeedbackResponse(fra.getId()));
    }

    @Test
    public void testDeleteFeedbackResponses_byQuestionId() {
        ______TS("standard success case");

        FeedbackResponseAttributes fra = fras.get("response1ForQ1S1C1");
        assertFalse(frDb.getFeedbackResponsesForQuestion(fra.getFeedbackQuestionId()).isEmpty());
        FeedbackResponseAttributes fraFromAnotherQuestion = fras.get("response1ForQ2S1C1");
        assertFalse(frDb.getFeedbackResponsesForQuestion(fraFromAnotherQuestion.getFeedbackQuestionId()).isEmpty());
        assertNotEquals(fra.getFeedbackQuestionId(), fraFromAnotherQuestion.getFeedbackQuestionId());

        frDb.deleteFeedbackResponses(
                AttributesDeletionQuery.builder()
                        .withQuestionId(fra.getFeedbackQuestionId())
                        .build());

        // all response of questions are deleted
        assertTrue(frDb.getFeedbackResponsesForQuestion(fra.getFeedbackQuestionId()).isEmpty());
        // responses of other questions remain
        assertFalse(frDb.getFeedbackResponsesForQuestion(fraFromAnotherQuestion.getFeedbackQuestionId()).isEmpty());

        ______TS("non-existent question id");

        // should pass silently
        frDb.deleteFeedbackResponses(
                AttributesDeletionQuery.builder()
                        .withQuestionId("not-exist")
                        .build());

        // responses are not deleted accidentally
        assertFalse(frDb.getFeedbackResponsesForQuestion(fraFromAnotherQuestion.getFeedbackQuestionId()).isEmpty());
    }

    @Test
    public void testDeleteFeedbackResponses_byCourseIdAndSessionName() {
        ______TS("standard success case");

        FeedbackResponseAttributes fra = fras.get("response1ForQ1S1C1");
        fra = frDb.getFeedbackResponse(fra.getFeedbackQuestionId(), fra.getGiver(), fra.getRecipient());
        assertNotNull(fra);
        FeedbackResponseAttributes fraFromAnotherSession = fras.get("response1ForQ1S2C1");
        fraFromAnotherSession = frDb.getFeedbackResponse(
                fraFromAnotherSession.getFeedbackQuestionId(), fraFromAnotherSession.getGiver(),
                fraFromAnotherSession.getRecipient());
        assertNotNull(fraFromAnotherSession);
        // response are belong to the same course
        assertEquals(fra.getCourseId(), fraFromAnotherSession.getCourseId());
        // but in different session
        assertNotEquals(fra.getFeedbackSessionName(), fraFromAnotherSession.getFeedbackSessionName());

        frDb.deleteFeedbackResponses(
                AttributesDeletionQuery.builder()
                        .withCourseId(fra.getCourseId())
                        .withFeedbackSessionName(fra.getFeedbackSessionName())
                        .build());

        assertNull(frDb.getFeedbackResponse(fra.getId()));
        // other responses remains
        assertNotNull(frDb.getFeedbackResponse(fraFromAnotherSession.getId()));

        ______TS("non-existent course id");

        // should pass silently
        frDb.deleteFeedbackResponses(
                AttributesDeletionQuery.builder()
                        .withCourseId("not_exist")
                        .withFeedbackSessionName(fra.getFeedbackSessionName())
                        .build());

        // other responses remain
        assertNotNull(frDb.getFeedbackResponse(fraFromAnotherSession.getId()));

        ______TS("non-existent session name");

        // should pass silently
        frDb.deleteFeedbackResponses(
                AttributesDeletionQuery.builder()
                        .withCourseId(fra.getCourseId())
                        .withFeedbackSessionName("not-exist")
                        .build());

        // other responses remain
        assertNotNull(frDb.getFeedbackResponse(fraFromAnotherSession.getId()));

        ______TS("non-existent course and session name");

        // should pass silently
        frDb.deleteFeedbackResponses(
                AttributesDeletionQuery.builder()
                        .withCourseId("not-exist")
                        .withFeedbackSessionName("not-exist")
                        .build());

        // other responses remain
        assertNotNull(frDb.getFeedbackResponse(fraFromAnotherSession.getId()));
    }

    @Test
    public void testDeleteFeedbackResponses_byCourseId() {
        ______TS("standard success case");

        FeedbackResponseAttributes fra = fras.get("response1ForQ1S1C1");
        fra = frDb.getFeedbackResponse(fra.getFeedbackQuestionId(), fra.getGiver(), fra.getRecipient());
        assertNotNull(fra);
        FeedbackResponseAttributes fraFromAnotherCourse = fras.get("response1ForQ1S1C2");
        fraFromAnotherCourse = frDb.getFeedbackResponse(
                fraFromAnotherCourse.getFeedbackQuestionId(), fraFromAnotherCourse.getGiver(),
                fraFromAnotherCourse.getRecipient());
        assertNotNull(fraFromAnotherCourse);
        // response are belong to different courses
        assertNotEquals(fra.getCourseId(), fraFromAnotherCourse.getCourseId());

        frDb.deleteFeedbackResponses(
                AttributesDeletionQuery.builder()
                        .withCourseId(fra.getCourseId())
                        .build());

        // all response of courses are deleted
        assertNull(frDb.getFeedbackResponse(fra.getId()));
        // responses of other course remain
        assertNotNull(frDb.getFeedbackResponse(fraFromAnotherCourse.getId()));

        ______TS("non-existent course id");

        // should pass silently
        frDb.deleteFeedbackResponses(
                AttributesDeletionQuery.builder()
                        .withCourseId("not-exist")
                        .build());

        // responses are not deleted accidentally
        assertNotNull(frDb.getFeedbackResponse(fraFromAnotherCourse.getId()));
    }

    @Test
    public void testCreateFeedbackResponse() throws Exception {

        ______TS("standard success case");

        FeedbackResponseAttributes fra = getNewFeedbackResponseAttributes();

        // remove possibly conflicting entity from the database
        deleteResponse(fra);

        frDb.createEntity(fra);

        // sets the id for fra
        verifyPresentInDatabase(fra);

        ______TS("duplicate - with same id.");

        EntityAlreadyExistsException eaee = assertThrows(EntityAlreadyExistsException.class, () -> frDb.createEntity(fra));
        assertEquals(
                String.format(FeedbackResponsesDb.ERROR_CREATE_ENTITY_ALREADY_EXISTS, fra.toString()), eaee.getMessage());

        ______TS("delete - with id specified");

        deleteResponse(fra);
        verifyAbsentInDatabase(fra);

        ______TS("null params");

        assertThrows(AssertionError.class, () -> frDb.createEntity(null));

        ______TS("invalid params");

        fra.setCourseId("invalid course id!");
        InvalidParametersException ipe = assertThrows(InvalidParametersException.class, () -> frDb.createEntity(fra));
        AssertHelper.assertContains(
                getPopulatedErrorMessage(
                        FieldValidator.COURSE_ID_ERROR_MESSAGE, "invalid course id!",
                        FieldValidator.COURSE_ID_FIELD_NAME, FieldValidator.REASON_INCORRECT_FORMAT,
                        FieldValidator.COURSE_ID_MAX_LENGTH),
                ipe.getLocalizedMessage());

    }

    @Test
    public void testGetFeedbackResponses() {

        ______TS("standard success case");

        FeedbackResponseAttributes expected = getResponseAttributes("response1ForQ1S1C1");

        FeedbackResponseAttributes actual =
                frDb.getFeedbackResponse(expected.getFeedbackQuestionId(), expected.getGiver(), expected.getRecipient());

        assertEquals(expected.toString(), actual.toString());

        ______TS("non-existent response");

        assertNull(frDb.getFeedbackResponse(expected.getFeedbackQuestionId(), "student1InCourse1@gmail.tmt",
                                            "student3InCourse1@gmail.tmt"));

        ______TS("null fqId");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponse(null, "student1InCourse1@gmail.tmt", "student1InCourse1@gmail.tmt"));

        ______TS("null giverEmail");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponse(expected.getFeedbackQuestionId(), null, "student1InCourse1@gmail.tmt"));

        ______TS("null receiverEmail");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponse(expected.getFeedbackQuestionId(), "student1InCourse1@gmail.tmt", null));

        ______TS("get by id");

        actual = frDb.getFeedbackResponse(actual.getId()); //Id from first success case

        assertEquals(expected.toString(), actual.toString());

        ______TS("get non-existent response by id");

        actual = frDb.getFeedbackResponse("non-existent id");

        assertNull(actual);
    }

    @Test
    public void testGetFeedbackResponsesForQuestion() {

        ______TS("standard success case");

        List<FeedbackResponseAttributes> responses =
                frDb.getFeedbackResponsesForQuestion(fras.get("response1ForQ1S1C1").getFeedbackQuestionId());
        assertEquals(2, responses.size());

        ______TS("null params");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForQuestion(null));

        ______TS("non-existent feedback question");

        assertTrue(frDb.getFeedbackResponsesForQuestion("non-existent fq id").isEmpty());
    }

    @Test
    public void testGetFeedbackResponsesForQuestionInSection() {

        ______TS("standard success case");

        String questionId = fras.get("response1ForQ2S1C1").getFeedbackQuestionId();

        List<FeedbackResponseAttributes> responses = frDb.getFeedbackResponsesForQuestionInSection(questionId, "Section 1");
        assertEquals(3, responses.size());

        ______TS("null params");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForQuestionInSection(null, "Section 1"));

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForQuestionInSection(questionId, null));

        ______TS("non-existent feedback question");

        assertTrue(frDb.getFeedbackResponsesForQuestionInSection("non-existent fq id", "Section 1")
                .isEmpty());
    }

    @Test
    public void testGetFeedbackResponsesForSession() {

        ______TS("standard success case");

        String feedbackSessionName = fras.get("response1ForQ1S1C1").getFeedbackSessionName();
        String courseId = fras.get("response1ForQ1S1C1").getCourseId();

        List<FeedbackResponseAttributes> responses = frDb.getFeedbackResponsesForSession(feedbackSessionName, courseId);

        assertEquals(6, responses.size());

        ______TS("null params");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForSession(null, courseId));

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForSession(feedbackSessionName, null));

        ______TS("non-existent feedback session");

        assertTrue(frDb.getFeedbackResponsesForSession("non-existent feedback session", courseId).isEmpty());

        ______TS("non-existent course");

        assertTrue(frDb.getFeedbackResponsesForSession(feedbackSessionName, "non-existent courseId").isEmpty());

    }

    @Test
    public void testGetFeedbackResponsesForReceiverForCourse() {

        ______TS("standard success case");

        String courseId = fras.get("response1ForQ1S1C1").getCourseId();

        List<FeedbackResponseAttributes> responses =
                frDb.getFeedbackResponsesForReceiverForCourse(courseId,
                        "student1InCourse1@gmail.tmt");

        assertEquals(1, responses.size());

        ______TS("null params");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForReceiverForCourse(null, "student1InCourse1@gmail.tmt"));

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForReceiverForCourse(courseId, null));

        ______TS("non-existent courseId");

        assertTrue(frDb.getFeedbackResponsesForReceiverForCourse(
                "non-existent courseId", "student1InCourse1@gmail.tmt").isEmpty());

        ______TS("non-existent receiver");

        assertTrue(frDb.getFeedbackResponsesForReceiverForCourse(
                courseId, "non-existentStudentInCourse1@gmail.tmt").isEmpty());
    }

    @Test
    public void testGetFeedbackResponsesFromGiverForQuestion() {

        ______TS("standard success case");

        String questionId = fras.get("response1ForQ1S1C1").getFeedbackQuestionId();

        List<FeedbackResponseAttributes> responses =
                frDb.getFeedbackResponsesFromGiverForQuestion(questionId,
                        "student1InCourse1@gmail.tmt");

        assertEquals(responses.size(), 1);

        ______TS("null params");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesFromGiverForQuestion(null, "student1InCourse1@gmail.tmt"));

        assertThrows(AssertionError.class, () -> frDb.getFeedbackResponsesFromGiverForQuestion(questionId, null));

        ______TS("non-existent feedback question");

        assertTrue(frDb.getFeedbackResponsesFromGiverForQuestion(
                "non-existent fq id", "student1InCourse1@gmail.tmt").isEmpty());

        ______TS("non-existent receiver");

        assertTrue(frDb.getFeedbackResponsesFromGiverForQuestion(
                questionId, "non-existentStudentInCourse1@gmail.tmt").isEmpty());
    }

    @Test
    public void testGetFeedbackResponsesForReceiverForQuestion() {

        ______TS("standard success case");

        String questionId = fras.get("response1ForQ1S1C1").getFeedbackQuestionId();

        List<FeedbackResponseAttributes> responses =
                frDb.getFeedbackResponsesForReceiverForQuestion(questionId,
                        "student1InCourse1@gmail.tmt");

        assertEquals(1, responses.size());

        ______TS("null params");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForReceiverForQuestion(null, "student1InCourse1@gmail.tmt"));

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForReceiverForQuestion(questionId, null));

        ______TS("non-existent feedback question");

        assertTrue(frDb.getFeedbackResponsesForReceiverForQuestion(
                "non-existent fq id", "student1InCourse1@gmail.tmt").isEmpty());

        ______TS("non-existent receiver");

        assertTrue(frDb.getFeedbackResponsesForReceiverForQuestion(
                questionId, "non-existentStudentInCourse1@gmail.tmt").isEmpty());
    }

    @Test
    public void testGetFeedbackResponsesFromGiverForCourse() {

        ______TS("standard success case");

        String courseId = fras.get("response1ForQ1S1C1").getCourseId();

        List<FeedbackResponseAttributes> responses =
                frDb.getFeedbackResponsesFromGiverForCourse(courseId,
                        "student1InCourse1@gmail.tmt");

        assertEquals(2, responses.size());

        ______TS("null params");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesFromGiverForCourse(null, "student1InCourse1@gmail.tmt"));

        assertThrows(AssertionError.class, () -> frDb.getFeedbackResponsesFromGiverForCourse(courseId, null));

        ______TS("non-existent feedback question");

        assertTrue(frDb.getFeedbackResponsesFromGiverForCourse(
                "non-existent courseId", "student1InCourse1@gmail.tmt").isEmpty());

        ______TS("non-existent giver");

        assertTrue(frDb.getFeedbackResponsesFromGiverForCourse(
                courseId, "non-existentStudentInCourse1@gmail.tmt").isEmpty());
    }

    @Test
    public void testGetFeedbackResponsesForSessionInSection() {

        ______TS("standard success case");

        String courseId = fras.get("response1ForQ1S1C1").getCourseId();
        String feedbackSessionName = fras.get("response1ForQ1S1C1").getFeedbackSessionName();

        List<FeedbackResponseAttributes> responses =
                frDb.getFeedbackResponsesForSessionInSection(feedbackSessionName, courseId, "Section 1");

        assertEquals(5, responses.size());

        ______TS("null params");

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForSessionInSection(null, courseId, "Section 1"));

        assertThrows(AssertionError.class,
                () -> frDb.getFeedbackResponsesForSessionInSection(feedbackSessionName, null, "Section 1"));

        ______TS("non-existent feedback session");

        assertTrue(frDb.getFeedbackResponsesForSessionInSection(
                "non-existent feedback session", courseId, "Section 1").isEmpty());

        ______TS("non-existent course");

        assertTrue(frDb.getFeedbackResponsesForSessionInSection(
                feedbackSessionName, "non-existent courseId", "Section 1").isEmpty());
    }

    @Test
    public void testUpdateFeedbackResponse_noChangeToResponse_shouldNotIssueSaveRequest() throws Exception {
        FeedbackResponseAttributes typicalResponse = getResponseAttributes("response3ForQ2S1C1");

        typicalResponse = frDb.getFeedbackResponse(typicalResponse.getFeedbackQuestionId(),
                typicalResponse.getGiver(), typicalResponse.getRecipient());

        FeedbackResponseAttributes updatedResponse = frDb.updateFeedbackResponse(
                FeedbackResponseAttributes.updateOptionsBuilder(typicalResponse.getId())
                        .build());

        assertEquals(JsonUtils.toJson(typicalResponse), JsonUtils.toJson(updatedResponse));
        assertEquals(typicalResponse.getUpdatedAt(), updatedResponse.getUpdatedAt());

        updatedResponse = frDb.updateFeedbackResponse(
                FeedbackResponseAttributes.updateOptionsBuilder(typicalResponse.getId())
                        .withGiver(typicalResponse.getGiver())
                        .withGiverSection(typicalResponse.getGiverSection())
                        .withRecipient(typicalResponse.getRecipient())
                        .withRecipientSection(typicalResponse.getRecipientSection())
                        .withResponseDetails(typicalResponse.getResponseDetailsCopy())
                        .build());

        assertEquals(JsonUtils.toJson(typicalResponse), JsonUtils.toJson(updatedResponse));
        assertEquals(typicalResponse.getUpdatedAt(), updatedResponse.getUpdatedAt());
    }

    @Test
    public void testUpdateFeedbackResponse() throws Exception {

        ______TS("null params");

        assertThrows(AssertionError.class, () -> frDb.updateFeedbackResponse(null));

        ______TS("feedback response does not exist");

        EntityDoesNotExistException ednee = assertThrows(EntityDoesNotExistException.class,
                () -> frDb.updateFeedbackResponse(
                        FeedbackResponseAttributes.updateOptionsBuilder("non-existent")
                                .withGiver("giverIdentifier")
                                .build()));
        AssertHelper.assertContains(FeedbackResponsesDb.ERROR_UPDATE_NON_EXISTENT, ednee.getLocalizedMessage());

        ______TS("standard success case");

        FeedbackResponseAttributes modifiedResponse = getResponseAttributes("response3ForQ2S1C1");

        modifiedResponse = frDb.getFeedbackResponse(modifiedResponse.getFeedbackQuestionId(),
                modifiedResponse.getGiver(), modifiedResponse.getRecipient());

        FeedbackResponseDetails frd = new FeedbackTextResponseDetails("New answer text!");
        modifiedResponse.setResponseDetails(frd);

        frDb.updateFeedbackResponse(
                FeedbackResponseAttributes.updateOptionsBuilder(modifiedResponse.getId())
                        .withResponseDetails(frd)
                        .build());

        verifyPresentInDatabase(modifiedResponse);
        modifiedResponse = frDb.getFeedbackResponse(modifiedResponse.getFeedbackQuestionId(),
                modifiedResponse.getGiver(),
                modifiedResponse.getRecipient());
        assertEquals("New answer text!", modifiedResponse.getResponseDetailsCopy().getAnswerString());

        ______TS("standard success case, recreate response when recipient/giver change");

        FeedbackResponseAttributes updatedResponse = frDb.updateFeedbackResponse(
                FeedbackResponseAttributes.updateOptionsBuilder(modifiedResponse.getId())
                        .withGiver("giver@email.com")
                        .withRecipient("recipient@email.com")
                        .build());

        assertNull(frDb.getFeedbackResponse(modifiedResponse.getId()));
        FeedbackResponseAttributes actualResponse = frDb.getFeedbackResponse(updatedResponse.getId());
        assertNotNull(actualResponse);
        assertEquals("giver@email.com", updatedResponse.getGiver());
        assertEquals(updatedResponse.getGiver(), actualResponse.getGiver());
        assertEquals("recipient@email.com", updatedResponse.getRecipient());
        assertEquals(updatedResponse.getRecipient(), actualResponse.getRecipient());
        assertEquals(modifiedResponse.getCourseId(), updatedResponse.getCourseId());
        assertEquals(modifiedResponse.getFeedbackSessionName(), updatedResponse.getFeedbackSessionName());
        assertEquals(modifiedResponse.getFeedbackQuestionType(), updatedResponse.getFeedbackQuestionType());
    }

    // the test is to ensure that optimized saving policy is implemented without false negative
    @Test
    public void testUpdateFeedbackResponse_singleFieldUpdate_shouldUpdateCorrectly() throws Exception {
        FeedbackResponseAttributes typicalResponse = getResponseAttributes("response3ForQ2S1C1");
        typicalResponse = frDb.getFeedbackResponse(
                typicalResponse.getFeedbackQuestionId(), typicalResponse.getGiver(), typicalResponse.getRecipient());

        assertNotEquals("testSection", typicalResponse.getGiverSection());
        FeedbackResponseAttributes updatedResponse = frDb.updateFeedbackResponse(
                FeedbackResponseAttributes.updateOptionsBuilder(typicalResponse.getId())
                        .withGiverSection("testSection")
                        .build());
        FeedbackResponseAttributes actualResponse = frDb.getFeedbackResponse(typicalResponse.getId());
        assertEquals("testSection", updatedResponse.getGiverSection());
        assertEquals("testSection", actualResponse.getGiverSection());

        assertNotEquals("testSection", typicalResponse.getRecipientSection());
        updatedResponse = frDb.updateFeedbackResponse(
                FeedbackResponseAttributes.updateOptionsBuilder(typicalResponse.getId())
                        .withRecipientSection("testSection")
                        .build());
        actualResponse = frDb.getFeedbackResponse(typicalResponse.getId());
        assertEquals("testSection", updatedResponse.getRecipientSection());
        assertEquals("testSection", actualResponse.getRecipientSection());

        assertNotEquals("testResponse", typicalResponse.getResponseDetailsCopy().getAnswerString());
        updatedResponse = frDb.updateFeedbackResponse(
                FeedbackResponseAttributes.updateOptionsBuilder(typicalResponse.getId())
                        .withResponseDetails(new FeedbackTextResponseDetails("testResponse"))
                        .build());
        actualResponse = frDb.getFeedbackResponse(typicalResponse.getId());
        assertEquals("testResponse", updatedResponse.getResponseDetailsCopy().getAnswerString());
        assertEquals("testResponse", actualResponse.getResponseDetailsCopy().getAnswerString());

        frDb.deleteFeedbackResponse(typicalResponse.getId());
    }

    private FeedbackResponseAttributes getNewFeedbackResponseAttributes() {
        return FeedbackResponseAttributes.builder(
                "testFeedbackQuestionId", "giver@email.tmt", "recipient@email.tmt")
                .withCourseId("testCourse")
                .withFeedbackSessionName("fsTest1")
                .withGiverSection("None")
                .withRecipientSection("None")
                .withResponseDetails(new FeedbackTextResponseDetails("Text response"))
                .build();
    }

    private FeedbackResponseAttributes getResponseAttributes(String id) {
        FeedbackResponseAttributes result = fras.get(id);

        return FeedbackResponseAttributes.builder(result.getFeedbackQuestionId(), result.getGiver(), result.getRecipient())
                .withCourseId(result.getCourseId())
                .withFeedbackSessionName(result.getFeedbackSessionName())
                .withGiverSection(result.getGiverSection())
                .withRecipientSection(result.getRecipientSection())
                .withResponseDetails(result.getResponseDetails())
                .build();
    }

    @AfterMethod
    public void afterMethod() {
        deleteResponsesFromDb();
    }

    private void deleteResponsesFromDb() {
        Set<String> keys = dataBundle.feedbackResponses.keySet();
        for (String i : keys) {
            deleteResponse(dataBundle.feedbackResponses.get(i));
        }
    }

    private void deleteResponse(FeedbackResponseAttributes attributes) {
        FeedbackResponseAttributes feedbackResponse = frDb.getFeedbackResponse(
                attributes.getFeedbackQuestionId(), attributes.getGiver(), attributes.getRecipient());
        if (feedbackResponse != null) {
            frDb.deleteFeedbackResponse(feedbackResponse.getId());
        }
    }

}
