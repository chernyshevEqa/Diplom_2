package specification;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import static constants.Endpoints.BASE_URL;
import static io.restassured.RestAssured.given;


public class Request {

    public static Response doGetRequest(String endpoint, String token) {
        RequestSpecification request =  given(baseRequest(token));
        return request.get(endpoint);
    }

    public static Response doGetRequest(String endpoint) {
        RequestSpecification request =  given(baseRequest());
        return request.get(endpoint);
    }

    public static Response doPostRequest(String endpoint, Object json) {
        RequestSpecification request =  given(baseRequest());
        request.body(json);
        return request.post(endpoint);
    }

    public static Response doPatchRequest(String endpoint, String token, Object json) {
        RequestSpecification request =  given(baseRequest(token));
        request.body(json);
        return request.patch(endpoint);
    }

    public static Response doPatchRequest(String endpoint, Object json) {
        RequestSpecification request =  given(baseRequest());
        request.body(json);
        return request.patch(endpoint);
    }

    public static Response doDeleteRequest(String endpoint, String token) {
        RequestSpecification request = given(baseRequest(token));
        return request.delete(endpoint);

    }

    private static RequestSpecification baseRequest() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
//                .addFilter(new RequestLoggingFilter())
//                .addFilter(new ResponseLoggingFilter())
                .setRelaxedHTTPSValidation()
                .build();
    }

    private static RequestSpecification baseRequest(String token) {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .addHeader("Authorization", token)
//                .addFilter(new RequestLoggingFilter())
//                .addFilter(new ResponseLoggingFilter())
                .setRelaxedHTTPSValidation()
                .build();
    }
    
}
