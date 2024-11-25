package createOrder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import constants.Endpoints;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import models.ingredientsPojo.Ingredient;
import models.ingredientsPojo.IngredientsRequest;
import models.userPojo.CreateUserPojo;
import models.userPojo.UserCredsPojo;
import models.userPojo.UserData;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static constants.Endpoints.*;
import static models.Errors.expectedErrorCreateOrderNoIngredients;
import static models.userPojo.UserCredsPojo.credsFromUser;
import static specification.Request.*;
import static utils.GeneratorUsers.randomUser;
public class CreateOrderTests {

    IngredientsRequest ingredients;
    String bun = "Флюоресцентная булка R2-D3";
    String filling = "Биокотлета из марсианской Магнолии";
    String sause = "Соус фирменный Space Sauce";
    JsonObject requestBody;
    JsonArray ingredientsArray;
    CreateUserPojo user;
    String token;

    @Before
    public void setUp() {
        getIngredients();
        requestBody = new JsonObject();
        ingredientsArray = new JsonArray();
        user = randomUser();
    }

    @Test
    @Description("Создание заказа без авторизации")
    public void createOrderNoAuthorizationTest() {
        //создаём список ингридиентов
        List<String> ingredientIds = getIngredientIdsByNames(List.of(bun, filling, sause));
        ingredientIds.forEach(ingredientsArray::add);
        //формирование тела запроса
        requestBody.add("ingredients", ingredientsArray);
        //создание заказа
        Response response = doPostRequest(CREATE_ORDER, requestBody);
        //проверяем статус код
        Assert.assertEquals(200, response.statusCode());
    }

    @Test
    @Description("Создание заказа с авторизацией и ингридиентами")
    public void createOrderWithAuthorizationTest() {
        //создаём пользователя и получаем токен
        UserData userData = doPostRequest(CREATE_USER, user).then().extract().as(UserData.class);
        token = userData.getAccessToken();
        //логинемся
        UserCredsPojo creds = credsFromUser(user);
        doPostRequest(LOGIN, creds);
        //создаём список ингридиентов
        List<String> ingredientIds = getIngredientIdsByNames(List.of(bun, filling, sause));
        ingredientIds.forEach(ingredientsArray::add);
        //формирование тела ответа
        requestBody.add("ingredients", ingredientsArray);
        //создание заказа
        Response response = doPostRequest(CREATE_ORDER, requestBody);
        //проверяем статус код
        Assert.assertEquals(200, response.statusCode());
    }

    @Test
    @Description("Создание заказа без ингредиентов")
    public void createOrderNoIngredientsTest() {
        //создаём пустой список ингредиентов
        JsonArray emptyIngredientArray = new JsonArray();
        requestBody.add("ingredients", emptyIngredientArray);
        //отправляем запрос на создания заказа
        Response response = doPostRequest(CREATE_ORDER, requestBody);
        //проверяем тело ответа
        Assert.assertEquals(expectedErrorCreateOrderNoIngredients, response.jsonPath().getString("message"));
    }

    @Test
    @Description("Создание заказа с неверным хеш кодом")
    public void createOrderWithWrongHashCodeTest() {
        //создаём список ингредиентов с неверным хешкодом
        JsonArray listOfIngredientWithWrongHashCode = new JsonArray();
        //генерируем случайный хеш код
        String hashCode = UUID.randomUUID().toString().replace("-", "").substring(0, 30);
        listOfIngredientWithWrongHashCode.add(hashCode);
        requestBody.add("ingredients", listOfIngredientWithWrongHashCode);
        //отправляем запрос на создания заказа
        Response response = doPostRequest(CREATE_ORDER, requestBody);
        //проверяем статус код
        Assert.assertEquals(500, response.statusCode());
    }





    //метод получения списка ингредиентов
    public void getIngredients() {
        ingredients = doGetRequest(GET_DATA_OF_INGREDIENT).then().extract().as(IngredientsRequest.class);
    }

    //метод создания списка ингридиентов для создания заказа
    public List<String> getIngredientIdsByNames(List<String> names) {
        return ingredients.getData().stream()
                .filter(ingredient -> names.stream().anyMatch(name -> ingredient.getName().contains(name)))
                .map(Ingredient::get_id)
                .collect(Collectors.toList());
    }

    @After
    public void tearDown() {
        if(token!=null){
            doDeleteRequest(Endpoints.API_AUTH_USER, token);
        }
    }
}
