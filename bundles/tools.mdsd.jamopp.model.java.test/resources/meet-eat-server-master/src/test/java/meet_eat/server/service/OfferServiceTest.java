package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.relation.Subscription;
import meet_eat.data.entity.user.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class OfferServiceTest extends EntityServiceTest<OfferService, Offer, String> {

    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    private ParticipationService participationService;

    //#region @Test getByCreatorId

    @Test(expected = NullPointerException.class)
    public void testGetByCreatorIdNull() {
        // Execution
        getEntityService().getByCreatorId(null);
    }

    @Test
    public void testGetByCreatorId() {
        // Test data
        User creator = getBasicUserPersistent();
        User otherCreator = getBasicUserPersistent();
        Offer offerFst = getOfferPersistent(creator);
        Offer offerSnd = getOfferPersistent(creator);
        Offer offerTrd = getOfferPersistent(otherCreator);

        // Execution
        Iterable<Offer> gotOffers = getEntityService().getByCreatorId(creator.getIdentifier()).orElseThrow();

        // Assertions
        assertFalse(Iterables.isEmpty(gotOffers));
        assertEquals(2, Iterables.size(gotOffers));
        assertTrue(Iterables.contains(gotOffers, offerFst));
        assertTrue(Iterables.contains(gotOffers, offerSnd));
        assertFalse(Iterables.contains(gotOffers, offerTrd));
    }

    //#endregion

    //#region @Test deleteByCreator

    @Test(expected = NullPointerException.class)
    public void testDeleteByCreatorEntityNull() {
        // Execution
        getEntityService().deleteByCreator((User) null);
    }

    @Test(expected = NullPointerException.class)
    public void testDeleteByCreatorIdentifierNull() {
        // Execution
        getEntityService().deleteByCreator((String) null);
    }

    @Test
    public void testDeleteByCreatorEntity() {
        // Test data
        User creator = getBasicUserPersistent();
        User otherCreator = getBasicUserPersistent();
        Offer offerFst = getOfferPersistent(creator);
        Offer offerSnd = getOfferPersistent(creator);
        Offer offerTrd = getOfferPersistent(otherCreator);

        // Execution
        getEntityService().deleteByCreator(creator);
        Iterable<Offer> gotOffers = getEntityService().getAll();

        // Assertions
        assertFalse(Iterables.isEmpty(gotOffers));
        assertEquals(1, Iterables.size(gotOffers));
        assertFalse(Iterables.contains(gotOffers, offerFst));
        assertFalse(Iterables.contains(gotOffers, offerSnd));
        assertTrue(Iterables.contains(gotOffers, offerTrd));
    }

    @Test
    public void testDeleteByCreatorIdentifier() {
        // Test data
        User creator = getBasicUserPersistent();
        User otherCreator = getBasicUserPersistent();
        Offer offerFst = getOfferPersistent(creator);
        Offer offerSnd = getOfferPersistent(creator);
        Offer offerTrd = getOfferPersistent(otherCreator);

        // Execution
        getEntityService().deleteByCreator(creator.getIdentifier());
        Iterable<Offer> gotOffers = getEntityService().getAll();

        // Assertions
        assertFalse(Iterables.isEmpty(gotOffers));
        assertEquals(1, Iterables.size(gotOffers));
        assertFalse(Iterables.contains(gotOffers, offerFst));
        assertFalse(Iterables.contains(gotOffers, offerSnd));
        assertTrue(Iterables.contains(gotOffers, offerTrd));
    }

    //#endregion

    //#region @Test existsPutConflict

    @Test(expected = NullPointerException.class)
    public void testExistsPutConflictNull() {
        // Execution
        getEntityService().existsPutConflict(null);
    }

    @Test
    public void testExistsPutConflictParticipantAmount() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());
        for (int i = 0; i < offer.getMaxParticipants(); i++) {
            participationService.post(new Participation(getBasicUserPersistent(), offer));
        }

        // Assertions: Pre-Decrement
        assertFalse(getEntityService().existsPutConflict(offer));

        // Execution
        offer.setMaxParticipants(offer.getMaxParticipants() - 1);

        // Assertions: Post-Decrement
        assertTrue(getEntityService().existsPutConflict(offer));
    }

    //#endregion

    //#region @Test getNumericHostRatingValue

    @Test(expected = NullPointerException.class)
    public void testGetNumericHostRatingValueNull() {
        // Assertions
        getEntityService().getNumericHostRating(null);
    }

    @Test
    public void testGetNumericHostRatingValue() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());

        // Execution
        double value = getEntityService().getNumericHostRating(offer);

        // Assertions
        assertEquals(0d, value, 0);
    }

    //#endregion

    //#region @Test getBySubscriberIdentifier

    @Test(expected = NullPointerException.class)
    public void testGetBySubscriberIdentifierNull() {
        // Assertions
        getEntityService().getBySubscriberIdentifier(null);
    }

    @Test
    public void testGetBySubscriberIdentifierUnknownIdentifier() {
        // Test data
        String identifier = "ABCDEFG";

        // Execution
        Optional<Iterable<Offer>> optionalOffers = getEntityService().getBySubscriberIdentifier(identifier);

        // Assertions
        assertTrue(optionalOffers.isEmpty());
    }

    @Test
    public void testGetBySubscriberIdentifierNoSubscriptions() {
        // Test data
        User user = getBasicUserPersistent();

        // Execution
        Optional<Iterable<Offer>> optionalOffers = getEntityService().getBySubscriberIdentifier(user.getIdentifier());

        // Assertions
        assertTrue(optionalOffers.isPresent());
        assertNotNull(optionalOffers.get());
        assertEquals(0, Iterables.size(optionalOffers.get()));
    }

    @Test
    public void testGetBySubscriberIdentifierSingleSubscription() {
        // Test data
        User subscribingUser = getBasicUserPersistent();
        User subscribedUser = getBasicUserPersistent();
        subscriptionService.post(new Subscription(subscribingUser, subscribedUser));
        Offer offerFst = getOfferPersistent(subscribedUser);
        Offer offerSnd = getOfferPersistent(subscribedUser);
        Offer offerTrd = getOfferPersistent(getBasicUserPersistent());

        // Execution
        Iterable<Offer> offers = getEntityService().getBySubscriberIdentifier(subscribingUser.getIdentifier()).orElseThrow();

        // Assertions
        assertEquals(2, Iterables.size(offers));
        assertTrue(Iterables.contains(offers, offerFst));
        assertTrue(Iterables.contains(offers, offerSnd));
        assertFalse(Iterables.contains(offers, offerTrd));
    }

    @Test
    public void testGetBySubscriberIdentifierMultipleSubscriptions() {
        // Test data
        User subscribingUser = getBasicUserPersistent();
        User subscribedUserFst = getBasicUserPersistent();
        User subscribedUserSnd = getBasicUserPersistent();
        subscriptionService.post(new Subscription(subscribingUser, subscribedUserFst));
        subscriptionService.post(new Subscription(subscribingUser, subscribedUserSnd));
        Offer offerFst = getOfferPersistent(subscribedUserFst);
        Offer offerSnd = getOfferPersistent(subscribedUserFst);
        Offer offerTrd = getOfferPersistent(getBasicUserPersistent());
        Offer offerFth = getOfferPersistent(subscribedUserSnd);

        // Execution
        Iterable<Offer> offers = getEntityService().getBySubscriberIdentifier(subscribingUser.getIdentifier()).orElseThrow();

        // Assertions
        assertEquals(3, Iterables.size(offers));
        assertTrue(Iterables.contains(offers, offerFst));
        assertTrue(Iterables.contains(offers, offerSnd));
        assertFalse(Iterables.contains(offers, offerTrd));
        assertTrue(Iterables.contains(offers, offerFth));
    }

    //#endregion

    @Override
    protected Offer createDistinctTestEntity() {
        return getOfferTransient(getBasicUserPersistent());
    }
}
