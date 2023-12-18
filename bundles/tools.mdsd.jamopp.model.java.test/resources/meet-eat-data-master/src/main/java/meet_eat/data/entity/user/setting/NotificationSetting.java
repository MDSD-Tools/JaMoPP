package meet_eat.data.entity.user.setting;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import meet_eat.data.entity.user.User;

import java.util.Objects;

/**
 * Represents the notification settings that can be changed by the {@link User}.
 */
public class NotificationSetting implements Setting {

    private static final String ERROR_MESSAGE_NOT_IMPLEMENTED = "This function is not implemented yet.";

    private static final String ERROR_MESSAGE_NEGATIVE_MINUTES = "Negative values are not allowed.";

    private static final boolean NOTIFICATION_DEFAULT = true;
    private static final int MINUTES_UNTIL_OFFER_DEFAULT = 60;

    @JsonProperty
    private boolean enabled;
    @JsonProperty
    private int minutesUntilOffer;

    /**
     * Creates a default notification setting.
     */
    public NotificationSetting() {
        this.enabled = NOTIFICATION_DEFAULT;
        this.minutesUntilOffer = MINUTES_UNTIL_OFFER_DEFAULT;
    }

    /**
     * Creates a notification setting.
     *
     * @param enabled           the indicator if the {@link User} wants to be notified or not
     * @param minutesUntilOffer the number of minutes to be notified before an offer takes place
     *                          in which the user has registered to participate
     */
    @JsonCreator
    public NotificationSetting(@JsonProperty("enabled") boolean enabled,
                               @JsonProperty("minutesUntilOffer") int minutesUntilOffer) {
        this.enabled = enabled;
        if (minutesUntilOffer < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NEGATIVE_MINUTES);
        }
        this.minutesUntilOffer = minutesUntilOffer;
    }

    /**
     * Gets the notification status.
     *
     * @return {@code true} if the notification settings are switched on, {@code false} otherwise
     */
    @JsonGetter
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Gets the number of minutes to be notified before an offer takes place.
     *
     * @return the number of minutes until an offer takes place
     */
    @JsonGetter
    public int getMinutesUntilOffer() {
        return minutesUntilOffer;
    }

    /**
     * Sets the notification status.
     *
     * @param enabled the indicator if notifications are switched on
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Sets the number of minutes until the offer takes place.
     *
     * @param minutesUntilOffer the number of minutes
     */
    public void setMinutesUntilOffer(int minutesUntilOffer) {
        if (minutesUntilOffer < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NEGATIVE_MINUTES);
        }
        this.minutesUntilOffer = minutesUntilOffer;
    }

    @Override
    public void apply() {
        throw new UnsupportedOperationException(ERROR_MESSAGE_NOT_IMPLEMENTED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NotificationSetting that = (NotificationSetting) o;
        return enabled == that.enabled &&
                minutesUntilOffer == that.minutesUntilOffer;
    }

    @Override
    public int hashCode() {
        return Objects.hash(enabled, minutesUntilOffer);
    }
}