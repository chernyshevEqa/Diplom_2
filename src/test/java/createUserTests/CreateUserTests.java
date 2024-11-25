package createUserTests;

import constants.Endpoints;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.userPojo.CreateUserPojo;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import static constants.Endpoints.CREATE_USER;
import static models.Errors.expectedErrorExistedUser;
import static models.Errors.expectedErrorNoRequiredFiled;
import static specification.Request.doDeleteRequest;
import static specification.Request.doPostRequest;
import static utils.GeneratorUsers.randomUser;
import static utils.GeneratorUsers.randomUserNoPassword;

public class CreateUserTests {
    CreateUserPojo user;
    String token;


    @Before
    public void setUp() {
        user = randomUser();
    }

    @Test
    @Description("создание уникального пользователя")
    public void createUserTest() {
        //создаём пользователя
        Response response = doPostRequest(CREATE_USER, user);

        //получаем токен для удаления пользователя
        token = response.jsonPath().getString("accessToken");

        //проверяем статус код
        Assert.assertEquals(200, response.statusCode());

    }

    @Test
    @Description("создание пользователя, который уже зарегистрирован")
    public void createExistedUserTest() {
        //создаём пользователя
        doPostRequest(CREATE_USER, user);

        //пытаемся создать пользователя с такими же данными
        Response response = doPostRequest(CREATE_USER, user);

        //проверяем тело ответа
        Assert.assertEquals(expectedErrorExistedUser, response.then().extract().jsonPath().getString("message"));
    }

    @Test
    @Description("создать пользователя и не заполнить одно из обязательных полей")
    public void createUserNoPasswordTest() {
        //создаём пользователя без пароля
        CreateUserPojo user = randomUserNoPassword();
        Response response = doPostRequest(CREATE_USER, user);

        // проверяем тело ответа
        Assert.assertEquals( expectedErrorNoRequiredFiled, response.jsonPath().getString("message"));
    }

    @After
    public void tearDown() {
        if(token!=null){
            doDeleteRequest(Endpoints.API_AUTH_USER, token);
        }
    }
}
