package order;

public class OrderGenerator {
    public static Order getIngredientList() {

        return new Order()
                .setIngredients(OrderClient.getAllIngredients().extract().path("data._id"));
    }
}
