package loginTests;

import com.github.javafaker.Faker;
import constants.Endpoints;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.userPojo.CreateUserPojo;
import models.userPojo.UserCredsPojo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static constants.Endpoints.*;
import static models.Errors.expectedErrorNoEmailOrPassword;
import static models.userPojo.UserCredsPojo.credsFromUser;
import static specification.Request.doDeleteRequest;
import static specification.Request.doPostRequest;
import static utils.GeneratorUsers.randomUser;

public class LoginTests {
    CreateUserPojo user;
    String token;
    Faker faker;
    UserCredsPojo creds;

    @Before
    public void setUp() {
        faker = new Faker();
        user = randomUser();
        doPostRequest(CREATE_USER,user);
        creds = credsFromUser(user);

    }

    @Test
    @Description("логин под существующим пользователем")
    public void loginTest() {
        //логинемся
        Response response = doPostRequest(LOGIN, creds);

        //получаем токен для удаления пользователя
        token = response.jsonPath().getString("accessToken");

        //проверяем статус код
        Assert.assertEquals(200, response.statusCode());
    }

    @Test
    @Description("логин с неверным логином и паролем")
    public void loginWrongPasswordTest() {
        //логинемся
        Response getToken = doPostRequest(LOGIN, creds);

        //получаем токен для удаления пользователя
        token = getToken.jsonPath().getString("accessToken");

        //создаём креды с неверным паролем
        UserCredsPojo credsWithWrongPassword = new UserCredsPojo(creds.getEmail(), faker.internet().password(10,15));

        //пытаемся залогиниться
        Response response = doPostRequest(LOGIN, credsWithWrongPassword);

        //проверяем тело ответа
        Assert.assertEquals(expectedErrorNoEmailOrPassword, response.jsonPath().getString("message"));
    }

    @After
    public void tearDown() {
        if(token!=null){
            doDeleteRequest(Endpoints.API_AUTH_USER, token);
        }
    }
}
