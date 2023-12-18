package meet_eat.server.service;

import com.google.common.collect.Streams;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.rating.Rating;
import meet_eat.data.entity.relation.rating.RatingBasis;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link Rating ratings} and their state
 * persistence.
 */
@Service
public class RatingService extends EntityRelationService<Rating, User, User, String, RatingRepository> {

    private static final int MIN_AMOUNT_RATINGS = 5;
    private static final double DEFAULT_NOT_ENOUGH_RATINGS = 0d;
    private static final int ROUNDING_FACTOR = 10;

    private final UserService userService;

    /**
     * Constructs a new instance of {@link RatingService}.
     *
     * @param repository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public RatingService(RatingRepository repository, UserService userService) {
        super(repository);
        this.userService = userService;
    }

    /**
     * Deletes all {@link Rating ratings} containing a specific {@link Offer offer}.
     *
     * @param offer the offer of the ratings to be deleted
     */
    public void deleteByOffer(Offer offer) {
        getRepository().deleteByOffer(Objects.requireNonNull(offer));
    }

    /**
     * Gets the averaged value of the {@link Rating ratings} of a given {@link User user} filtered by the
     * {@link RatingBasis basis} of the ratings.
     *
     * @param user        the source user of the ratings to be used
     * @param ratingBasis the basis of the ratings to be used
     * @return the averaged rating of a user
     */
    public double getRatingValue(User user, RatingBasis ratingBasis) {
        Iterable<Rating> ratings = getByTarget(user);
        long ratingCount = countRatings(ratings, ratingBasis);
        double roundedRatingAverage = roundToFirstDecimal(averageRatings(ratings, ratingBasis));
        return (ratingCount >= MIN_AMOUNT_RATINGS) ? roundedRatingAverage : DEFAULT_NOT_ENOUGH_RATINGS;
    }

    /**
     * Gets the averaged value of the {@link Rating ratings} of a given {@link User user} filtered by the
     * {@link RatingBasis basis} of the ratings.
     *
     * @param userIdentifier the identifier of the source user of the ratings to be used
     * @param ratingBasis    the basis of the ratings to be used
     * @return the averaged rating of a user
     */
    public Optional<Double> getRatingValue(String userIdentifier, RatingBasis ratingBasis) {
        Optional<User> optionalUser = userService.get(userIdentifier);
        return optionalUser.map(user -> getRatingValue(user, ratingBasis));
    }

    /**
     * Counts the amount of {@link Rating ratings} based on a given {@link RatingBasis}.
     *
     * @param ratings     the ratings to be filtered by {@link RatingBasis} and counted
     * @param ratingBasis the rating basis of the ratings to be counted
     * @return the amount of specific ratings
     */
    private long countRatings(Iterable<Rating> ratings, RatingBasis ratingBasis) {
        return Streams.stream(ratings).filter(x -> x.getBasis().equals(ratingBasis)).count();
    }

    /**
     * Sums up the values of {@link Rating ratings} based on a given {@link RatingBasis}.
     *
     * @param ratings     the ratings to be filtered by {@link RatingBasis} and summed up
     * @param ratingBasis the rating basis of the ratings to be summed up
     * @return the sum of specific ratings
     */
    private int sumRatings(Iterable<Rating> ratings, RatingBasis ratingBasis) {
        return Streams.stream(ratings)
                .filter(x -> x.getBasis().equals(ratingBasis))
                .mapToInt(x -> x.getValue().getIntegerValue())
                .sum();
    }

    /**
     * Averages the values of {@link Rating ratings} based on a given {@link RatingBasis}.
     *
     * @param ratings     the ratings to be filtered by {@link RatingBasis} and averaged
     * @param ratingBasis the rating basis of the ratings to be averaged
     * @return the average of specific ratings
     */
    private double averageRatings(Iterable<Rating> ratings, RatingBasis ratingBasis) {
        return (double) sumRatings(ratings, ratingBasis) / (double) countRatings(ratings, ratingBasis);
    }

    /**
     * Rounds a value to its first decimal.
     *
     * @param value the value to be rounded
     * @return the value rounded to first decimal
     */
    private double roundToFirstDecimal(double value) {
        return (((double) Math.round(value * ROUNDING_FACTOR)) / ROUNDING_FACTOR);
    }
}
