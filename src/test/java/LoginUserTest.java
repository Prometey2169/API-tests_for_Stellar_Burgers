import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;

import static org.hamcrest.core.Is.is;

public class LoginUserTest {
    private User user;
    private UserClient userClient;
    protected final UserGenerator randomUser = new UserGenerator();
    private String bearerToken;
    private ValidatableResponse responseRegister;

    @Before
    public void setUp() {
        userClient = new UserClient();
        user = randomUser.createNewUser();
        responseRegister = userClient.createUser(user);
    }

    @Test
    @DisplayName("Авторизация существующего пользователя")
    @Description("Проверка на возможность авторизации существующего пользователя")
    public void loginExistsUser() {
        bearerToken = responseRegister.extract().path("accessToken");

        ValidatableResponse responseLogin = userClient.loginUser(user);
        responseLogin
                .assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным email")
    @Description("Проверка на возможность авторизации пользователя с неверным email")
    public void loginUserWithWrongEmail() {
        bearerToken = responseRegister.extract().path("accessToken");

        user.setEmail("");
        ValidatableResponse responseLogin = userClient.loginUser(user);
        responseLogin
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @Test
    @DisplayName("Авторизация пользователя с неверным паролем")
    @Description("Проверка на возможность авторизации пользователя с неверным паролем")
    public void loginIncorrectPassword() {
        bearerToken = responseRegister
                .extract()
                .path("accessToken");

        user.setPassword("");
        ValidatableResponse responseLogin = userClient.loginUser(user);
        responseLogin
                .assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
    }

    @After
    public void tearDown() {

        if (bearerToken == null) return;
        userClient.deleteUser(bearerToken);
    }
}
