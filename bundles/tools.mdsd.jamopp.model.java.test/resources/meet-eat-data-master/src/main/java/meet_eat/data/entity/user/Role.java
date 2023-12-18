package meet_eat.data.entity.user;

/**
 * Represents the different roles that assign administrative rights to a {@link User}.
 */
public enum Role {

    /**
     * The standard user.
     */
    USER,

    /**
     * The user with moderator rights.
     */
    MODERATOR,

    /**
     * The user with administrator rights.
     */
    ADMIN
}