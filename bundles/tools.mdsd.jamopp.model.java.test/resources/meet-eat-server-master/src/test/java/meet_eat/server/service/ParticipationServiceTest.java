package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Participation;
import meet_eat.data.entity.user.User;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class ParticipationServiceTest extends EntityRelationServiceTest<ParticipationService, Participation, User, Offer, String> {

    private static final class ParticipationMock extends Participation {

        private static final long serialVersionUID = -5543340191151587923L;

        protected ParticipationMock(String identifier, User source, Offer target) {
            super(identifier, source, target);
        }
    }

    //#region @Test getByOfferIdentifier

    @Test(expected = NullPointerException.class)
    public void testGetByOfferIdentifierNull() {
        // Assertions
        getEntityService().getByOfferIdentifier(null);
    }

    @Test
    public void testGetByOfferIdentifierNonExistingOfferEmpty() {
        // Test data
        String identifier = "ABCDEFG123!";

        // Execution
        Optional<Iterable<Participation>> optionalParticipations = getEntityService().getByOfferIdentifier(identifier);

        // Assertions
        assertTrue(optionalParticipations.isEmpty());
    }

    @Test
    public void testGetByOfferIdentifierExistingOfferEmpty() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());

        // Execution
        Optional<Iterable<Participation>> optionalParticipations = getEntityService().getByOfferIdentifier(offer.getIdentifier());

        // Assertions
        assertTrue(optionalParticipations.isPresent());
        assertNotNull(optionalParticipations.get());
        assertEquals(0, Iterables.size(optionalParticipations.get()));
    }

    @Test
    public void testGetByOfferIdentifierSingleParticipation() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());
        Participation participation = getRelationEntityPersistent(getSourceEntity(), offer);

        // Execution
        Optional<Iterable<Participation>> optionalParticipations = getEntityService().getByOfferIdentifier(offer.getIdentifier());

        // Assertions
        assertTrue(optionalParticipations.isPresent());
        assertNotNull(optionalParticipations.get());
        assertEquals(1, Iterables.size(optionalParticipations.get()));
        assertTrue(Iterables.contains(optionalParticipations.get(), participation));
    }

    @Test
    public void testGetByOfferIdentifierMultipleParticipations() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());
        Participation participationFst = getRelationEntityPersistent(getSourceEntity(), offer);
        Participation participationSnd = getRelationEntityPersistent(getSourceEntity(), offer);
        Participation participationForeign = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());

        // Execution
        Optional<Iterable<Participation>> optionalParticipations = getEntityService().getByOfferIdentifier(offer.getIdentifier());

        // Assertions
        assertEquals(2, Iterables.size(optionalParticipations.orElseThrow()));
        assertTrue(Iterables.contains(optionalParticipations.get(), participationFst));
        assertTrue(Iterables.contains(optionalParticipations.get(), participationSnd));
    }

    //#endregion

    //#region @Test canParticipate

    @Test(expected = NullPointerException.class)
    public void testCanParticipateNull() {
        // Assertions
        getEntityService().canParticipate(null);
    }

    @Test
    public void testCanParticipateUnknownOffer() {
        // Test data
        String identifier = "ABCDEFG";

        // Execution
        Optional<Boolean> optionalBoolean = getEntityService().canParticipate(identifier);

        // Assertions
        assertFalse(optionalBoolean.isPresent());
    }

    @Test
    public void testCanParticipateNoParticipations() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());

        // Execution
        Optional<Boolean> optionalBoolean = getEntityService().canParticipate(offer.getIdentifier());

        // Assertions
        assertTrue(optionalBoolean.isPresent());
        assertTrue(optionalBoolean.get());
    }

    @Test
    public void testCanParticipateFullOffer() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());
        for (int i = 0; i < offer.getMaxParticipants(); i++) {
            getRelationEntityPersistent(getSourceEntity(), offer);
        }

        // Execution
        Optional<Boolean> optionalBoolean = getEntityService().canParticipate(offer.getIdentifier());

        // Assertions
        assertTrue(optionalBoolean.isPresent());
        assertFalse(optionalBoolean.get());
    }

    @Test
    public void testCanParticipateNearlyFullOffer() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());
        for (int i = 0; i < offer.getMaxParticipants() - 1; i++) {
            getRelationEntityPersistent(getSourceEntity(), offer);
        }

        // Execution
        Optional<Boolean> optionalBoolean = getEntityService().canParticipate(offer.getIdentifier());

        // Assertions
        assertTrue(optionalBoolean.isPresent());
        assertTrue(optionalBoolean.get());
    }

    //#endregion

    //#region @Test existsPostConflict

    @Test
    public void testExistsPostConflictSameIdentifier() {
        // Test data
        Participation participation = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Participation conflictParticipation = new ParticipationMock(participation.getIdentifier(), getSourceEntity(), getTargetEntity());

        // Assertions
        assertTrue(getEntityService().existsPostConflict(conflictParticipation));
    }

    @Test
    public void testExistsPostConflictSameSourceAndSameTarget() {
        // Test data
        Participation participation = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Participation conflictParticipation = new Participation(participation.getSource(), participation.getTarget());

        // Assertions
        assertTrue(getEntityService().existsPostConflict(conflictParticipation));
    }

    @Test
    public void testExistsPostConflictDifferentSourceAndSameTarget() {
        // Test data
        Participation participation = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Participation conflictParticipation = new Participation(getSourceEntity(), participation.getTarget());

        // Assertions
        assertFalse(getEntityService().existsPostConflict(conflictParticipation));
    }

    @Test
    public void testExistsPostConflictSameSourceAndDifferentTarget() {
        // Test data
        Participation participation = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Participation conflictParticipation = new Participation(participation.getSource(), getTargetEntity());

        // Assertions
        assertFalse(getEntityService().existsPostConflict(conflictParticipation));
    }

    //#endregion

    @Override
    protected User getSourceEntity() {
        return getBasicUserPersistent();
    }

    @Override
    protected Offer getTargetEntity() {
        return getOfferPersistent(getBasicUserPersistent());
    }

    @Override
    protected Participation createDistinctTestEntity(User source, Offer target) {
        return new Participation(source, target);
    }
}
