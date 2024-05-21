package tests;

import data.Constants;
import data.Keys;
import data.Settings;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@Story("Negative tests")
public class NegativeTests {


    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = Settings.BASE_URL;
        RestAssured.filters(new AllureRestAssured());
    }

    @Test(description = "Invalid API key test")
    public void testInvalidApiKey() {
        given()
                .queryParam(Keys.KEY, Constants.INVALID_API_KEY)
                .queryParam(Keys.Q, Constants.VALID_CITY_NAME)
                .when()
                .get(Keys.CURRENT_JSON)
                .then()
                .statusCode(403)
                .body(Keys.ERROR_CODE, equalTo(2008))
                .body(Keys.ERROR_MESSAGE, containsString("API key has been disabled."));
    }

    @Test(description = "Disabled API key test")
    public void testDisabledApiKey() {
        given()
                .queryParam(Keys.KEY, Constants.DISABLED_API_KEY)
                .queryParam(Keys.Q, Constants.VALID_CITY_NAME)
                .when()
                .get(Keys.CURRENT_JSON)
                .then()
                .statusCode(403)
                .body(Keys.ERROR_CODE, equalTo(2008))
                .body(Keys.ERROR_MESSAGE, containsString("API key has been disabled."));
    }

    @Test(description = "Invalid city name test")
    public void testInvalidCityName() {
        given()
                .queryParam(Keys.KEY, Settings.API_KEY)
                .queryParam(Keys.Q, Constants.INVALID_CITY_NAME)
                .when()
                .get(Keys.CURRENT_JSON)
                .then()
                .statusCode(400)
                .body(Keys.ERROR_CODE, equalTo(1006))
                .body(Keys.ERROR_MESSAGE, containsString("No location found matching parameter 'q'"));
    }

    @Test(description = "Missing parameter test")
    public void testMissingParameters() {
        given()
                .queryParam(Keys.KEY, Settings.API_KEY)
                .when()
                .get(Keys.CURRENT_JSON)
                .then()
                .statusCode(400)
                .body(Keys.ERROR_CODE, equalTo(1003))
                .body(Keys.ERROR_MESSAGE, containsString("Parameter 'q' not provided."));
    }
}
