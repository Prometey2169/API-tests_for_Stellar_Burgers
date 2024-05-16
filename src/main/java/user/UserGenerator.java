package user;
import com.github.javafaker.Faker;
import io.qameta.allure.Step;


public class UserGenerator {
    static final Faker faker = new Faker();
    public static User randomUser(){
        Faker faker = new Faker();
        final String email = faker.internet().emailAddress();
        final String name = faker.name().firstName();
        final String password = faker.internet().password(6,9);
        return new User(email, name, password);
    }
    @Step("Создание нового пользователя")
    public User createNewUser() {
        return new User(
                faker.internet().emailAddress(),
                faker.name().firstName(),
                faker.internet().password(6, 9)
        );
    }

}



