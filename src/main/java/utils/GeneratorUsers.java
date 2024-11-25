package utils;
import com.github.javafaker.Faker;
import models.userPojo.CreateUserPojo;


public class GeneratorUsers {

    public static CreateUserPojo randomUser() {

        Faker faker = new Faker();

        return new CreateUserPojo()
                .withEmail(faker.internet().emailAddress())
                .withPassword(faker.internet().password(10, 20))
                .withName(faker.name().firstName());
    }

    public static CreateUserPojo randomUserNoPassword() {

        Faker faker = new Faker();

        return new CreateUserPojo()
                .withEmail(faker.internet().emailAddress())
                .withName(faker.name().firstName());
    }

}
