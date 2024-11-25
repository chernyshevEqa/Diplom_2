package getOrdersTests;

import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.userPojo.CreateUserPojo;
import models.userPojo.UserCredsPojo;
import models.userPojo.UserData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static constants.Endpoints.*;
import static models.userPojo.UserCredsPojo.credsFromUser;
import static specification.Request.doGetRequest;
import static specification.Request.doPostRequest;
import static utils.GeneratorUsers.randomUser;

public class GetOrdersTests {
    CreateUserPojo user;
    String token;

    @Before
    public void setUp() {
        user = randomUser();
    }

    @Test
    @Description("Получение заказов конкретного пользователя авторизованный пользователь")
    public void getOrderWithAuthorizationTest() {
        //создаём пользователя и получаем токен
        UserData userData = doPostRequest(CREATE_USER, user).then().extract().as(UserData.class);
        token = userData.getAccessToken();

        //логинемся
        UserCredsPojo creds = credsFromUser(user);
        doPostRequest(LOGIN, creds);

        //получаем список заказов пользователя
        Response response = doGetRequest("/api/orders", token);

        //проверяем статус код
        Assert.assertEquals(200, response.statusCode());
    }

    @Test
    @Description("Получение заказов конкретного пользователя не авторизованный пользователь")
    public void getOrderNoAuthorizationTest() {
        //создаём пользователя и получаем токен
        doPostRequest(CREATE_USER, user);

        //логинемся
        UserCredsPojo creds = credsFromUser(user);
        doPostRequest(LOGIN, creds);

        //получаем список заказов пользователя
        Response response = doGetRequest("/api/orders");

        //проверяем статус код
        Assert.assertEquals(401,response.statusCode());
    }
}
