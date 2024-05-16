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

public class CreateUserTest {

    private User user;
    private UserClient userClient;
    protected final UserGenerator randomUser = new UserGenerator();
    private String bearerToken;
    @Before
    public void setUp() {
        userClient = new UserClient();
        user = randomUser.createNewUser();
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка на возможность создания уникального пользователя")
    public void createUserTest() {
        ValidatableResponse responseRegister = userClient.createUser(user);
        bearerToken = responseRegister.extract().path("accessToken");
        responseRegister.assertThat().statusCode(200).body("success", is(true));
    }

    @Test
    @DisplayName("Создание уже существующего пользователя")
    @Description("Проверка на возможность создания уже существующего пользователя")
    public void createAlreadyExistsUserTest() {
        ValidatableResponse responseRegisterFirstUser = userClient.createUser(user);
        bearerToken = responseRegisterFirstUser.extract().path("accessToken");
        ValidatableResponse responseRegisterSecondUser = userClient.createUser(user);
        responseRegisterSecondUser.assertThat().statusCode(403).body("success", is(false)).body("message", is("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка на возможность создания пользователя без email")
    public void createUserWithoutEmailTest() {
        user.setEmail("");
        ValidatableResponse responseRegister = userClient.createUser(user);
        bearerToken = responseRegister.extract().path("accessToken");
        responseRegister.assertThat().statusCode(403).body("success", is(false)).body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка на возможность создания пользователя без имени")
    public void createUserWithoutNameTest() {
        user.setName("");
        ValidatableResponse responseRegister = userClient.createUser(user);
        bearerToken = responseRegister.extract().path("accessToken");
        responseRegister.assertThat().statusCode(403).body("success", is(false)).body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка на возможность создания пользователя без пароля")
    public void createUserWithoutPasswordTest() {
        user.setPassword("");
        ValidatableResponse responseRegister = userClient.createUser(user);
        bearerToken = responseRegister.extract().path("accessToken");
        responseRegister.assertThat().statusCode(403).body("success", is(false)).body("message", is("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        if (bearerToken == null) return;
        userClient.deleteUser(bearerToken);
    }
}
