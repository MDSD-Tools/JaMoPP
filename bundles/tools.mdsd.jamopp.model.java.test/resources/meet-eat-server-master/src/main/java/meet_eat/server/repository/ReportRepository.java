package meet_eat.server.repository;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.user.User;
import org.springframework.stereotype.Repository;

/**
 * Represents a repository managing persistence of {@link Report} instances.
 */
@Repository
public interface ReportRepository extends EntityRelationRepository<Report, User, Entity<?>, String> {

}
