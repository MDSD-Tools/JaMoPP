package meet_eat.data.entity.user.setting;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import meet_eat.data.entity.user.User;

/**
 * Represents the settings that can be changed by a {@link User}.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DisplaySetting.class),
        @JsonSubTypes.Type(value = NotificationSetting.class)
})
public interface Setting {

    /**
     * Applies the settings changed by the {@link User}.
     */
    void apply();
}