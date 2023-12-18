package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.user.User;
import meet_eat.server.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

/**
 * Represents a service class providing functionality to manage {@link Report reports} and their state
 * persistence.
 */
@Service
public class ReportService extends EntityRelationService<Report, User, Entity<?>, String, ReportRepository> {

    /**
     * Constructs a new instance of {@link ReportService}.
     *
     * @param repository the repository used for persistence operations
     */
    @Lazy
    @Autowired
    public ReportService(ReportRepository repository) {
        super(repository);
    }
}
