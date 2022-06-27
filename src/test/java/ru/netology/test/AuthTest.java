package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.faker.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.faker.DataGenerator.Registration.getUser;

public class AuthTest {

    @BeforeAll
    static void setUpAll() {
        open("http://localhost:9999");
    }

    @Test
    void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=\"login\"] input").val(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").val(registeredUser.getPassword());
        $(".button").click();
        $(".heading").shouldBe(visible).shouldBe(ownText("Личный кабинет"));
    }

    @Test
    void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=\"login\"] input").val(notRegisteredUser.getLogin());
        $("[data-test-id=\"password\"] input").val(notRegisteredUser.getPassword());
        $(".button").click();
        $("[data-test-id=\"error-notification\"] [class=\"notification__content\"]").shouldBe(visible);
        $("[data-test-id=\"error-notification\"] [class=\"notification__content\"]").shouldBe(text("Ошибка! Неверно указан логин или пароль"));
    }
}
