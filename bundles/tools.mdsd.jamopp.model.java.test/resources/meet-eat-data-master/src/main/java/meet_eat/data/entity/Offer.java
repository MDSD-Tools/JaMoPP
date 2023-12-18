package meet_eat.data.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.Localizable;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an {@link Entity} of a {@link User}, which can be used to
 * arrange a meet to eating together.
 */
public class Offer extends Entity<String> implements Reportable {

    private static final long serialVersionUID = -5026750026998176586L;

    private static final String ERROR_MESSAGE_TEMPLATE_NULL = "The %s must not be null.";
    private static final String ERROR_MESSAGE_NULL_CREATOR = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "creator");
    private static final String ERROR_MESSAGE_NULL_TAGS = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "tags");
    private static final String ERROR_MESSAGE_NULL_TAG = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "tag");
    private static final String ERROR_MESSAGE_NULL_NAME = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "name");
    private static final String ERROR_MESSAGE_NULL_DESCRIPTION = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "description");
    private static final String ERROR_MESSAGE_NULL_DATE_TIME = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "dateTime");
    private static final String ERROR_MESSAGE_NULL_LOCATION = String.format(ERROR_MESSAGE_TEMPLATE_NULL, "location");
    private static final String ERROR_MESSAGE_ILLEGAL_PRICE = "The price must be greater or equals than 0.";
    private static final String ERROR_MESSAGE_ILLEGAL_MAX_PARTICIPANTS = "At least one individual has to participate.";

    private static final double DEFAULT_MIN_PRICE = 0d;
    private static final int DEFAULT_MIN_PARTICIPANTS = 1;

    @DBRef
    @JsonProperty
    private final User creator;
    @DBRef
    @JsonProperty
    private final Set<Tag> tags;

    @JsonProperty
    private String name;
    @JsonProperty
    private String description;
    @JsonProperty
    private double price;
    @JsonProperty
    private int maxParticipants;
    @JsonProperty
    private LocalDateTime dateTime;
    @JsonProperty
    private Localizable location;

    /**
     * Creates an offer.
     *
     * @param creator         the creator
     * @param tags            the offer tags
     * @param name            the name
     * @param description     the description
     * @param price           the price to be paid for participation
     * @param maxParticipants the maximum number of participating users
     * @param dateTime        the date and time at which the offer takes place
     * @param location        the location at which the offer takes place
     */
    public Offer(User creator, Set<Tag> tags, String name, String description, double price,
                 int maxParticipants, LocalDateTime dateTime, Localizable location) {

        this.creator = Objects.requireNonNull(creator, ERROR_MESSAGE_NULL_CREATOR);
        this.tags = Objects.requireNonNull(tags, ERROR_MESSAGE_NULL_TAGS);
        this.name = Objects.requireNonNull(name, ERROR_MESSAGE_NULL_NAME);
        this.description = Objects.requireNonNull(description, ERROR_MESSAGE_NULL_DESCRIPTION);
        if (price < DEFAULT_MIN_PRICE) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ILLEGAL_PRICE);
        }
        this.price = price;
        if (maxParticipants < DEFAULT_MIN_PARTICIPANTS) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ILLEGAL_MAX_PARTICIPANTS);
        }
        this.maxParticipants = maxParticipants;
        this.dateTime = Objects.requireNonNull(dateTime, ERROR_MESSAGE_NULL_DATE_TIME);
        this.location = Objects.requireNonNull(location, ERROR_MESSAGE_NULL_LOCATION);
    }

    /**
     * Creates an offer.
     *
     * @param identifier      the identifier
     * @param creator         the creator
     * @param tags            the offer tags
     * @param name            the name
     * @param description     the description
     * @param price           the price to be paid for participation
     * @param maxParticipants the maximum number of participating users
     * @param dateTime        the date and time at which the offer takes place
     * @param location        the location at which the offer takes place
     */
    @JsonCreator
    @PersistenceConstructor
    public Offer(@JsonProperty("identifier") String identifier,
                 @JsonProperty("creator") User creator,
                 @JsonProperty("tags") Set<Tag> tags,
                 @JsonProperty("name") String name,
                 @JsonProperty("description") String description,
                 @JsonProperty("price") double price,
                 @JsonProperty("maxParticipants") int maxParticipants,
                 @JsonProperty("dateTime") LocalDateTime dateTime,
                 @JsonProperty("location") Localizable location) {

        super(identifier);
        this.creator = Objects.requireNonNull(creator, ERROR_MESSAGE_NULL_CREATOR);
        this.tags = Objects.requireNonNull(tags, ERROR_MESSAGE_NULL_TAGS);
        this.name = Objects.requireNonNull(name, ERROR_MESSAGE_NULL_NAME);
        this.description = Objects.requireNonNull(description, ERROR_MESSAGE_NULL_DESCRIPTION);
        if (price < DEFAULT_MIN_PRICE) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ILLEGAL_PRICE);
        }
        this.price = price;
        if (maxParticipants < DEFAULT_MIN_PARTICIPANTS) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ILLEGAL_MAX_PARTICIPANTS);
        }
        this.maxParticipants = maxParticipants;
        this.dateTime = Objects.requireNonNull(dateTime, ERROR_MESSAGE_NULL_DATE_TIME);
        this.location = Objects.requireNonNull(location, ERROR_MESSAGE_NULL_LOCATION);
    }

    /**
     * Gets the creator.
     *
     * @return the creator
     */
    @JsonGetter
    public User getCreator() {
        return creator;
    }

    /**
     * Gets the tags.
     *
     * @return the tags
     */
    @JsonGetter
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @JsonGetter
    public String getName() {
        return name;
    }

    /**
     * Gets the description.
     *
     * @return the description
     */
    @JsonGetter
    public String getDescription() {
        return description;
    }

    /**
     * Gets the price.
     *
     * @return the price
     */
    @JsonGetter
    public double getPrice() {
        return price;
    }

    /**
     * Gets the maximum amount of participating users.
     *
     * @return the maximum amount of participating users
     */
    @JsonGetter
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Gets the date and time at which the offer takes place.
     *
     * @return the date and time
     */
    @JsonGetter
    public LocalDateTime getDateTime() {
        return dateTime;
    }

    /**
     * Gets the location at which the offer takes place.
     *
     * @return the location
     */
    @JsonGetter
    public Localizable getLocation() {
        return location;
    }

    /**
     * Sets the name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = Objects.requireNonNull(name, ERROR_MESSAGE_NULL_NAME);
    }

    /**
     * Sets the description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = Objects.requireNonNull(description, ERROR_MESSAGE_NULL_DESCRIPTION);
    }

    /**
     * Sets the price.
     *
     * @param price the price
     */
    public void setPrice(double price) {
        if (price < DEFAULT_MIN_PRICE) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ILLEGAL_PRICE);
        }
        this.price = price;
    }

    /**
     * Sets the maximum amount of participating users.
     *
     * @param maxParticipants the maximum amount of participating users
     */
    public void setMaxParticipants(int maxParticipants) {
        if (maxParticipants < DEFAULT_MIN_PARTICIPANTS) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ILLEGAL_MAX_PARTICIPANTS);
        }
        this.maxParticipants = maxParticipants;
    }

    /**
     * Sets the date and time.
     *
     * @param dateTime the date and time
     */
    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = Objects.requireNonNull(dateTime, ERROR_MESSAGE_NULL_DATE_TIME);
    }

    /**
     * Sets the location.
     *
     * @param location the location
     */
    public void setLocation(Localizable location) {
        this.location = Objects.requireNonNull(location, ERROR_MESSAGE_NULL_LOCATION);
    }

    /**
     * Adds a {@link Tag}.
     *
     * @param tag the tag
     */
    public void addTag(Tag tag) {
        tags.add(Objects.requireNonNull(tag, ERROR_MESSAGE_NULL_TAG));
    }

    /**
     * Removes a {@link Tag}.
     *
     * @param tag the tag
     */
    public void removeTag(Tag tag) {
        tags.remove(Objects.requireNonNull(tag, ERROR_MESSAGE_NULL_TAG));
    }
}