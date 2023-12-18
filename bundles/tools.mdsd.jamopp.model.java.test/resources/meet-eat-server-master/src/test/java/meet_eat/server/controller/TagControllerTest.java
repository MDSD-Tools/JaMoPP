package meet_eat.server.controller;

import meet_eat.data.entity.Tag;
import org.junit.Test;

public class TagControllerTest extends EntityControllerTest<TagController, Tag, String> {

    //#region @Test @RequestMapping

    @Test
    public void testGetTag() {
        createHandleGetEndpointTest(getEntityController()::getTag);
    }

    @Test
    public void testGetAllTags() {
        createHandleGetAllEndpointTest(getEntityController()::getAllTags);
    }

    @Test
    public void testPostTag() {
        createHandlePostEndpointTest(getEntityController()::postTag);
    }

    @Test
    public void testPutTagWithIdentifier() {
        createHandlePutEndpointTest(i -> (e -> (t -> getEntityController().putTag(i, e, t))));
    }

    @Test
    public void testPutTagWithoutIdentifier() {
        createHandlePutEndpointTest(i -> (e -> (t -> getEntityController().putTag(e, t))));
    }

    @Test
    public void testDeleteTagByIdentifier() {
        createHandleDeleteByIdentifierEndpointTest(getEntityController()::deleteTag);
    }

    @Test
    public void testDeleteTagByEntity() {
        createHandleDeleteByEntityEndpointTest(getEntityController()::deleteTag);
    }

    //#endregion

    @Override
    protected Tag getTestEntityTransient() {
        return getTagTransient();
    }

    @Override
    protected Tag getTestEntityPersistent() {
        return getTagPersistent();
    }

    @Override
    protected String getTestIdentifierInvalid() {
        return "InvalidTagIdentifier";
    }
}
