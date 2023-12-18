package meet_eat.data.entity.relation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Reportable;
import meet_eat.data.entity.user.User;
import org.springframework.data.annotation.PersistenceConstructor;

import java.util.Objects;

/**
 * Represents a report of a {@link Reportable} {@link Entity}.
 * Consists of a {@link User reporter} and an {@link Entity} that should be reported.
 */
public class Report extends EntityRelation<User, Entity<?>, String> {

    private static final long serialVersionUID = -3131866887183153010L;

    @JsonProperty
    private final String message;
    @JsonProperty
    private boolean processed;

    /**
     * Constructs a new instance of {@link Report}.
     *
     * @param source  the {@link User reporter} of this report
     * @param target  the {@link Entity} to be reported
     * @param message the message describing the report's circumstances
     * @param <T>     the type of the target entity
     */
    public <T extends Entity<?> & Reportable> Report(User source, T target, String message) {
        super(source, target);
        this.message = Objects.requireNonNull(message);
    }

    /**
     * Constructs a new instance of {@link Report}.
     *
     * @param identifier the identifier of this report
     * @param source     the {@link User reporter} of this report
     * @param target     the {@link Entity} to be reported
     * @param message    the message describing the report's circumstances
     * @param processed  the processing state of this report
     * @param <T>        the type of the target entity
     */
    @JsonCreator
    @PersistenceConstructor
    protected <T extends Entity<?> & Reportable> Report(@JsonProperty("identifier") String identifier,
                                                        @JsonProperty("source") User source,
                                                        @JsonProperty("target") T target,
                                                        @JsonProperty("message") String message,
                                                        @JsonProperty("processed") boolean processed) {
        super(identifier, source, target);
        this.message = message;
        this.processed = processed;
    }

    /**
     * Gets the message of this report.
     *
     * @return the message of this report
     */
    @JsonGetter
    public String getMessage() {
        return message;
    }

    /**
     * Gets the report processed status.
     *
     * @return {@code true} if the report is processed, {@code false} otherwise
     */
    @JsonGetter
    public boolean isProcessed() {
        return processed;
    }

    /**
     * Sets the processed status.
     *
     * @param processed indicator, if a {@link Report} has been processed or not
     */
    public void setProcessed(boolean processed) {
        this.processed = processed;
    }
}