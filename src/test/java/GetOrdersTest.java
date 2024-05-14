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

import static org.hamcrest.core.Is.is;

public class GetOrdersTest {

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
    @DisplayName("Получение списка заказов авторизованного пользователя")
    @Description("Проверка на возможность получения списка заказов авторизованного пользователя")
    public void createOrderWithAuthorizationTest() {
        ValidatableResponse responseRegister = userClient.createUser(user);
        bearerToken = responseRegister.extract().path("accessToken");
        userClient.loginUser(user);
        orderClient.createOrder(order, bearerToken);

        ValidatableResponse responseOrderUser = OrderClient.getClientOrder(bearerToken);

        responseOrderUser.assertThat().statusCode(200).body("success", is(true));
    }

    @Test
    @DisplayName("Получение заказов неавторизованного пользователя")
    @Description("Проверка получения списка заказов неавторизованного пользователя")
    public void createOrderWithoutAuthorizationTest() {
        bearerToken = "";
        ValidatableResponse getClientOrder = OrderClient.getClientOrder(bearerToken);

        getClientOrder.assertThat().statusCode(401).body("success", is(false)).and()
                .body("message", is("You should be authorised"));
    }

    @After
    public void tearDown() {
        if (bearerToken == null) return;
        userClient.deleteUser(bearerToken);
    }
}
