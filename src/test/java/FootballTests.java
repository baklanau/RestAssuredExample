import config.TestConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

public class FootballTests extends TestConfig {

    @Test
    public void getDetailsOfOneArea() {

        //http://api.football-data.org/v2/areas/?areas=2072

        given().
                spec(footballRequestSpec).
                queryParam("areas", 2072).
                log().
                all().
        when().
                get("areas/").
        then().
                log().
                all();
    }

    @Test
    public void getDateFounded() {
        //http://api.football-data.org/v2/teams/57
        given().
                spec(footballRequestSpec).
        when().
                get("teams/57").
        then().
                body("founded", equalTo(1886));
    }

    @Test
    public void getFirstTeamName() {
        //http://api.football-data.org/v2/competitions/2021/teams

        given().
                spec(footballRequestSpec).
        when().
                get("competitions/2021/teams").
        then().
                body("teams.name[0]", equalTo("Arsenal FC"));
    }

    @Test
    public void getAllTeamData() {
        //http://api.football-data.org/v2/teams/57

        String responseBody =
                given().
                        spec(footballRequestSpec).
                when().
                        get("teams/57").asString();

        System.out.println(responseBody);
    }

    @Test
    public void getAllTeamData_DoCheckFirst() {
        //http://api.football-data.org/v2/teams/57

        Response response =
                given().
                        spec(footballRequestSpec).
                when().
                        get("teams/57").
                then().
                        contentType(ContentType.JSON).
                        extract().response();

        String jsonResponseAsString = response.asString();

        System.out.println(jsonResponseAsString);

    }

    @Test
    public void extractHeaders() {
        //http://api.football-data.org/v2/teams/57

        Response response =
                given().
                        spec(footballRequestSpec).
                when().
                        get("teams/57").
                then().
                        contentType(ContentType.JSON).
                        extract().response();

        Headers headers = response.getHeaders();

        List<Header> listHeaders = headers.asList();
        System.out.println(listHeaders.get(1));

        String contentType = response.getHeader("Content-Type");

        System.out.println(contentType);
    }


    @Test
    public void extractFirstTeamName() {

        String firstTeamName =
                given().
                        spec(footballRequestSpec).
                when().
                        get("competitions/2021/teams").jsonPath().getString("teams.name[0]");

        System.out.println(firstTeamName);
    }

    @Test
    public void extractAllTeamNames() {

        Response response =
                given().
                        spec(footballRequestSpec).
                when().
                        get("competitions/2021/teams").
                then().
                        contentType(ContentType.JSON).
                        extract().response();

        List<String> teamNames = response.path("teams.name");

        for(String teamName: teamNames) {
            System.out.println(teamName);
        }
    }


}
