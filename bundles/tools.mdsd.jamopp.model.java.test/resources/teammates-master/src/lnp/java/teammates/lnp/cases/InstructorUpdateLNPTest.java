package teammates.lnp.cases;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.jmeter.protocol.http.control.HeaderManager;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.ListedHashTree;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import teammates.common.datatransfer.DataBundle;
import teammates.common.datatransfer.FeedbackParticipantType;
import teammates.common.datatransfer.InstructorPrivileges;
import teammates.common.datatransfer.attributes.CourseAttributes;
import teammates.common.datatransfer.attributes.FeedbackQuestionAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseAttributes;
import teammates.common.datatransfer.attributes.FeedbackResponseCommentAttributes;
import teammates.common.datatransfer.attributes.FeedbackSessionAttributes;
import teammates.common.datatransfer.attributes.InstructorAttributes;
import teammates.common.datatransfer.attributes.StudentAttributes;
import teammates.common.datatransfer.questions.FeedbackQuestionDetails;
import teammates.common.datatransfer.questions.FeedbackTextQuestionDetails;
import teammates.common.datatransfer.questions.FeedbackTextResponseDetails;
import teammates.common.exception.HttpRequestFailedException;
import teammates.common.util.Const;
import teammates.common.util.JsonUtils;
import teammates.lnp.util.JMeterElements;
import teammates.lnp.util.LNPSpecification;
import teammates.lnp.util.LNPTestData;
import teammates.ui.request.InstructorCreateRequest;

/**
* L&P Test Case for instructor update cascade API.
*/
public class InstructorUpdateLNPTest extends BaseLNPTestCase {
    private static final int NUM_INSTRUCTORS = 1;
    private static final int RAMP_UP_PERIOD = NUM_INSTRUCTORS * 2;

    private static final int NUMBER_OF_FEEDBACK_RESPONSE_COMMENTS = 100;

    private static final String COURSE_ID = "TestData.CS101";
    private static final String COURSE_NAME = "LnPCourse";
    private static final String COURSE_TIME_ZONE = "UTC";

    private static final String INSTRUCTOR_ID = "LnPInstructor_id";
    private static final String INSTRUCTOR_NAME = "LnPInstructor";
    private static final String INSTRUCTOR_EMAIL = "tmms.test@gmail.tmt";

    private static final String UPDATE_INSTRUCTOR_EMAIL = "update.test@gmail.tmt";

    private static final String STUDENT_ID = "LnPStudent.tmms";
    private static final String STUDENT_NAME = "LnPStudent";
    private static final String STUDENT_EMAIL = "studentEmail@gmail.tmt";

    private static final String TEAM_NAME = "Team 1";
    private static final String GIVER_SECTION_NAME = "Section 1";
    private static final String RECEIVER_SECTION_NAME = "Section 1";

    private static final String FEEDBACK_SESSION_NAME = "Test Feedback Session";

    private static final String FEEDBACK_RESPONSE_ID = "ResponseForQ";

    private static final String FEEDBACK_RESPONSE_COMMENT_ID = "TestComment";

    private static final String FEEDBACK_QUESTION_ID = "QuestionTest";
    private static final String FEEDBACK_QUESTION_TEXT = "Test Question";

    private static final double ERROR_RATE_LIMIT = 0.01;
    private static final double MEAN_RESP_TIME_LIMIT = 10;

    @Override
    protected LNPTestData getTestData() {
        return new LNPTestData() {
            @Override
            protected Map<String, CourseAttributes> generateCourses() {
                Map<String, CourseAttributes> courses = new HashMap<>();

                courses.put(COURSE_NAME, CourseAttributes.builder(COURSE_ID)
                        .withName(COURSE_NAME)
                        .withTimezone(ZoneId.of(COURSE_TIME_ZONE))
                        .build());

                return courses;
            }

            @Override
            protected Map<String, InstructorAttributes> generateInstructors() {
                Map<String, InstructorAttributes> instructors = new HashMap<>();

                instructors.put(INSTRUCTOR_NAME,
                        InstructorAttributes.builder(COURSE_ID, INSTRUCTOR_EMAIL)
                            .withGoogleId(INSTRUCTOR_ID)
                            .withName(INSTRUCTOR_NAME)
                            .withRole("Co-owner")
                            .withIsDisplayedToStudents(true)
                            .withDisplayedName("Co-owner")
                            .withPrivileges(new InstructorPrivileges(
                                    Const.InstructorPermissionRoleNames.INSTRUCTOR_PERMISSION_ROLE_COOWNER))
                            .build()
                );

                return instructors;
            }

            @Override
            protected Map<String, StudentAttributes> generateStudents() {
                Map<String, StudentAttributes> students = new LinkedHashMap<>();
                StudentAttributes studentAttribute;

                studentAttribute = StudentAttributes.builder(COURSE_ID, STUDENT_EMAIL)
                        .withGoogleId(STUDENT_ID)
                        .withName(STUDENT_NAME)
                        .withComment("This student's name is " + STUDENT_NAME)
                        .withSectionName(GIVER_SECTION_NAME)
                        .withTeamName(TEAM_NAME)
                        .build();

                students.put(STUDENT_NAME, studentAttribute);

                return students;
            }

            @Override
            protected Map<String, FeedbackSessionAttributes> generateFeedbackSessions() {
                Map<String, FeedbackSessionAttributes> feedbackSessions = new LinkedHashMap<>();

                FeedbackSessionAttributes session = FeedbackSessionAttributes
                        .builder(FEEDBACK_SESSION_NAME, COURSE_ID)
                        .withCreatorEmail(INSTRUCTOR_EMAIL)
                        .withStartTime(Instant.now().plusMillis(100))
                        .withEndTime(Instant.now().plusSeconds(500))
                        .withSessionVisibleFromTime(Instant.now())
                        .withResultsVisibleFromTime(Instant.now())
                        .build();

                feedbackSessions.put(FEEDBACK_SESSION_NAME, session);

                return feedbackSessions;
            }

            @Override
            protected Map<String, FeedbackQuestionAttributes> generateFeedbackQuestions() {
                List<FeedbackParticipantType> showResponses = new ArrayList<>();
                showResponses.add(FeedbackParticipantType.RECEIVER);
                showResponses.add(FeedbackParticipantType.INSTRUCTORS);

                List<FeedbackParticipantType> showGiverName = new ArrayList<>();
                showGiverName.add(FeedbackParticipantType.INSTRUCTORS);

                List<FeedbackParticipantType> showRecepientName = new ArrayList<>();
                showRecepientName.add(FeedbackParticipantType.INSTRUCTORS);

                Map<String, FeedbackQuestionAttributes> feedbackQuestions = new LinkedHashMap<>();
                FeedbackQuestionDetails details = new FeedbackTextQuestionDetails(FEEDBACK_QUESTION_TEXT);

                feedbackQuestions.put(FEEDBACK_QUESTION_ID,
                        FeedbackQuestionAttributes.builder()
                                .withFeedbackSessionName(FEEDBACK_SESSION_NAME)
                                .withQuestionDescription(FEEDBACK_QUESTION_TEXT)
                                .withCourseId(COURSE_ID)
                                .withQuestionDetails(details)
                                .withQuestionNumber(1)
                                .withGiverType(FeedbackParticipantType.INSTRUCTORS)
                                .withRecipientType(FeedbackParticipantType.STUDENTS)
                                .withShowResponsesTo(showResponses)
                                .withShowGiverNameTo(showGiverName)
                                .withShowRecipientNameTo(showRecepientName)
                                .build()
                );

                return feedbackQuestions;
            }

            @Override
            protected Map<String, FeedbackResponseAttributes> generateFeedbackResponses() {
                Map<String, FeedbackResponseAttributes> feedbackResponses = new HashMap<>();
                FeedbackTextResponseDetails details =
                        new FeedbackTextResponseDetails(FEEDBACK_RESPONSE_ID);

                feedbackResponses.put(FEEDBACK_RESPONSE_ID,
                        FeedbackResponseAttributes.builder("1",
                            INSTRUCTOR_EMAIL,
                            STUDENT_EMAIL)
                            .withCourseId(COURSE_ID)
                            .withFeedbackSessionName(FEEDBACK_SESSION_NAME)
                            .withGiverSection(GIVER_SECTION_NAME)
                            .withRecipientSection(RECEIVER_SECTION_NAME)
                            .withResponseDetails(details)
                            .build());

                return feedbackResponses;
            }

            @Override
            protected Map<String, FeedbackResponseCommentAttributes> generateFeedbackResponseComments() {
                List<FeedbackParticipantType> showComments = new ArrayList<>();
                showComments.add(FeedbackParticipantType.RECEIVER);
                showComments.add(FeedbackParticipantType.INSTRUCTORS);

                List<FeedbackParticipantType> showGiverName = new ArrayList<>();
                showGiverName.add(FeedbackParticipantType.INSTRUCTORS);

                Map<String, FeedbackResponseCommentAttributes> feedbackResponseComments = new HashMap<>();

                for (int i = 1; i <= NUMBER_OF_FEEDBACK_RESPONSE_COMMENTS; i++) {
                    String responseCommentText = FEEDBACK_RESPONSE_COMMENT_ID + i;
                    feedbackResponseComments.put(responseCommentText,
                            FeedbackResponseCommentAttributes.builder()
                                .withCourseId(COURSE_ID)
                                .withFeedbackResponseId(FEEDBACK_RESPONSE_ID)
                                .withFeedbackQuestionId(FEEDBACK_QUESTION_ID)
                                .withFeedbackSessionName(FEEDBACK_SESSION_NAME)
                                .withCommentText(responseCommentText)
                                .withCommentGiver(INSTRUCTOR_EMAIL)
                                .withCommentGiverType(FeedbackParticipantType.INSTRUCTORS)
                                .withCommentFromFeedbackParticipant(true)
                                .withVisibilityFollowingFeedbackQuestion(true)
                                .withShowCommentTo(showComments)
                                .withShowGiverNameTo(showGiverName)
                                .withGiverSection(GIVER_SECTION_NAME)
                                .withReceiverSection(RECEIVER_SECTION_NAME)
                                .build());
                }

                return feedbackResponseComments;
            }

            @Override
            public List<String> generateCsvHeaders() {
                List<String> headers = new ArrayList<>();

                headers.add("loginId");
                headers.add("courseId");
                headers.add("updateData");

                return headers;
            }

            @Override
            public List<List<String>> generateCsvData() {
                DataBundle dataBundle = loadDataBundle(getJsonDataPath());
                List<List<String>> csvData = new ArrayList<>();

                dataBundle.instructors.forEach((key, instructor) -> {
                    List<String> csvRow = new ArrayList<>();

                    csvRow.add(INSTRUCTOR_ID);
                    csvRow.add(COURSE_ID);

                    InstructorCreateRequest instructorCreateRequest =
                            new InstructorCreateRequest(
                                INSTRUCTOR_ID,
                                INSTRUCTOR_NAME,
                                UPDATE_INSTRUCTOR_EMAIL,
                                "Co-owner",
                                "Co-owner", true);

                    String updateData = sanitizeForCsv(JsonUtils.toJson(instructorCreateRequest));
                    csvRow.add(updateData);

                    csvData.add(csvRow);
                });

                return csvData;
            }
        };
    }

    private Map<String, String> getRequestHeaders() {
        Map<String, String> headers = new HashMap<>();

        headers.put(Const.HeaderNames.CSRF_TOKEN, "${csrfToken}");
        headers.put("Content-Type", "application/json");

        return headers;
    }

    private String getTestEndpoint() {
        return Const.ResourceURIs.INSTRUCTOR + "?courseid=${courseId}";
    }

    @Override
    protected ListedHashTree getLnpTestPlan() {
        ListedHashTree testPlan = new ListedHashTree(JMeterElements.testPlan());
        HashTree threadGroup = testPlan.add(
                JMeterElements.threadGroup(NUM_INSTRUCTORS, RAMP_UP_PERIOD, 1));

        threadGroup.add(JMeterElements.csvDataSet(getPathToTestDataFile(getCsvConfigPath())));
        threadGroup.add(JMeterElements.cookieManager());
        threadGroup.add(JMeterElements.defaultSampler());

        threadGroup.add(JMeterElements.onceOnlyController())
                .add(JMeterElements.loginSampler())
                .add(JMeterElements.csrfExtractor("csrfToken"));

        // Add HTTP sampler for test endpoint
        HeaderManager headerManager = JMeterElements.headerManager(getRequestHeaders());
        threadGroup.add(JMeterElements.httpSampler(getTestEndpoint(), PUT, "${updateData}"))
                .add(headerManager);

        return testPlan;
    }

    @Override
    protected void setupSpecification() {
        this.specification = LNPSpecification.builder()
                .withErrorRateLimit(ERROR_RATE_LIMIT)
                .withMeanRespTimeLimit(MEAN_RESP_TIME_LIMIT)
                .build();
    }

    @BeforeClass
    public void classSetup() throws IOException, HttpRequestFailedException {
        generateTimeStamp();
        createTestData();
        setupSpecification();
    }

    @Test
    public void runLnpTest() throws IOException {
        runJmeter(false);
        displayLnpResults();
    }

    @AfterClass
    public void classTearDown() throws IOException {
        deleteTestData();
        deleteDataFiles();
        cleanupResults();
    }
}
