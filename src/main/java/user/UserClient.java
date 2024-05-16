package user;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import utils.APIs;
import utils.BaseURL;

import static io.restassured.RestAssured.given;

public class UserClient {
    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .spec(BaseURL.specification())
                .and()
                .body(user)
                .when()
                .post(APIs.CREATE_USER_API)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse loginUser(User user) {
        return given()
                .spec(BaseURL.specification())
                .and()
                .body(user)
                .when()
                .post(APIs.LOGIN_USER_API)
                .then();
    }

    @Step("Изменение данных пользователя")
    public ValidatableResponse changeUser(User user, String bearerToken) {
        return given()
                .spec(BaseURL.specification())
                .header("Authorization", bearerToken)
                .contentType(ContentType.JSON)
                .and()
                .body(user)
                .when()
                .patch(APIs.PATCH_USER_API)
                .then();
    }

    @Step("Удаление пользователя")
    public ValidatableResponse deleteUser(String bearerToken) {
        return given()
                .spec(BaseURL.specification())
                .headers("Authorization", bearerToken)
                .delete(APIs.DELETE_USER_API)
                .then();
    }
}
