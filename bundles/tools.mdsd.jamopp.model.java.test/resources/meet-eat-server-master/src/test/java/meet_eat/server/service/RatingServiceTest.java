package meet_eat.server.service;

import com.google.common.collect.Iterables;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingBasis;
import meet_eat.data.entity.relation.rating.RatingValue;
import meet_eat.data.entity.user.User;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RatingServiceTest extends EntityRelationServiceTest<RatingService, Rating, User, User, String> {

    private static final int MIN_AMOUNT_RATINGS = 5;
    private static final double DEFAULT_NOT_ENOUGH_RATINGS = 0d;

    //#region @Test deleteByOffer

    @Test(expected = NullPointerException.class)
    public void testDeleteByOfferNull() {
        // Assertions
        getEntityService().deleteByOffer(null);
    }

    @Test
    public void testDeleteByOfferIdentifierEmpty() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());

        // Execution
        getEntityService().deleteByOffer(offer);

        // Assertions
        assertEquals(0, Iterables.size(getEntityService().getAll()));
    }

    @Test
    public void testDeleteByOfferIdentifierSingleRating() {
        // Test data
        Rating rating = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());
        Offer offer = rating.getOffer();

        // Assertions: Pre-Deletion
        assertTrue(getEntityService().exists(rating.getIdentifier()));

        // Execution
        getEntityService().deleteByOffer(offer);

        // Assertions: Post-Deletion
        assertFalse(getEntityService().exists(rating.getIdentifier()));
    }

    @Test
    public void testDeleteByOfferIdentifierMultipleRatings() {
        // Test data
        Offer offer = getOfferPersistent(getBasicUserPersistent());
        Rating ratingFst = getEntityService().post(Rating.createHostRating(getBasicUserPersistent(), offer, RatingValue.POINTS_3));
        Rating ratingSnd = getEntityService().post(Rating.createHostRating(getBasicUserPersistent(), offer, RatingValue.POINTS_3));
        Rating ratingForeign = getRelationEntityPersistent(getSourceEntity(), getTargetEntity());

        // Assertions: Pre-Deletion
        assertTrue(getEntityService().exists(ratingFst.getIdentifier()));
        assertTrue(getEntityService().exists(ratingSnd.getIdentifier()));
        assertTrue(getEntityService().exists(ratingForeign.getIdentifier()));

        // Execution
        getEntityService().deleteByOffer(offer);

        // Assertions: Post-Deletion
        assertFalse(getEntityService().exists(ratingFst.getIdentifier()));
        assertFalse(getEntityService().exists(ratingSnd.getIdentifier()));
        assertTrue(getEntityService().exists(ratingForeign.getIdentifier()));
    }

    //#endregion

    //#region @Test getRatingValue

    @Test(expected = NullPointerException.class)
    public void testGetRatingValueNullUser() {
        // Assertions
        getEntityService().getRatingValue((User) null, RatingBasis.HOST);
    }

    @Test(expected = NullPointerException.class)
    public void testGetRatingValueNullIdentifier() {
        // Assertions
        getEntityService().getRatingValue((String) null, RatingBasis.HOST);
    }

    @Test
    public void testGetRatingValueExistingIdentifierNullRatingBase() {
        // Test data
        User user = getBasicUserPersistent();

        // Assertions
        assertEquals(DEFAULT_NOT_ENOUGH_RATINGS, getEntityService().getRatingValue(user.getIdentifier(), null).orElseThrow(), 0);
    }

    @Test
    public void testGetRatingValueExistingUserNullRatingBase() {
        // Test data
        User user = getBasicUserPersistent();

        // Assertions
        assertEquals(DEFAULT_NOT_ENOUGH_RATINGS, getEntityService().getRatingValue(user, null), 0);
    }

    @Test
    public void testGetRatingValueExistingUserZeroRatings() {
        // Test data
        User user = getBasicUserPersistent();

        // Execution
        double value = getEntityService().getRatingValue(user, RatingBasis.HOST);

        // Assertions
        assertEquals(0d, value, 0);
    }

    @Test
    public void testGetRatingValueExistingIdentifierZeroRatings() {
        // Test data
        User user = getBasicUserPersistent();

        // Execution
        Optional<Double> optionalValue = getEntityService().getRatingValue(user.getIdentifier(), RatingBasis.HOST);

        // Assertions
        assertEquals(0d, optionalValue.orElseThrow(), 0);
    }

    @Test
    public void testGetRatingValueUnknownIdentifier() {
        // Test data
        String identifier = "ABCDEFG";

        // Execution
        Optional<Double> optionalValue = getEntityService().getRatingValue(identifier, RatingBasis.HOST);

        // Assertions
        assertTrue(optionalValue.isEmpty());
    }

    @Test
    public void testGetRatingValueExistingUserNotEnoughRatings() {
        // Test data
        User user = getBasicUserPersistent();
        Rating rating = getRelationEntityPersistent(getSourceEntity(), user);

        // Execution
        double value = getEntityService().getRatingValue(user, RatingBasis.HOST);

        // Assertions
        assertEquals(DEFAULT_NOT_ENOUGH_RATINGS, value, 0);
    }

    @Test
    public void testGetRatingValueExistingIdentifierNotEnoughRatings() {
        // Test data
        User user = getBasicUserPersistent();
        Rating rating = getRelationEntityPersistent(getSourceEntity(), user);

        // Execution
        Optional<Double> optionalValue = getEntityService().getRatingValue(user.getIdentifier(), RatingBasis.HOST);

        // Assertions
        assertEquals(DEFAULT_NOT_ENOUGH_RATINGS, optionalValue.orElseThrow(), 0);
    }

    @Test
    public void testGetRatingValueExistingUserEnoughRatings() {
        // Test data
        User user = getBasicUserPersistent();
        int ratingSum = 0;
        for (int i = 0; i < MIN_AMOUNT_RATINGS; i++) {
            ratingSum += getRelationEntityPersistent(getSourceEntity(), user).getValue().getIntegerValue();
        }
        double ratingAverage = (double) ratingSum / (double) MIN_AMOUNT_RATINGS;

        // Execution
        double value = getEntityService().getRatingValue(user, RatingBasis.HOST);

        // Assertions
        assertEquals(ratingAverage, value, 0.05);
    }

    @Test
    public void testGetRatingValueExistingIdentifierEnoughRatings() {
        // Test data
        User user = getBasicUserPersistent();
        int ratingSum = 0;
        for (int i = 0; i < MIN_AMOUNT_RATINGS; i++) {
            ratingSum += getRelationEntityPersistent(getSourceEntity(), user).getValue().getIntegerValue();
        }
        double ratingAverage = (double) ratingSum / (double) MIN_AMOUNT_RATINGS;

        // Execution
        Optional<Double> optionalValue = getEntityService().getRatingValue(user.getIdentifier(), RatingBasis.HOST);

        // Assertions
        assertEquals(ratingAverage, optionalValue.orElseThrow(), 0.05);
    }

    //#endregion

    @Override
    protected User getSourceEntity() {
        return getBasicUserPersistent();
    }

    @Override
    protected User getTargetEntity() {
        return getBasicUserPersistent();
    }

    @Override
    protected Rating createDistinctTestEntity(User source, User target) {
        return Rating.createHostRating(source, getOfferPersistent(target), RatingValue.POINTS_3);
    }
}
