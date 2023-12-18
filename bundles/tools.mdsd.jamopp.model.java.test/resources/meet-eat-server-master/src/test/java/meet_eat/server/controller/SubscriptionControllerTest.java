package meet_eat.server.controller;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SubscriptionControllerTest extends EntityControllerTest<SubscriptionController, Subscription, String> {

    //#region @Test @RequestMapping

    @Test
    public void testGetSubscriptionByUser() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Iterable<Subscription>> responseEntity = getEntityController().getSubscriptionsByUser(subscription.getSource().getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(Iterables.contains(responseEntity.getBody(), subscription));
    }

    @Test
    public void testGetSubscriptionByUserUnknownIdentifier() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Iterable<Subscription>> responseEntity = getEntityController().getSubscriptionsByUser(getTestIdentifierInvalid(), token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetSubscriptionByUserNullToken() {
        // Test data
        Subscription subscription = getTestEntityPersistent();

        // Execution
        ResponseEntity<Iterable<Subscription>> responseEntity = getEntityController().getSubscriptionsByUser(subscription.getSource().getIdentifier(), null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testGetSubscriptionByUserInvalidToken() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenInvalid(subscription.getSource());

        // Execution
        ResponseEntity<Iterable<Subscription>> responseEntity = getEntityController().getSubscriptionsByUser(subscription.getSource().getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testPostSubscription() {
        // Test data
        Subscription subscription = getTestEntityTransient();
        Token token = getTokenPersistent(subscription.getSource());

        // Test frame
        createHandlePostEndpointTest((e, t) -> getEntityController().postSubscription(e.getSource().getIdentifier(), e, t), subscription, token);
    }

    @Test
    public void testPostSubscriptionInvalidOfferIdentifier() {
        // Test data
        Subscription subscription = getTestEntityTransient();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Subscription> responseEntity = getEntityController().postSubscription(getTestIdentifierInvalid(), subscription, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteSubscriptionBySubscribedUser() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().deleteSubscriptionBySubscribedUser(subscription.getSource().getIdentifier(), subscription.getTarget(), token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteSubscriptionBySubscribedUserInvalidIdentifier() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().deleteSubscriptionBySubscribedUser(getTestIdentifierInvalid(), subscription.getTarget(), token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testDeleteSubscriptionBySubscribedUserNotSubscribed() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().deleteSubscriptionBySubscribedUser(token.getUser().getIdentifier(), getUserPersistent(Role.USER), token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePost

    @Override
    public void testHandlePostSingleEntity() {
        // Test data
        Subscription subscription = getTestEntityTransient();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Subscription> responseEntity = getEntityController().handlePost(subscription, token);

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
        List<Subscription> subscriptions = new LinkedList<>();
        repeat(entityAmount, i -> subscriptions.add(i, new Subscription(user, getUserPersistent(Role.USER))));

        // Execution
        List<ResponseEntity<Subscription>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handlePost(subscriptions.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.CREATED, responses.get(i).getStatusCode()));
        responses.forEach(r -> assertNotNull(r.getBody()));
    }

    @Override
    public void testHandlePostEntityConflict() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Subscription> responseEntity = getEntityController().handlePost(subscription, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handlePut

    @Override
    public void testHandlePutUnknownEntity() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        getEntityController().getEntityService().delete(subscription);
        ResponseEntity<Subscription> responseEntity = getEntityController().handlePut(subscription.getIdentifier(), subscription, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Override
    public void testHandlePutNullIdentifier() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Subscription> responseEntity = getEntityController().handlePut(null, subscription, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handleDelete

    @Override
    public void testHandleDeleteSingleEntity() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(subscription, token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Override
    public void testHandleDeleteMultipleEntities() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        int entityAmount = 4;
        List<Subscription> subscriptions = new LinkedList<>();
        repeat(entityAmount, i -> subscriptions.add(getEntityController().getEntityService().post(new Subscription(user, getUserPersistent(Role.USER)))));

        // Execution
        List<ResponseEntity<Void>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handleDelete(subscriptions.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.NO_CONTENT, responses.get(i).getStatusCode()));
    }

    @Override
    public void testHandleDeleteExistingIdentifier() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(subscription.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Override
    public void testHandleDeleteMultipleIdentifiers() {
        // Test data
        User user = getUserPersistent(Role.USER);
        Token token = getTokenPersistent(user);
        int entityAmount = 4;
        List<Subscription> subscriptions = new LinkedList<>();
        repeat(entityAmount, i -> subscriptions.add(getEntityController().getEntityService().post(new Subscription(user, getUserPersistent(Role.USER)))));

        // Execution
        List<ResponseEntity<Void>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handleDelete(subscriptions.get(i).getIdentifier(), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.NO_CONTENT, responses.get(i).getStatusCode()));
    }

    @Override
    public void testHandleDeleteUnknownEntity() {
        // Test data
        Subscription subscription = getTestEntityPersistent();
        Token token = getTokenPersistent(subscription.getSource());

        // Execution
        getEntityController().handleDelete(subscription, token);
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(subscription, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    //#endregion

    @Override
    protected Subscription getTestEntityTransient() {
        return new Subscription(getUserPersistent(Role.USER), getUserPersistent(Role.USER));
    }

    @Override
    protected Subscription getTestEntityPersistent() {
        return getEntityController().getEntityService().post(getTestEntityTransient());
    }

    @Override
    protected String getTestIdentifierInvalid() {
        return "InvalidSubscriptionIdentifier";
    }
}
