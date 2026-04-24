package com.framework.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.framework.dto.request.apirestful.Objects;
import com.framework.dto.request.apirestful.ObjectsData;
import com.framework.dto.request.gorest.POSTUsersReq;
import com.framework.dto.request.other.*;
import com.github.javafaker.Faker;

import java.util.Map;


public class TestDataBuilder {

    private static final Faker        faker        = new Faker();
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private TestDataBuilder() {}

    // ── Post Service ──────────────────────────────────────────────────────────

    public static PostRequest buildPostRequest() {
        return PostRequest.builder()
                .title(faker.lorem().sentence(4))
                .body(faker.lorem().paragraph(2))
                .userId(faker.number().numberBetween(1, 10))
                .build();
    }

    // ── User Service ──────────────────────────────────────────────────────────

    public static UserRequest buildUserRequest() {
        return UserRequest.builder()
                .name(faker.name().fullName())
                .username(faker.name().username())
                .email(faker.internet().emailAddress())
                .phone(faker.phoneNumber().phoneNumber())
                .build();
    }

    // ── Comment Service ───────────────────────────────────────────────────────

    public static CommentRequest buildCommentRequest() {
        return CommentRequest.builder()
                .postId(faker.number().numberBetween(1, 100))
                .name(faker.lorem().sentence(3))
                .email(faker.internet().emailAddress())
                .body(faker.lorem().paragraph(1))
                .build();
    }

    // ── Album Service ─────────────────────────────────────────────────────────

    public static AlbumRequest buildAlbumRequest() {
        return AlbumRequest.builder()
                .userId(faker.number().numberBetween(1, 10))
                .title(faker.book().title())
                .build();
    }

//    =====API Restful =============================================================================================================

    public static Objects.ObjectsBuilder buildObjectRequest(){
        return  Objects.builder()
                .name(faker.funnyName().name())
                .data(ObjectsData.builder()
                        .year(faker.number().numberBetween(2020,2050))
                        .price(faker.number().randomDigitNotZero())
                        .cpu_model("Intel Core i9")
                        .hard_disk_size("2 TB")
                        .build());
    }

    public static Objects.ObjectsBuilder buildObjectPutRequest(){
        return  Objects.builder()
                .name(faker.funnyName().name())
                .data(ObjectsData.builder()
                        .year(faker.number().numberBetween(2020,2050))
                        .price(faker.number().randomDigitNotZero())
                        .cpu_model("Intel Core i9")
                        .hard_disk_size("2 TB")
                        .build());
    }

    public static POSTUsersReq.POSTUsersReqBuilder postUsersReqBuilder(){
        return  POSTUsersReq.builder().status("active").name(faker.funnyName().name()).email(faker.internet().emailAddress())
                .gender("male");
    }

//    =====TODO =============================================================================================================

    public static TodoRequest buildTodoRequest() {
        return TodoRequest.builder()
                .userId(faker.number().numberBetween(1, 10))
                .title(faker.lorem().sentence(5))
                .completed(false)
                .build();
    }

    // ── Dynamic PATCH / PUT Builder ───────────────────────────────────────────

    /**
     * Builds a partial request DTO (for PATCH or PUT) from any Map of fields.
     *
     * How it works:
     *   1. Accepts the target DTO class and a Map of field → value pairs
     *   2. Jackson's convertValue maps the fields directly onto the DTO
     *   3. Fields NOT present in the Map remain null on the DTO
     *   4. @JsonInclude(NON_NULL) on the DTO ensures null fields are omitted
     *      from the JSON payload — so only the supplied fields are sent
     *
     * Examples:
     *
     *   // PATCH only the title of a post
     *   PostRequest req = TestDataBuilder.buildPatchRequest(
     *       PostRequest.class,
     *       Map.of("title", "UPDATED - Breaking news")
     *   );
     *   // serialises to → { "title": "UPDATED - Breaking news" }
     *
     *   // PATCH only the body of a comment
     *   CommentRequest req = TestDataBuilder.buildPatchRequest(
     *       CommentRequest.class,
     *       Map.of("body", "PATCHED - New comment body")
     *   );
     *   // serialises to → { "body": "PATCHED - New comment body" }
     *
     *   // PATCH multiple fields at once on a todo
     *   TodoRequest req = TestDataBuilder.buildPatchRequest(
     *       TodoRequest.class,
     *       Map.of("title", "Updated task", "completed", true)
     *   );
     *   // serialises to → { "title": "Updated task", "completed": true }
     *
     * @param dtoClass the request DTO class to build (PostRequest.class, etc.)
     * @param fields   Map of field names to values — only these are set on the DTO
     * @param <T>      the DTO type
     * @return a typed DTO with only the supplied fields populated
     */
    public static <T> T buildPatchRequest(Class<T> dtoClass, Map<String, Object> fields) {
        return objectMapper.convertValue(fields, dtoClass);
    }

    // ── Generic Helpers ───────────────────────────────────────────────────────

    public static String randomTitle()    { return faker.lorem().sentence(4); }
    public static String randomBody()     { return faker.lorem().paragraph(2); }
    public static String randomEmail()    { return faker.internet().emailAddress(); }
    public static int    randomUserId()   { return faker.number().numberBetween(1, 10); }
    public static int    randomPostId()   { return faker.number().numberBetween(1, 100); }
}