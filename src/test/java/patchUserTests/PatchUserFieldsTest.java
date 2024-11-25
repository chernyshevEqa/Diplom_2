package patchUserTests;


import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.userPojo.CreateUserPojo;
import models.userPojo.UserCredsPojo;
import models.userPojo.User;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static constants.Endpoints.*;
import static models.Errors.expectedErrorNoAuthorization;
import static models.userPojo.UserCredsPojo.credsFromUser;
import static specification.Request.*;
import static utils.GeneratorUsers.randomUser;

@RunWith(Parameterized.class)
public class PatchUserFieldsTest {

    private final String fieldToChange;
    private final String newValue;
    private String token;
    CreateUserPojo user;

    public PatchUserFieldsTest(String fieldToChange, String newValue) {
        this.fieldToChange = fieldToChange;
        this.newValue = newValue;
    }

        // Параметры для теста
        @Parameterized.Parameters(name = "Изменение поля {0} на значение {1}")
        public static Object[][] data() {
        Faker faker = new Faker();
            return new Object[][]{
                    {"email", faker.internet().emailAddress()},
                    {"name", faker.name().firstName()}
            };
        }

        @Before
        public void setUp() {
            //создаём пользователя
            user = randomUser();
            UserCredsPojo creds = credsFromUser(user);
            doPostRequest(CREATE_USER, user);
            // Логинимся и получаем токен
            Response response = doPostRequest(LOGIN, creds);
            token = response.jsonPath().getString("accessToken");
        }

        @Test
        @Description("Изменение данных пользователя с авторизацией для поля")
        public void patchUserFieldWithAuthorizationTest() {
            // Создаем объект пользователя с обновленным полем
            User updatedUser = new User();
            if ("email".equals(fieldToChange)) {
                updatedUser.setEmail(newValue);
            } else if ("name".equals(fieldToChange)) {
                updatedUser.setName(newValue);
            }

            // Отправляем запрос на обновление
            doPatchRequest(API_AUTH_USER, token, updatedUser);

            // Получаем информацию о пользователе
            Response getUserInfo = doGetRequest(API_AUTH_USER, token);

            // Проверяем, что значение поля изменилось
            String actualValue = getUserInfo.jsonPath().getString("user." + fieldToChange);
            Assert.assertEquals("Поле " + fieldToChange + " должно быть обновлено", newValue, actualValue);
        }

    @Test
    @Description("Изменение данных пользователя бее авторизации для поля")
    public void patchUserFieldNoAuthorizationTest() {
        // Создаем объект пользователя с обновленным полем
        User updatedUser = new User();
        if ("email".equals(fieldToChange)) {
            updatedUser.setEmail(newValue);
        } else if ("name".equals(fieldToChange)) {
            updatedUser.setName(newValue);
        }

        // Отправляем запрос на обновление
        Response response = doPatchRequest(API_AUTH_USER, updatedUser);

        //проверяем что тело ответа возвращает ошибку

        Assert.assertEquals(expectedErrorNoAuthorization, response.jsonPath().getString("message"));
    }



    @After
    public void tearDown() {
        if(token!=null){
            doDeleteRequest(API_AUTH_USER, token);
        }
    }
}

