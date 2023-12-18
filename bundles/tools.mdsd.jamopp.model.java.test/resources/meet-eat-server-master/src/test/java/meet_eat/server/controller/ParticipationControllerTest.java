package meet_eat.server.controller;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.server.service.OfferService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParticipationControllerTest extends EntityControllerTest<ParticipationController, Participation, String> {

    @Autowired
    private OfferService offerService;

    //#region @Test @RequestMapping

    @Test
    public void testGetParticipationsByOffer() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        ResponseEntity<Iterable<Participation>> responseEntity = getEntityController().getParticipationsByOffer(participation.getTarget().getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(Iterables.contains(responseEntity.getBody(), participation));
    }

    @Test
    public void testGetParticipationsByOfferUnknownIdentifier() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        ResponseEntity<Iterable<Participation>> responseEntity = getEntityController().getParticipationsByOffer(getTestIdentifierInvalid(), token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetParticipationsByOfferNullToken() {
        // Test data
        Participation participation = getTestEntityPersistent();

        // Execution
        ResponseEntity<Iterable<Participation>> responseEntity = getEntityController().getParticipationsByOffer(participation.getTarget().getIdentifier(), null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testGetParticipationsByOfferInvalidToken() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenInvalid(participation.getSource());

        // Execution
        ResponseEntity<Iterable<Participation>> responseEntity = getEntityController().getParticipationsByOffer(participation.getTarget().getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testPostParticipation() {
        // Test data
        Participation participation = getTestEntityTransient();
        Token token = getTokenPersistent(participation.getSource());

        // Test frame
        createHandlePostEndpointTest((e, t) -> getEntityController().postParticipation(e.getTarget().getIdentifier(), e, t), participation, token);
    }

    @Test
    public void testPostParticipationInvalidOfferIdentifier() {
        // Test data
        Participation participation = getTestEntityTransient();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        ResponseEntity<Participation> responseEntity = getEntityController().postParticipation(getTestIdentifierInvalid(), participation, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void testPostParticipationDeletedOffer() {
        // Test data
        Participation participation = getTestEntityTransient();
        Token token = getTokenPersistent(participation.getSource());
        offerService.delete(participation.getTarget());

        // Execution
        ResponseEntity<Participation> responseEntity = getEntityController().postParticipation(participation.getTarget().getIdentifier(), participation, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testPostParticipationFullOffer() {
        // Test data
        Participation participation = getTestEntityTransient();
        Token token = getTokenPersistent(participation.getSource());
        repeat(participation.getTarget().getMaxParticipants(), i -> getEntityController().getEntityService().post(new Participation(getUserPersistent(Role.USER), participation.getTarget())));

        // Execution
        ResponseEntity<Participation> responseEntity = getEntityController().postParticipation(participation.getTarget().getIdentifier(), participation, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteParticipation() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Test frame
        createHandleDeleteByEntityEndpointTest((e, t) -> getEntityController().deleteParticipation(e.getTarget().getIdentifier(), e, t), participation, token);
    }

    @Test
    public void testDeleteBookmarkInvalidOfferIdentifier() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().deleteParticipation(getTestIdentifierInvalid(), participation, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePost

    @Override
    public void testHandlePostSingleEntity() {
        // Test data
        Participation participation = getTestEntityTransient();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        ResponseEntity<Participation> responseEntity = getEntityController().handlePost(participation, token);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Override
    public void testHandlePostMultipleEntities() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        int entityAmount = 3;
        List<Participation> participations = new LinkedList<>();
        repeat(entityAmount, i -> participations.add(i, new Participation(user, getOfferPersistent(getUserPersistent(Role.USER)))));

        // Execution
        List<ResponseEntity<Participation>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handlePost(participations.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.CREATED, responses.get(i).getStatusCode()));
        responses.forEach(r -> assertNotNull(r.getBody()));
    }

    @Override
    public void testHandlePostEntityConflict() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        ResponseEntity<Participation> responseEntity = getEntityController().handlePost(participation, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePut

    @Override
    public void testHandlePutUnknownEntity() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        getEntityController().getEntityService().delete(participation);
        ResponseEntity<Participation> responseEntity = getEntityController().handlePut(participation.getIdentifier(), participation, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Override
    public void testHandlePutNullIdentifier() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        ResponseEntity<Participation> responseEntity = getEntityController().handlePut(null, participation, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handleDelete

    @Override
    public void testHandleDeleteSingleEntity() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(participation, token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Override
    public void testHandleDeleteMultipleEntities() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        int entityAmount = 4;
        List<Participation> participations = new LinkedList<>();
        repeat(entityAmount, i -> participations.add(getEntityController().getEntityService().post(new Participation(user, getOfferPersistent(getUserPersistent(Role.USER))))));

        // Execution
        List<ResponseEntity<Void>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handleDelete(participations.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.NO_CONTENT, responses.get(i).getStatusCode()));
    }

    @Override
    public void testHandleDeleteExistingIdentifier() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(participation.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Override
    public void testHandleDeleteMultipleIdentifiers() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        int entityAmount = 4;
        List<Participation> participations = new LinkedList<>();
        repeat(entityAmount, i -> participations.add(getEntityController().getEntityService().post(new Participation(user, getOfferPersistent(getUserPersistent(Role.USER))))));

        // Execution
        List<ResponseEntity<Void>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handleDelete(participations.get(i).getIdentifier(), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.NO_CONTENT, responses.get(i).getStatusCode()));
    }

    @Override
    public void testHandleDeleteUnknownEntity() {
        // Test data
        Participation participation = getTestEntityPersistent();
        Token token = getTokenPersistent(participation.getSource());

        // Execution
        getEntityController().handleDelete(participation, token);
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(participation, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    //#endregion

    @Override
    protected Participation getTestEntityTransient() {
        return new Participation(getUserPersistent(Role.USER), getOfferPersistent(getUserPersistent(Role.USER)));
    }

    @Override
    protected Participation getTestEntityPersistent() {
        return getEntityController().getEntityService().post(getTestEntityTransient());
    }

    @Override
    protected String getTestIdentifierInvalid() {
        return "InvalidParticipationIdentifier";
    }
}
