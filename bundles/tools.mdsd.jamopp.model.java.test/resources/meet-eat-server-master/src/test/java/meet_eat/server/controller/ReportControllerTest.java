package meet_eat.server.controller;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ReportControllerTest extends EntityControllerTest<ReportController, Report, String> {

    //#region @Test @RequestMapping

    @Test
    public void testPostReport() {
        // Test data
        Report report = getTestEntityTransient();
        Token token = getTokenPersistent(report.getSource());

        // Test frame
        createHandlePostEndpointTest(getEntityController()::postReport, report, token);
    }

    //#endregion

    //#region @Test handlePost

    @Override
    public void testHandlePostSingleEntity() {
        // Test data
        Report report = getTestEntityTransient();
        Token token = getTokenPersistent(report.getSource());

        // Execution
        ResponseEntity<Report> responseEntity = getEntityController().handlePost(report, token);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Override
    public void testHandlePostMultipleEntities() {
        // Test data
        User reporter = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(reporter);
        int entityAmount = 3;
        List<Report> reports = new LinkedList<>();
        repeat(entityAmount, i -> reports.add(i, new Report(reporter, getUserPersistent(Role.USER), "JUnit Report")));

        // Execution
        List<ResponseEntity<Report>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handlePost(reports.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.CREATED, responses.get(i).getStatusCode()));
        responses.forEach(r -> assertNotNull(r.getBody()));
    }

    @Override
    public void testHandlePostEntityConflict() {
        // Test data
        Report report = getTestEntityPersistent();
        Token token = getTokenPersistent(report.getSource());

        // Execution
        ResponseEntity<Report> responseEntity = getEntityController().handlePost(report, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    @Override
    protected Report getTestEntityTransient() {
        return new Report(getUserPersistent(Role.USER), getUserPersistent(Role.USER), "JUnit Report");
    }

    @Override
    protected Report getTestEntityPersistent() {
        return getEntityController().getEntityService().post(getTestEntityTransient());
    }

    @Override
    protected String getTestIdentifierInvalid() {
        return "InvalidReportIdentifier";
    }
}
