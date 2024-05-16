import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.Order;
import order.OrderClient;
import order.OrderGenerator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import user.User;
import user.UserClient;
import user.UserGenerator;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

public class CreateOrderTest {
    private User user;
    protected final UserGenerator randomUser = new UserGenerator();
    private UserClient userClient;

    private OrderClient orderClient;
    private Order order;

    private String bearerToken;

    @Before
    public void setUp() {
        user = randomUser.createNewUser();
        userClient = new UserClient();
        order = OrderGenerator.getIngredientList();
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Создание заказа авторизованного пользователя")
    @Description("Проверка на возможность создания заказа авторизованным пользователем")
    public void createOrderWithAuthorizationTest() {
        ValidatableResponse responseRegister = userClient.createUser(user);
        userClient.loginUser(user);
        bearerToken = responseRegister.extract().path("accessToken");
        ValidatableResponse responseCreateOrder = orderClient.createOrder(order, bearerToken);
        responseCreateOrder.assertThat().statusCode(200).body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа незарегистрированного пользователя")
    @Description("Проверка на возможность создания заказа незарегистрированного пользователя")
    public void createOrderWithoutAuthorizationTest() {
        bearerToken = "";
        ValidatableResponse responseCreateOrder = orderClient.createOrder(order, bearerToken);

        responseCreateOrder.assertThat().statusCode(200).body("success", is(true));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов")
    @Description("Проверка на возможность создания заказа без ингредиентов")
    public void createOrderWithoutIngredientTest() {
        ValidatableResponse responseRegister = userClient.createUser(user);
        userClient.loginUser(user);
        bearerToken = responseRegister.extract().path("accessToken");

        order.setIngredients(java.util.Collections.emptyList());

        ValidatableResponse responseCreateOrder = orderClient.createOrder(order, bearerToken);

        responseCreateOrder.assertThat().statusCode(400).body("success", is(false)).and().body("message", is("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа с неправильными ингредиентами")
    @Description("Проверка на возможность создания заказа с неправильными ингредиентами")
    public void createOrderWithWrongIngredientTest() {
        ValidatableResponse responseRegister = userClient.createUser(user);
        userClient.loginUser(user);
        bearerToken = responseRegister.extract().path("accessToken");

        List <String> wrongIngredient = new ArrayList<>();
        wrongIngredient.add("60d3463f7034a000269f45e7");


        order.setIngredients(wrongIngredient);

        ValidatableResponse responseCreateOrder = orderClient.createOrder(order, bearerToken);

        responseCreateOrder.assertThat().statusCode(400).body("success", is(false)).and().body("message", is("One or more ids provided are incorrect"));
    }
    @Test
    @DisplayName("Создание заказа с ингредиентами")
    @Description("Проверка на возможность создания заказа с ингредиентами")
    public void createOrderWithIngredientTest() {
        ValidatableResponse responseRegister = userClient.createUser(user);
        userClient.loginUser(user);
        bearerToken = responseRegister.extract().path("accessToken");

        List <String> ingredient = new ArrayList<>();
        ingredient.add("61c0c5a71d1f82001bdaaa6c");


        order.setIngredients(ingredient);

        ValidatableResponse responseCreateOrder = orderClient.createOrder(order, bearerToken);

        responseCreateOrder.assertThat().statusCode(200).body("success", is(true));
    }

    @After
    public void tearDown() {
        if (bearerToken == null) return;
        userClient.deleteUser(bearerToken);
    }


}
