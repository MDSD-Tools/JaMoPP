package meet_eat.server.service;

import meet_eat.data.entity.Tag;
import meet_eat.server.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Represents a service class providing functionality to manage {@link Tag tags} and their state persistence.
 */
@Service
public class TagService extends EntityService<Tag, String, TagRepository> {

    /**
     * Constructs a new instance of {@link TagService}.
     *
     * @param tagRepository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public TagService(TagRepository tagRepository) {
        super(tagRepository);
    }
}
