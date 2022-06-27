package ru.netology.faker;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import ru.netology.data.Info;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {

    private static final RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    private static final Faker faker = new Faker(new Locale("ru"));

    private DataGenerator() {
    }

    private static void sendRequest(Info user) {
        // сам запрос
        given() // "дано"
                .spec(requestSpec) // указываем, какую спецификацию используем
                .body(new Info(
                        user.getLogin(),
                        user.getPassword(),
                        user.getStatus()
                )) // передаём в теле объект, который будет преобразован в JSON
                .when() // "когда"
                .post("/api/system/users") // на какой путь, относительно BaseUri отправляем запрос
                .then() // "тогда ожидаем"
                .statusCode(200); // код 200 OK
    }

    public static String getRandomLogin() {
        Faker faker = new Faker(new Locale("ru"));
        String login = faker.name().username();
        return login;
    }

    public static String getRandomPassword() {
        Faker faker = new Faker(new Locale("ru"));
        String password = faker.internet().password();
        return password;
    }

    public static class Registration {
        private Registration() {

        }

        public static Info getUser(String status) {
            return new Info(getRandomLogin(), getRandomPassword(), status);
        }

        public static Info getRegisteredUser(String status) {
            Info registeredUser = getUser(status);
            sendRequest(registeredUser);
            return registeredUser;
        }

    }
}
