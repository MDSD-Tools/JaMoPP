package meet_eat.server.service;

import meet_eat.data.entity.Tag;
import org.junit.Test;

public class TagServiceTest extends EntityServiceTest<TagService, Tag, String> {

    private static int tagCount = 0;

    @Override
    protected Tag createDistinctTestEntity() {
        return new Tag("TestTag" + tagCount++);
    }

    @Test(expected = EntityConflictException.class)
    public void testPostIdConflict() {
        // Test data
        Tag tagFst = new Tag("1A", "TestTag1");
        Tag tagSnd = new Tag("1A", "TestTag2");

        // Execution
        Tag postedTagFst = getEntityService().post(tagFst);
        Tag postedTagSnd = getEntityService().post(tagSnd);
    }
}
