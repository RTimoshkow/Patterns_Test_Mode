package ru.netology.test;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.faker.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.faker.DataGenerator.Registration.getUser;
import static ru.netology.faker.DataGenerator.getRandomLogin;
import static ru.netology.faker.DataGenerator.getRandomPassword;

public class AuthTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldSuccessfulLoginIfRegisteredActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id=\"login\"] input").val(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").val(registeredUser.getPassword());
        $(".button").click();
        $(".heading").shouldBe(ownText("Личный кабинет"));
    }

    @Test
    public void shouldGetErrorIfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id=\"login\"] input").val(notRegisteredUser.getLogin());
        $("[data-test-id=\"password\"] input").val(notRegisteredUser.getPassword());
        $(".button").click();
        $("[data-test-id=\"error-notification\"] [class=\"notification__content\"]").shouldHave(ownText("Неверно указан логин или пароль"));
    }

    @Test
    public void shouldGetErrorIfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id=\"login\"] input").val(blockedUser.getLogin());
        $("[data-test-id=\"password\"] input").val(blockedUser.getPassword());
        $(".button").click();
        $("[data-test-id=\"error-notification\"] [class=\"notification__content\"]").shouldBe(ownText("Пользователь заблокирован"));
    }

    @Test
    public void shouldGetErrorIfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id=\"login\"] input").val(wrongLogin);
        $("[data-test-id=\"password\"] input").val(registeredUser.getPassword());
        $(".button").click();
        $("[data-test-id=\"error-notification\"] [class=\"notification__content\"]").shouldHave(ownText("Неверно указан логин или пароль"));
    }

    @Test
    public void shouldGetErrorIfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id=\"login\"] input").val(registeredUser.getLogin());
        $("[data-test-id=\"password\"] input").val(wrongPassword);
        $(".button").click();
        $("[data-test-id=\"error-notification\"] [class=\"notification__content\"]").shouldHave(ownText("Неверно указан логин или пароль"));
    }
}
