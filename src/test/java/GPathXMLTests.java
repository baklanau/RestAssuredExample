import config.EndPoint;
import config.TestConfig;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.element.Node;
import io.restassured.response.Response;
import org.junit.Test;
import java.util.List;

import static io.restassured.RestAssured.*;

public class GPathXMLTests extends TestConfig {

    //34
    @Test
    public void getFirstGameInList() {

        Response response = get(EndPoint.VIDEOGAMES);

        String name = response.path("videoGames.videoGame.name[1]");

        System.out.println(name);

    }

    //35
    @Test
    public void getAttributeName() {

        Response response = get(EndPoint.VIDEOGAMES);

        String category = response.path("videoGames.videoGame[0].@category");

        System.out.println(category);

    }

    //36
    @Test
    public void getListOfXMLNodes() {

        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        List<Node> allResults = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.findAll { element -> return element }"
        );

        System.out.println(allResults.get(1).get("name").toString());
    }

    //37
    @Test
    public void getListOfXMLNodesByFindAllOnAttribute() {

        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        List<Node> allDrivingGames = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.findAll { videoGame -> def category = videoGame.@category == 'Driving'}"
        );

        System.out.println(allDrivingGames.get(0).get("name").toString());

    }

    //38
    @Test
    public void getSingleNode() {

        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        Node videoGame = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.find { videoGame -> def name = videoGame.name; name == 'Tetris'}");

        String videoGameName = videoGame.get("name").toString();

        System.out.println(videoGameName);
    }

    //39
    @Test
    public void getSingleElementDepthFirst() {

        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        int reviewScore = XmlPath.from(responseAsString).getInt(
                "**.find { it.name == 'Tetris'}.reviewScore");

        System.out.println(reviewScore);
    }

    //40
    @Test
    public void getAllNodesBasedOnACondition() {

        String responseAsString = get(EndPoint.VIDEOGAMES).asString();

        int reviewScore = 90;

        List<Node> allVideoGamesOverCertainScore = XmlPath.from(responseAsString).get(
                "videoGames.videoGame.findAll { it.reviewScore.toFloat() >= " + reviewScore + "}");

        System.out.println(allVideoGamesOverCertainScore);
    }


}
