package config;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.BeforeClass;

import static org.hamcrest.Matchers.lessThan;


public class TestConfig {
    public static RequestSpecification videoGameRequestSpec;
    public static RequestSpecification footballRequestSpec;
    public static ResponseSpecification responseSpec;

    @BeforeClass
    public static void setup() {
        //Charles should be run for PROXY
        //proxy("localhost", 8889);

//        Simple implementation for Request and Response Specifications
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = 8080;
//        RestAssured.basePath = "/app/";
//
//        RequestSpecification requestSpecification = new RequestSpecBuilder().
//                addHeader("Content-Type", "application/json").
//                addHeader("Accept", "application/json").
//                build();
//
//        RestAssured.requestSpecification = requestSpecification;
//
//        ResponseSpecification responseSpecification = new ResponseSpecBuilder().
//                expectStatusCode(200).
//                build();
//
//        RestAssured.responseSpecification = responseSpecification;

        videoGameRequestSpec = new RequestSpecBuilder().
                setBaseUri("http://localhost").
                setPort(8080).
                setBasePath("/app/").
                addHeader("Content-Type", "application/xml").
                addHeader("Accept", "application/xml").
                build();

        //multiple request and response specifications
        //may be add in Given section in a test

        footballRequestSpec = new RequestSpecBuilder().
                setBaseUri("http://api.football-data.org").
                setBasePath("/v2/").
                addHeader("X-Auth-Token", "302ffb3362e64a37805e136382545f0b").
                addHeader("X-Response-Control", "minified").
                build();

        RestAssured.requestSpecification = videoGameRequestSpec;

        responseSpec = new ResponseSpecBuilder().
                expectStatusCode(200).
                expectResponseTime(lessThan(3000L)).
                build();

        RestAssured.responseSpecification = responseSpec;


    }
}
