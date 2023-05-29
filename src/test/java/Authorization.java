import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;


import java.util.Date;

import static com.codeborne.selenide.Selenide.$x;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class Authorization {
/*
1. Сначала открываем страницу.
2. Получаем значение `csrftoken` из cookie.
3. Подставляем `csrftoken`, логин и пароль в форму авторизации, отправляем запрос по адресу `/login/`.
4. Извлекаем значение `sessionid` из полученного ответа.
5. Создаём cookie с полученным `sessionid`, доменом и датой истечения срока действия.
6. Добавляем созданную cookie в браузер и обновляем страницу.
7. Получаем элемент `logIn` - это ссылка на имя пользователя, который вошёл в систему.
8. Получаем текст внутри элемента `logIn` и сравниваем его с логином пользователя.
 */
    private final static String URL = "https://at-sandbox.workbench.lanit.ru/";

    @Test
    public void authTest() {
        // Открыть страницу
        Selenide.open(URL);

        // Получить значение csrftoken из cookie
        String csrfToken = WebDriverRunner.getWebDriver().manage().getCookieNamed("csrftoken").getValue();
        System.out.println(csrfToken + " - this is csrfToken");

        // Отправить авторизационный запрос
        String sessionId = given()
                .contentType(ContentType.MULTIPART)
                .cookie("csrftoken", csrfToken)
                .multiPart("csrftoken", csrfToken)
                .multiPart("username", "admin")
                .multiPart("password", "adminat")
                .multiPart("next", "/")
                .multiPart("csrfmiddlewaretoken", csrfToken)
                .when().post(URL + "login/")
                .then().extract().cookie("sessionid");
        System.out.println(sessionId + " - this is sessionId");

        // Создать cookie для авторизации
        Date expData = new Date();
        expData.setTime(expData.getTime() + (100 * 100));
        System.out.println(expData + " - this is expData");
        Cookie cookie = new Cookie("sessionid", sessionId, ".workbench.lanit.ru", "/", expData);

        // Добавить cookie в браузер и обновить страницу
        WebDriverRunner.getWebDriver().manage().addCookie(cookie);
        Selenide.refresh();

        // Проверить, что авторизационный токен совпадает с логином пользователя
        WebElement logIn = $x("//a[@id='userDropdown']");
        System.out.println(logIn.getText() + " - this is logIn");
        $x("//a[@id='userDropdown']")
                .shouldHave(Condition.text("admin"));
        Assertions.assertEquals(logIn.getText(), "admin", "Вторая проверка на вход с авторизацией");
    }
}