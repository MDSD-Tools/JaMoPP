package meet_eat.server.controller;

import com.google.common.collect.Iterables;
import meet_eat.data.LoginCredential;
import meet_eat.data.entity.Entity;
import meet_eat.data.entity.Offer;
import meet_eat.data.entity.Tag;
import meet_eat.data.entity.Token;
import meet_eat.data.entity.user.Email;
import meet_eat.data.entity.user.Password;
import meet_eat.data.entity.user.Role;
import meet_eat.data.entity.user.User;
import meet_eat.data.location.CityLocation;
import meet_eat.data.location.Localizable;
import meet_eat.data.location.SphericalLocation;
import meet_eat.data.location.SphericalPosition;
import meet_eat.server.service.OfferService;
import meet_eat.server.service.TagService;
import meet_eat.server.service.TokenService;
import meet_eat.server.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.WebDataBinder;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class EntityControllerTest<C extends EntityController<T, U, ?>, T extends Entity<U>, U extends Serializable> {

    protected static final String PASSWORD_VALID_VALUE = "AbcdefgTest1234!?";

    private static int userCount = 0;
    private static int offerCount = 0;
    private static int tagCount = 0;

    @Autowired
    private UserService userService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private C entityController;

    @Before
    public void prepareTestEnvironment() {
        entityController.getEntityService().getRepository().deleteAll();
    }

    @Before
    public void prepareNonRelationRepositories() {
        userService.getRepository().deleteAll();
        offerService.getRepository().deleteAll();
        tagService.getRepository().deleteAll();
        tokenService.getRepository().deleteAll();
    }

    //#region @Test handleGet

    @Test
    public void testHandleGetNullIdentifier() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handleGet(null, token);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleGetNullToken() {
        // Test data
        T entity = getTestEntityPersistent();

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handleGet(entity.getIdentifier(), null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleGetNullTokenAndNullIdentifier() {
        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handleGet(null, null);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleGetInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityPersistent();

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handleGet(entity.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleGetUnknownIdentifier() {
        // Test data
        U unknownIdentifier = getTestIdentifierInvalid();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handleGet(unknownIdentifier, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleGetExistingIdentifier() {
        // Test data
        T entity = getTestEntityPersistent();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handleGet(entity.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseEntity.getBody(), entity);
    }

    //#endregion

    //#region @Test handleGetAll

    @Test
    public void testHandleGetAllNullToken() {
        // Execution
        ResponseEntity<Iterable<T>> responseEntity = getEntityController().handleGetAll(null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleGetAllInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<Iterable<T>> responseEntity = getEntityController().handleGetAll(token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleGetAllMultipleElements() {
        // Test data
        T entityFst = getTestEntityPersistent();
        T entitySnd = getTestEntityPersistent();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<Iterable<T>> responseEntity = getEntityController().handleGetAll(token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(Iterables.contains(responseEntity.getBody(), entityFst));
        assertTrue(Iterables.contains(responseEntity.getBody(), entitySnd));
    }

    //#endregion

    //#region @Test handlePost

    @Test
    public void testHandlePostNullEntity() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePost(null, token);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePostNullToken() {
        // Test data
        T entity = getTestEntityTransient();

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePost(entity, null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePostNullTokenAndNullEntity() {
        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePost(null, null);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePostInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityTransient();

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePost(entity, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePostEntityConflict() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityTransient();

        // Execution
        ResponseEntity<T> responseEntityValid = getEntityController().handlePost(entity, token);
        ResponseEntity<T> responseEntityConflict = getEntityController().handlePost(responseEntityValid.getBody(), token);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntityValid.getStatusCode());
        assertEquals(HttpStatus.CONFLICT, responseEntityConflict.getStatusCode());
    }

    @Test
    public void testHandlePostSingleEntity() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityTransient();

        // Execution
        ResponseEntity<T> responseEntityValid = getEntityController().handlePost(entity, token);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntityValid.getStatusCode());
    }

    @Test
    public void testHandlePostMultipleEntities() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        int entityAmount = 4;
        List<T> entities = new LinkedList<>();
        repeat(entityAmount, i -> entities.add(getTestEntityTransient()));

        // Execution
        List<ResponseEntity<T>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handlePost(entities.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.CREATED, responses.get(i).getStatusCode()));
    }

    //#endregion

    //#region @Test handlePut

    @Test
    public void testHandlePutNullIdentifier() {
        // Test data
        T entity = getTestEntityPersistent();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePut(null, entity, token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePutInvalidIdentifier() {
        // Test data
        T entity = getTestEntityPersistent();
        U identifier = getTestIdentifierInvalid();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePut(identifier, entity, token);

        // Assertions
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePutNullEntity() {
        // Test data
        T entity = getTestEntityPersistent();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePut(entity.getIdentifier(), null, token);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePutNullToken() {
        // Test data
        T entity = getTestEntityPersistent();

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePut(entity.getIdentifier(), entity, null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePutNullTokenAndNullIdentifier() {
        // Test data
        T entity = getTestEntityPersistent();

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePut(entity.getIdentifier(), null, null);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePutInvalidToken() {
        // Test data
        T entity = getTestEntityPersistent();
        Token token = getTokenInvalid(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<T> responseEntity = getEntityController().handlePut(entity.getIdentifier(), entity, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testHandlePutUnknownEntity() {
        // Test data
        T entity = getTestEntityPersistent();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        getEntityController().getEntityService().delete(entity);
        ResponseEntity<T> responseEntity = getEntityController().handlePut(entity.getIdentifier(), entity, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    //#endregion

    //#region @Test handleDelete

    @Test
    public void testHandleDeleteNullEntity() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete((T) null, token);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteNullIdentifier() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete((U) null, token);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteEntityNullToken() {
        // Test data
        T entity = getTestEntityPersistent();

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(entity, null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteIdentifierNullToken() {
        // Test data
        T entity = getTestEntityPersistent();

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(entity.getIdentifier(), null);

        // Assertions
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteNullTokenAndNullEntity() {
        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete((T) null, null);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteNullTokenAndNullIdentifier() {
        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete((U) null, null);

        // Assertions
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteEntityInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityPersistent();

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(entity, token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteIdentifierInvalidToken() {
        // Test data
        Token token = getTokenInvalid(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityPersistent();

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(entity.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.FORBIDDEN, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteUnknownIdentifier() {
        // Test data
        U unknownIdentifier = getTestIdentifierInvalid();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(unknownIdentifier, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteUnknownEntity() {
        // Test data
        T entity = getTestEntityPersistent();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        getEntityController().handleDelete(entity, token);
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(entity, token);

        // Assertions
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteExistingIdentifier() {
        // Test data
        T entity = getTestEntityPersistent();
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(entity.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteMultipleIdentifiers() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        int entityAmount = 4;
        List<T> entities = new LinkedList<>();
        repeat(entityAmount, i -> entities.add(getTestEntityPersistent()));

        // Execution
        List<ResponseEntity<Void>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handleDelete(entities.get(i).getIdentifier(), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.NO_CONTENT, responses.get(i).getStatusCode()));
    }

    @Test
    public void testHandleDeleteSingleEntity() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityPersistent();

        // Execution
        ResponseEntity<Void> responseEntity = getEntityController().handleDelete(entity, token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    public void testHandleDeleteMultipleEntities() {
        // Test data
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        int entityAmount = 4;
        List<T> entities = new LinkedList<>();
        repeat(entityAmount, i -> entities.add(getTestEntityPersistent()));

        // Execution
        List<ResponseEntity<Void>> responses = new LinkedList<>();
        repeat(entityAmount, i -> responses.add(i, getEntityController().handleDelete(entities.get(i), token)));

        // Assertions
        repeat(entityAmount, i -> assertEquals(HttpStatus.NO_CONTENT, responses.get(i).getStatusCode()));
    }

    //#endregion

    //#region @Test getEntityService

    @Test
    public void testGetEntityServiceNotNull() {
        // Assertions
        assertNotNull(getEntityController().getEntityService());
    }

    //#endregion

    //#region @Test getSecurityService

    @Test
    public void testGetSecurityServiceNotNull() {
        // Assertions
        assertNotNull(getEntityController().getSecurityService());
    }

    //#endregion

    //#region @Test initBinder

    @Test(expected = NullPointerException.class)
    public void testInitBinderNull() {
        // Assertions
        getEntityController().initBinder(null);
    }

    @Test
    public void testInitBinder() {
        // Test data
        WebDataBinder webDataBinder = new WebDataBinder(null);

        // Execution
        getEntityController().initBinder(webDataBinder);

        // Assertions
        assertNotNull(webDataBinder);
    }

    //#endregion

    //#region Abstract endpoint test frames

    // Get(all)

    protected void createHandleGetAllEndpointTest(Function<Token, ResponseEntity<Iterable<T>>> function) {
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        createHandleGetAllEndpointTest(function, token);
    }

    protected void createHandleGetAllEndpointTest(Function<Token, ResponseEntity<Iterable<T>>> function, Token token) {
        // Test data
        int entityAmount = 4;
        List<T> entities = new LinkedList<>();
        repeat(entityAmount, i -> entities.add(getTestEntityPersistent()));

        // Execution
        ResponseEntity<Iterable<T>> responseEntity = function.apply(token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        repeat(entityAmount, i -> assertTrue(Iterables.contains(responseEntity.getBody(), entities.get(i))));
    }

    // Get

    protected void createHandleGetEndpointTest(BiFunction<U, Token, ResponseEntity<T>> function) {
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityPersistent();
        createHandleGetEndpointTest(function, entity, token);
    }

    protected void createHandleGetEndpointTest(BiFunction<U, Token, ResponseEntity<T>> function, T persistentEntity, Token token) {
        // Execution
        ResponseEntity<T> responseEntity = function.apply(persistentEntity.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals(persistentEntity, responseEntity.getBody());
    }

    // Delete

    protected void createHandleDeleteByIdentifierEndpointTest(BiFunction<U, Token, ResponseEntity<Void>> function) {
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityPersistent();
        createHandleDeleteByIdentifierEndpointTest(function, entity, token);
    }

    protected void createHandleDeleteByIdentifierEndpointTest(BiFunction<U, Token, ResponseEntity<Void>> function, T persistentEntity, Token token) {
        // Execution
        ResponseEntity<Void> responseEntity = function.apply(persistentEntity.getIdentifier(), token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertFalse(getEntityController().getEntityService().exists(persistentEntity.getIdentifier()));
    }

    protected void createHandleDeleteByEntityEndpointTest(BiFunction<T, Token, ResponseEntity<Void>> function) {
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityPersistent();
        createHandleDeleteByEntityEndpointTest(function, entity, token);
    }

    protected void createHandleDeleteByEntityEndpointTest(BiFunction<T, Token, ResponseEntity<Void>> function, T persistentEntity, Token token) {
        // Execution
        ResponseEntity<Void> responseEntity = function.apply(persistentEntity, token);

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertFalse(getEntityController().getEntityService().exists(persistentEntity.getIdentifier()));
    }

    // Post

    protected void createHandlePostEndpointTest(BiFunction<T, Token, ResponseEntity<T>> function) {
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityTransient();
        createHandlePostEndpointTest(function, entity, token);
    }

    protected void createHandlePostEndpointTest(BiFunction<T, Token, ResponseEntity<T>> function, T transientEntity, Token token) {
        // Execution
        ResponseEntity<T> responseEntity = function.apply(transientEntity, token);

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertTrue(getEntityController().getEntityService().exists(responseEntity.getBody().getIdentifier()));
    }

    // Put

    protected void createHandlePutEndpointTest(Function<U, Function<T, Function<Token, ResponseEntity<T>>>> function) {
        Token token = getTokenPersistent(getUserPersistent(Role.ADMIN));
        T entity = getTestEntityPersistent();
        createHandlePutEndpointTest(function, entity.getIdentifier(), entity, token);
    }

    protected void createHandlePutEndpointTest(Function<U, Function<T, Function<Token, ResponseEntity<T>>>> function, U identifier, T persistentEntity, Token token) {
        // Execution
        ResponseEntity<T> responseEntity = function.apply(identifier).apply(persistentEntity).apply(token);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(getEntityController().getEntityService().exists(persistentEntity.getIdentifier()));
    }

    //#endregion

    //#region Test environment utility

    protected C getEntityController() {
        return entityController;
    }

    /**
     * Creates and returns a new distinct entity.
     * Guarantees that returned entities do not conflict with each other.
     * Guarantees that multiple calls always return entities that are not equal according to the equals implementation.
     *
     * @return A new distinct entity.
     */
    protected abstract T getTestEntityTransient();

    protected abstract T getTestEntityPersistent();

    protected abstract U getTestIdentifierInvalid();

    protected Offer getOfferTransient(User creator) {
        LocalDateTime dateTime = LocalDateTime.of(2020, Month.JULY, 30, 12, 32);
        Localizable location = new CityLocation("Karlsruhe");
        Set<Tag> tags = new HashSet<>();
        return new Offer(creator, tags, "Offer " + offerCount++,
                "Spaghetti. Mhmmm.", 4.99, 5, dateTime, location);
    }

    protected Offer getOfferPersistent(User creator) {
        Offer transientOffer = getOfferTransient(creator);
        return offerService.post(transientOffer);
    }

    protected User getUserTransient(Role role) {
        Email email = new Email("noreply" + userCount + ".meet.eat@example.com");
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        Localizable validLocalizable = new SphericalLocation(new SphericalPosition(0, 0));
        User user = new User(email, password, LocalDate.EPOCH, "User" + userCount, "12345" + userCount,
                "Description" + userCount, true, validLocalizable);
        user.setRole(role);
        userCount++;
        return user;
    }

    protected User getUserPersistent(Role role) {
        User transientUser = getUserTransient(role);
        return userService.post(transientUser);
    }

    protected Tag getTagTransient() {
        return new Tag("Tag" + tagCount++);
    }

    protected Tag getTagPersistent() {
        Tag transientTag = getTagTransient();
        return tagService.post(transientTag);
    }

    protected Token getTokenInvalid(User user) {
        Email email = user.getEmail();
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        Token token = tokenService.createToken(new LoginCredential(email, password));
        return new Token(token.getIdentifier(), token.getUser(), token.getValue() + "INVALID123!");
    }

    protected Token getTokenPersistent(User user) {
        Email email = user.getEmail();
        Password password = Password.createHashedPassword(PASSWORD_VALID_VALUE);
        return tokenService.createToken(new LoginCredential(email, password));
    }

    protected void repeat(int count, Consumer<Integer> consumer) {
        IntStream.range(0, count).forEach(consumer::accept);
    }

    //#endregion
}
