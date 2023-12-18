package meet_eat.data.entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import meet_eat.data.entity.user.User;

/**
 * Serves as marker interface for reportable constructs.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
@JsonSubTypes({
        @JsonSubTypes.Type(value = User.class),
        @JsonSubTypes.Type(value = Offer.class)
})
public interface Reportable {

}
