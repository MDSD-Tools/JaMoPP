package meet_eat.server.service;

import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Reportable;
import meet_eat.data.entity.relation.Report;
import meet_eat.data.entity.user.User;

public class ReportServiceTest extends EntityRelationServiceTest<ReportService, Report, User, Entity<?>, String> {

    @Override
    protected User getSourceEntity() {
        return getBasicUserPersistent();
    }

    @Override
    protected Entity<?> getTargetEntity() {
        return getBasicUserPersistent();
    }

    @Override
    protected Report createDistinctTestEntity(User source, Entity<?> target) {
        return new Report(source, (Entity<?> & Reportable) target, "JUnit Test Report");
    }
}
