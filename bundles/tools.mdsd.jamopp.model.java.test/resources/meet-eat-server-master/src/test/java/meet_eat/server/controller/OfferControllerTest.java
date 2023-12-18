package meet_eat.server.controller;

import com.google.common.collect.Iterables;
import meet_eat.data.comparator.OfferComparableField;
import meet_eat.data.comparator.OfferComparator;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.data.predicate.OfferPredicate;
import meet_eat.data.predicate.numeric.DoubleOperation;
import meet_eat.data.predicate.numeric.PricePredicate;
import meet_eat.server.service.SubscriptionService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OfferControllerTest extends EntityControllerTest<OfferController, Offer, String> {

    @Autowired
    private SubscriptionService subscriptionService;

    @Before
    public void prepareSubscriptionRepository() {
        subscriptionService.getRepository().deleteAll();
    }

    //#region @Test @RequestMapping

    @Test
    public void testGetAllOffersNullToken() {
        // Execution
        ResponseEntity<Iterable<Offer>> responseEntity = getEntityController().getAllOffers(null, null, null, null, null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllOffersInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Iterable<Offer>> responseEntity = getEntityController().getAllOffers(null, null, null, null, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllOffersByCreatorIdentifier() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.USER));
        List<Offer> offers = new LinkedList<>();
        int entityAmount = 5;
        repeat(entityAmount, i -> offers.add(i, getOfferPersistent(token.getUser())));

        // Execution
        ResponseEntity<Iterable<Offer>> responseEntity = getEntityController().getAllOffers(token.getUser().getIdentifier(), null, null, null, token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        repeat(entityAmount, i -> assertTrue(Iterables.contains(responseEntity.getBody(), offers.get(i))));
    }

    @Test
    public void testGetAllOffersByUnknownCreatorIdentifier() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Iterable<Offer>> responseEntity = getEntityController().getAllOffers("INVALID_IDENTIFIER", null, null, null, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllOffersBySubscriberIdentifier() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.USER));
        User subscribedUser = getUserPersistent(Role.USER);
        subscriptionService.post(new Subscription(token.getUser(), subscribedUser));
        List<Offer> offers = new LinkedList<>();
        int entityAmount = 5;
        repeat(entityAmount, i -> offers.add(i, getOfferPersistent(subscribedUser)));

        // Execution
        ResponseEntity<Iterable<Offer>> responseEntity = getEntityController().getAllOffers(null, token.getUser().getIdentifier(), null, null, token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        repeat(entityAmount, i -> assertTrue(Iterables.contains(responseEntity.getBody(), offers.get(i))));
    }

    @Test
    public void testGetAllOffersByUnknownSubscriberIdentifier() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.USER));

        // Execution
        ResponseEntity<Iterable<Offer>> responseEntity = getEntityController().getAllOffers(null, "INVALID_IDENTIFIER", null, null, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testGetAllOffersWithComparator() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.USER));
        OfferComparator offerComparator = new OfferComparator(OfferComparableField.PRICE, new SphericalLocation(new SphericalPosition(0, 0)));
        List<Offer> offers = new LinkedList<>();
        int entityAmount = 5;
        repeat(entityAmount, i -> offers.add(i, getTestEntityPersistent()));

        // Execution
        ResponseEntity<Iterable<Offer>> responseEntity = getEntityController().getAllOffers(null, null, null, offerComparator, token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        offers.sort(offerComparator);
        assertTrue(Iterables.elementsEqual(responseEntity.getBody(), offers));
    }

    @Test
    public void testGetAllOffersWithPredicates() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.USER));
        OfferPredicate predicate = new PricePredicate(DoubleOperation.LESS, 12d);
        List<Offer> offers = new LinkedList<>();
        int entityAmount = 5;
        repeat(entityAmount, i -> offers.add(i, getTestEntityPersistent()));

        // Execution
        ResponseEntity<Iterable<Offer>> responseEntity = getEntityController().getAllOffers(null, null, new OfferPredicate[]{predicate}, null, token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(Iterables.elementsEqual(responseEntity.getBody(), offers.stream().filter(predicate).collect(Collectors.toList())));
    }

    @Test
    public void testGetOffer() {
        createHandleGetEndpointTest(getEntityController()::getOffer);
    }

    @Test
    public void testPostOffer() {
        // Test data
        Offer offer = getOfferTransient(getUserPersistent(Role.USER));
        Token token = getTokenPersistent(offer.getCreator());

        // Test frame
        createHandlePostEndpointTest(getEntityController()::postOffer, offer, token);
    }

    @Test
    public void testPutOfferWithIdentifier() {
        createHandlePutEndpointTest(i -> (e -> (t -> getEntityController().putOffer(i, e, t))));
    }

    @Test
    public void testPutOfferWithoutIdentifier() {
        createHandlePutEndpointTest(i -> (e -> (t -> getEntityController().putOffer(e, t))));
    }

    @Test
    public void testDeleteOfferByIdentifier() {
        createHandleDeleteByIdentifierEndpointTest(getEntityController()::deleteOffer);
    }

    @Test
    public void testDeleteOfferByEntity() {
        createHandleDeleteByEntityEndpointTest(getEntityController()::deleteOffer);
    }

    //#endregion

    //#region @Test handlePost

    @Override
    public void testHandlePostSingleEntity() {
        // Test data
        Offer offer = getOfferTransient(getUserPersistent(Role.USER));
        Token token = getTokenPersistent(offer.getCreator());

        // Execution
        ResponseEntity<Offer> responseEntity = getEntityController().handlePost(offer, token);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Override
    public void testHandlePostMultipleEntities() {
        // Test data
        List<Offer> offers = new LinkedList<>();
        List<Token> tokens = new LinkedList<>();
        int entityAmount = 4;
        repeat(entityAmount, i -> offers.add(i, getTestEntityTransient()));
        repeat(entityAmount, i -> tokens.add(i, getTokenPersistent(offers.get(i).getCreator())));

        // Execution
        List<ResponseEntity<Offer>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handlePost(offers.get(i), tokens.get(i))));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.CREATED, responses.get(i).getStatusCode()));
    }

    @Override
    public void testHandlePostEntityConflict() {
        // Test data
        Offer offer = getOfferPersistent(getUserPersistent(Role.USER));
        Token token = getTokenPersistent(offer.getCreator());

        // Execution
        ResponseEntity<Offer> responseEntity = getEntityController().handlePost(offer, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    //#endregion

    @Override
    protected Offer getTestEntityTransient() {
        return getOfferTransient(getUserPersistent(Role.USER));
    }

    @Override
    protected Offer getTestEntityPersistent() {
        return getOfferPersistent(getUserPersistent(Role.USER));
    }

    @Override
    protected String getTestIdentifierInvalid() {
        return "TestInvalidIdentifier";
    }
}
