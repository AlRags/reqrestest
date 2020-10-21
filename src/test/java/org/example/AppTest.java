package org.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertTrue;


public class AppTest {
    private final RequestSpecification requestSpecification;
    private final ResponseSpecification responseSpecification;

    public AppTest() {

        final String baseUrl = "https://reqres.in";

        requestSpecification = new RequestSpecBuilder()
                .setBaseUri(baseUrl)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        responseSpecification = new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .setDefaultParser(Parser.JSON)
                .log(LogDetail.ALL)
                .build();
    }

    /*
    Test case ID: 001
     */
    @Test
    public void testRegisterWithValidParams() {
        final String relativeUrl = "/api/register";
        Map<String, Object> params = new HashMap<>();
        params.put("email", "eve.holt@reqres.in");
        params.put("password", "pistol");

        given()
                .spec(requestSpecification)
                .body(params)
                .post(relativeUrl)
                .then()
                .spec(responseSpecification)
                .statusCode(200);
    }

    /*
    Test case ID: 002
     */
    @Test
    public void testRegisterWithValidParams_shouldReturnToken() {
        final String relativeUrl = "/api/register";
        Map<String, Object> params = new HashMap<>();
        params.put("email", "eve.holt@reqres.in");
        params.put("password", "pistol");

        given()
                .spec(requestSpecification)
                .body(params)
                .post(relativeUrl)
                .then()
                .spec(responseSpecification)
                .body("id", notNullValue())
                .body("token", notNullValue());
    }

    /*
    Test case ID: 003
     */
    @Test
    public void testRegisterWithoutPassword_shouldReturnError() {
        final String relativeUrl = "/api/register";
        Map<String, Object> params = new HashMap<>();
        params.put("email", "eve.holt@reqres.in");

        given()
                .spec(requestSpecification)
                .body(params)
                .post(relativeUrl)
                .then()
                .spec(responseSpecification)
                .statusCode(400)
                .and()
                .body("error", containsString("Missing password"));
    }

    /*
    Test case ID: 004
     */
    @Test
    public void testLoginWithValidParams() {
        final String relativeUrl = "/api/login";
        Map<String, Object> params = new HashMap<>();
        params.put("email", "eve.holt@reqres.in");
        params.put("password", "cityslicka");

        given()
                .spec(requestSpecification)
                .body(params)
                .post(relativeUrl)
                .then()
                .spec(responseSpecification)
                .statusCode(200);
    }

    /*
    Test case ID: 005
     */
    @Test
    public void testLoginWithValidParams_shouldReturnToken() {
        final String relativeUrl = "/api/login";
        Map<String, Object> params = new HashMap<>();
        params.put("email", "eve.holt@reqres.in");
        params.put("password", "cityslicka");

        given()
                .spec(requestSpecification)
                .body(params)
                .post(relativeUrl)
                .then()
                .spec(responseSpecification)
                .body("token", notNullValue());
    }

    /*
    Test case ID: 006
     */
    @Test
    public void testLoginWithoutPassword_shouldReturnError() {
        final String relativeUrl = "/api/login";
        Map<String, Object> params = new HashMap<>();
        params.put("email", "eve.holt@reqres.in");

        given()
                .spec(requestSpecification)
                .body(params)
                .post(relativeUrl)
                .then()
                .spec(responseSpecification)
                .statusCode(400)
                .and()
                .body("error", containsString("Missing password"));
    }

    /*
    Test case ID: 007
     */
    @Test
    public void testCreate_shouldReturnUserInfo() {
        final String relativeUrl = "/api/users";
        Map<String, Object> params = new HashMap<>();
        params.put("name", "jack");
        params.put("job", "leader");

        given()
                .spec(requestSpecification)
                .body(params)
                .post(relativeUrl)
                .then()
                .spec(responseSpecification)
                .statusCode(201)
                .and()
                .body("name", equalToObject(params.get("name")))
                .body("job", equalToObject(params.get("job")))
                .body("id", notNullValue())
                .body("createdAt", notNullValue());
    }

    /*
    Test case ID: 008
     */
    @Test
    public void testGetListOfResource() {
        final String relativeUrl = "/api/unknown";

        Response response = given()
                .spec(requestSpecification)
                .when()
                .get(relativeUrl)
                .then()
                .spec(responseSpecification)
                .extract().response();
        final List<Map<String, Object>> resources = response.jsonPath().getList("data");
        assertTrue(resources.size() > 0);
        assertTrue(resources.get(0).containsKey("id"));
        assertTrue(resources.get(0).containsKey("name"));
    }
}
