package tests;

import data.Settings;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.response.Response;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Story("Positive tests")
public class PositiveTests {

    @DataProvider
    public Object[][] cities() {
        return new Object[][]{
                {"London", 14, 10},
                {"New York", 22, 10},
                {"Minsk", 21, 10},
                {"Tokyo", 18, 10}
        };
    }

    @Test(description = "Test City", dataProvider = "cities")
    public void testCityWeather(String cityName, float expectedTemp, float delta) {
        Response response = given()
                .baseUri(Settings.BASE_URL)
                .filter(new AllureRestAssured())
                .queryParam("key", Settings.API_KEY)
                .queryParam("q", cityName)
                .when()
                .get("/current.json")
                .then()
                .statusCode(200)
                .body("location.name", equalTo(cityName))
//                .body("current.temp_c", closeTo(expectedTemp, delta)) - NOT WORKING
                .body("current.temp_c", greaterThanOrEqualTo(expectedTemp - delta))
                .body("current.temp_c", lessThanOrEqualTo(expectedTemp + delta))
                .extract()
                .response();
        float temp = response.jsonPath().getFloat("current.temp_c");
        System.out.printf("Difference from expected in %s: %.1f%n", cityName, temp - expectedTemp);
    }
}