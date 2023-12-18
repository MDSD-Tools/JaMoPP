package meet_eat.server.service;

import meet_eat.data.entity.Offer;
import meet_eat.data.entity.relation.Bookmark;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Represents a service class providing functionality to manage {@link Bookmark bookmarks} and their state
 * persistence.
 */
@Service
public class BookmarkService extends EntityRelationService<Bookmark, User, Offer, String, BookmarkRepository> {

    private final UserService userService;

    /**
     * Constructs a new instance of {@link BookmarkService}.
     *
     * @param repository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public BookmarkService(BookmarkRepository repository, UserService userService) {
        super(repository);
        this.userService = userService;
    }

    /**
     * Gets all {@link Bookmark bookmarks} containing a specific {@link User user} identified by
     * identifier.
     *
     * @param userIdentifier the user's identifier
     * @return all bookmarks containing a specific {@link User user}
     */
    public Optional<Iterable<Bookmark>> getByUserIdentifier(String userIdentifier) {
        Optional<User> optionalCreator = userService.get(userIdentifier);
        return optionalCreator.map(this::getBySource);
    }

    @Override
    public boolean existsPostConflict(Bookmark entity) {
        return getRepository().existsBySourceAndTarget(entity.getSource(), entity.getTarget())
                || super.existsPostConflict(entity);
    }
}
