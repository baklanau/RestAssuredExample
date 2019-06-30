import config.EndPoint;
import config.TestConfig;
import io.restassured.response.Response;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.namespace.QName;

import static io.restassured.RestAssured.given;
import static io.restassured.matcher.RestAssuredMatchers.matchesXsdInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;

public class VideoGameDBTests extends TestConfig {

    @Test
    public void getAllGames() {
        given().
        when().
                get(EndPoint.VIDEOGAMES).
        then().
                log().
                all();
    }

    @Test
    public void createNewGameByJSON() {
        String gameBodyJSON = "{\n" +
                "  \"id\": 11,\n" +
                "  \"name\": \"MyNewGame\",\n" +
                "  \"releaseDate\": \"2019-06-24T18:37:36.835Z\",\n" +
                "  \"reviewScore\": 0,\n" +
                "  \"category\": \"string\",\n" +
                "  \"rating\": \"string\"\n" +
                "}";

        given().
                body(gameBodyJSON).
        when().
                post(EndPoint.VIDEOGAMES).
        then().
                log().
                all();

    }

    @Test
    public void createNewGameByXML() {
        String gameBodyXML = "<videoGame category=\"string\" rating=\"string\">\n" +
                "    <id>12</id>\n" +
                "    <name>MyNewGame</name>\n" +
                "    <releaseDate>2019-06-24T00:00:00+03:00</releaseDate>\n" +
                "    <reviewScore>0</reviewScore>\n" +
                "  </videoGame>";

        given().
                body(gameBodyXML).
        when().
                post(EndPoint.VIDEOGAMES).
        then().
                log().
                all();
     }

    @Test
    public void updateGame() {
         String gameBodyJSON = "{\n" +
                 "  \"id\": 15,\n" +
                 "  \"name\": \"MyUpdatedGame\",\n" +
                 "  \"releaseDate\": \"2019-06-24T18:37:36.835Z\",\n" +
                 "  \"reviewScore\": 88,\n" +
                 "  \"category\": \"string\",\n" +
                 "  \"rating\": \"string\"\n" +
                 "}";

         given().
                 body(gameBodyJSON).
         when().
                 put("/videogames/15").
         then().
                 log().
                 all();

     }

    @Test
    public void deleteGame() {

         String gameBodyJSON = "{\n" +
                 "  \"id\": 13,\n" +
                 "  \"name\": \"MyGameForDelete\",\n" +
                 "  \"releaseDate\": \"2019-06-24T18:37:36.835Z\",\n" +
                 "  \"reviewScore\": 88,\n" +
                 "  \"category\": \"string\",\n" +
                 "  \"rating\": \"string\"\n" +
                 "}";
         given().
                 body(gameBodyJSON).
                 log().
                 all().
         when().
                 post(EndPoint.VIDEOGAMES).
         then().
                 log().
                 all();

         System.out.println("Game is created");

         given().
                 log().
                 all().
         when().
                 delete("/videogames/13").
         then().
                 log().
                 all();
     }

    @Test
    public void getSingleGame() {

        given().
                pathParam("videoGameId", 5).
                log().
                all().
        when().
                get("videogames/{videoGameId}").
        then().
                log().
                all();
    }

    //29
    @Test
    public void getSingleGameSecond() {

        long responseTime =
        given().
                pathParam("videoGameId", 15).
                log().
                all().
        when().
                get(EndPoint.SINGLE_VIDEOGAME).
                time();

        System.out.println(responseTime);

        given().
                pathParam("videoGameId", 16).
                log().
                all().
        when().
                get(EndPoint.SINGLE_VIDEOGAME).
        then().
                time(lessThan(1000L));

    }

    //25
    @Test
    public void testVideoGameSerialisationByJSON() {

        VideoGame videoGame =
                new VideoGame("18", "shooter", "2019-06-06", "Halo 5", "Mature", "89");

        given().
                body(videoGame).
        when().
                post(EndPoint.VIDEOGAMES).
        then().
                log().
                all();
    }

    @Test
    public void testVideoGameSerialisationByXML() {

        VideoGame videoGame =
                new VideoGame("17", "shooter", "2019-06-07", "Halo 6", "Mature", "89");


        //jaxbObjectToXML(videoGame);

        given().
                body(videoGame).
        when().
                post(EndPoint.VIDEOGAMES).
        then().
                log().
                all();
    }

//    private static void jaxbObjectToXML(VideoGame videoGameObj)
//    {
//        try {
//            JAXBContext jaxbContext = JAXBContext.newInstance(VideoGame.class);
//            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
//
//            // To format XML
//            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
//
//            //If we DO NOT have JAXB annotated class
//            JAXBElement<VideoGame> jaxbElement =
//                    new JAXBElement<VideoGame>( new QName("", "videoGame"),
//                            VideoGame.class,
//                            videoGameObj);
//
//            jaxbMarshaller.marshal(jaxbElement, System.out);
//
//            //If we have JAXB annotated class
//            //jaxbMarshaller.marshal(employeeObj, System.out);
//
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }
//    }

    //26
    @Test
    public void testVideoGameSchemaXML() {

        given().
                pathParam("videoGameId", 15).
                log().
                all().
        when().
                get(EndPoint.SINGLE_VIDEOGAME).
        then().
                body(matchesXsdInClasspath("VideoGame.xsd")).
                log().
                all();
    }

    //27
    @Test
    public void testVideoGameSchemaJSON() {

        given().
                pathParam("videoGameId", 15).
                log().
                all().
        when().
                get(EndPoint.SINGLE_VIDEOGAME).
        then().
                body(matchesJsonSchemaInClasspath("VideoGameJSONSchema.json")).
                log().
                all();
    }

    //28
    @Test
    public void convertJSONToPojo() {
        Response response =
                given().
                        pathParam("videoGameId", 5).
                when().
                        get(EndPoint.SINGLE_VIDEOGAME);

        VideoGame videoGame = response.getBody().as(VideoGame.class);

        System.out.println(videoGame.toString());
    }


}
