import config.TestConfig;
import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.*;

public class GPathJSONTests extends TestConfig {

    //30
    @Test
    public void extractMapOfElementsWithFind() {

        Response response =
                given().
                        spec(footballRequestSpec).
                when().get("competitions/2021/teams");

        Map<String,?> allTeamDataForSingleTeam = response.path
                ("teams.find { it.name == 'Leicester City FC' }");

        System.out.println(allTeamDataForSingleTeam);
    }

    //31
    @Test
    public void extractSingleValueWithFind() {

        Response response =
                given().
                        spec(footballRequestSpec).
                when().get("teams/58");

        String certainPlayer = response.path("squad.find { it.id  == 3547}.name");
        System.out.println(certainPlayer);
    }

    @Test
    public void extractListOfValuesWithFindAll() {

        Response response =
                given().
                        spec(footballRequestSpec).
                when().
                        get("teams/58");

        List<String> playerNames = response.path("squad.findAll { it.id > 5988}.name");
        System.out.println(playerNames);
    }

    //32
    @Test
    public void extractSingleValueWithHighestNumber() {

        Response response =
                given().
                        spec(footballRequestSpec).
                when().
                        get("teams/57");

        String playerName = response.path("squad.max { it.shirtNumber }.name");
        System.out.println(playerName);
    }

    @Test
    public void extractMultipleValuesAndSumThem() {

        Response response =
                given().
                        spec(footballRequestSpec).
                when().
                        get("teams/57");

        int sumOfIds = response.path("squad.collect { it.id }.sum()");
        System.out.println(sumOfIds);
    }

    //33
    @Test
    public void extractMapOfObjectWithFindAndFindAll() {

        String position = "Defender";
        String nationality = "Greece";

        Response response =
                given().
                        spec(footballRequestSpec).
                when().
                        get("teams/57");

        Map<String,?> playerOfCertainPosition = response.path(
                "squad.findAll { it.position == '%s' }.find { it.nationality == '%s' }",
                position, nationality);

        System.out.println(playerOfCertainPosition);
    }

    @Test
    public void extractMultiplePlayers() {

        String position = "Midfielder";
        String nationality = "England";

        Response response =
                given().
                        spec(footballRequestSpec).
                when().
                        get("teams/57");

        ArrayList<Map<String,?>> allPlayersCertainNation = response.path(
                "squad.findAll { it.position == '%s' }.findAll { it.nationality == '%s' }",
                position, nationality);

        System.out.println(allPlayersCertainNation);

    }


}