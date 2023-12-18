package meet_eat.server.controller;

import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingValue;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RatingControllerTest extends EntityControllerTest<RatingController, Rating, String> {

    //#region @Test @RequestMapping

    @Test
    public void testGetGuestRatingValue() {
        // Test data
        Rating rating = getTestEntityPersistent();
        Token token = getTokenPersistent(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Double> responseEntity = getEntityController().getGuestRatingValue(rating.getTarget().getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetHostRatingValue() {
        // Test data
        Rating rating = getTestEntityPersistent();
        Token token = getTokenPersistent(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Double> responseEntity = getEntityController().getHostRatingValue(rating.getTarget().getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    public void testGetHostRatingValueNullToken() {
        // Test data
        Rating rating = getTestEntityPersistent();

        // Execution
        ResponseEntity<Double> responseEntity = getEntityController().getHostRatingValue(rating.getTarget().getIdentifier(), null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testGetHostRatingValueInvalidToken() {
        // Test data
        Rating rating = getTestEntityPersistent();
        Token token = getTokenInvalid(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Double> responseEntity = getEntityController().getHostRatingValue(rating.getTarget().getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testGetHostRatingValueUnknownIdentifier() {
        // Test data
        String identifier = getTestIdentifierInvalid();
        Token token = getTokenPersistent(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Double> responseEntity = getEntityController().getHostRatingValue(identifier, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testPostRating() {
        // Test data
        Rating rating = getTestEntityTransient();
        Token token = getTokenPersistent(rating.getSource());

        // Test frame
        createHandlePostEndpointTest((e, t) -> getEntityController().postRating(e.getTarget().getIdentifier(), e, t), rating, token);
    }

    @Test
    public void testPostRatingInvalidOfferIdentifier() {
        // Test data
        Rating rating = getTestEntityTransient();
        Token token = getTokenPersistent(rating.getSource());

        // Execution
        ResponseEntity<Rating> responseEntity = getEntityController().postRating(getTestIdentifierInvalid(), rating, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePost

    @Override
    public void testHandlePostSingleEntity() {
        // Test data
        Rating rating = getTestEntityTransient();
        Token token = getTokenPersistent(rating.getSource());

        // Execution
        ResponseEntity<Rating> responseEntity = getEntityController().handlePost(rating, token);

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
        List<Rating> ratings = new LinkedList<>();
        repeat(entityAmount, i -> ratings.add(i, Rating.createHostRating(user, getOfferPersistent(getUserPersistent(Role.USER)), RatingValue.POINTS_3)));

        // Execution
        List<ResponseEntity<Rating>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handlePost(ratings.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.CREATED, responses.get(i).getStatusCode()));
        responses.forEach(r -> assertNotNull(r.getBody()));
    }

    @Override
    public void testHandlePostEntityConflict() {
        // Test data
        Rating rating = getTestEntityPersistent();
        Token token = getTokenPersistent(rating.getSource());

        // Execution
        ResponseEntity<Rating> responseEntity = getEntityController().handlePost(rating, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePut

    @Override
    public void testHandlePutUnknownEntity() {
        // Test data
        Rating rating = getTestEntityPersistent();
        Token token = getTokenPersistent(rating.getSource());

        // Execution
        getEntityController().getEntityService().delete(rating);
        ResponseEntity<Rating> responseEntity = getEntityController().handlePut(rating.getIdentifier(), rating, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Override
    public void testHandlePutNullIdentifier() {
        // Test data
        Rating rating = getTestEntityPersistent();
        Token token = getTokenPersistent(rating.getSource());

        // Execution
        ResponseEntity<Rating> responseEntity = getEntityController().handlePut(null, rating, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    //#endregion

    @Override
    protected Rating getTestEntityTransient() {
        return Rating.createHostRating(getUserPersistent(Role.USER), getOfferPersistent(getUserPersistent(Role.USER)), RatingValue.POINTS_3);
    }

    @Override
    protected Rating getTestEntityPersistent() {
        return getEntityController().getEntityService().post(getTestEntityTransient());
    }

    @Override
    protected String getTestIdentifierInvalid() {
        return "InvalidRatingIdentifier";
    }
}
