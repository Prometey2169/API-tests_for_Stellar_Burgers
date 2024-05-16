package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import utils.APIs;
import utils.BaseURL;

import static io.restassured.RestAssured.given;



public class OrderClient {

    @Step("Создание нового заказа")
    public ValidatableResponse createOrder(Order order, String bearerToken) {
        return given()
                .spec(BaseURL.specification())
                .headers("Authorization", bearerToken)
                .and()
                .body(order)
                .when()
                .post(APIs.CREATE_ORDER_API)
                .then();
    }

    @Step("Получение списка заказов")
    public static ValidatableResponse getClientOrder(String bearerToken) {
        return given()
                .spec(BaseURL.specification())
                .headers("Authorization", bearerToken)
                .get(APIs.USER_ORDERS_API)
                .then();
    }


    @Step("Получение списка ингредиентов")
    public static  ValidatableResponse getAllIngredients() {
        return given()
                .spec(BaseURL.specification())
                .get(APIs.INGREDIENTS_API)
                .then();
    }
}
