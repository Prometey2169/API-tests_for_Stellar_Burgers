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
import static user.UserGenerator.randomUser;

public class EditUserDataTest {
    private User user;
    private UserClient userClient;

    private String bearerToken;
    protected final UserGenerator randomUser = new UserGenerator();
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = randomUser.createNewUser();
    }

    @Test
    @DisplayName("Изменение данных авторизованного пользователя")
    @Description("Проверка на возможность изменения данных существующего авторизованного пользователя")
    public void changeDataUserWithAuthorization() {
        ValidatableResponse responseRegister = userClient.createUser(user);
        bearerToken = responseRegister
                .extract()
                .path("accessToken");

        User secondUser = randomUser();

        ValidatableResponse responsePatch = userClient.changeUser(secondUser, bearerToken);
        responsePatch.assertThat()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("Изменение данных неавторизованного пользователя")
    @Description("Проверка на возможность изменения данных существующего неавторизованного пользователя")
    public void changeDataUserWithoutAuthorization() {
        User secondUser = randomUser();

        ValidatableResponse responsePatch = userClient.changeUser(secondUser, "");
        responsePatch.assertThat()
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }


    @After
    public void tearDown() {
        if (bearerToken == null) return;
        userClient.deleteUser(bearerToken);

    }
}
