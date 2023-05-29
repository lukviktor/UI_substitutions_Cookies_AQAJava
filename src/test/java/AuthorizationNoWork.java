import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;

import java.util.Date;

import static com.codeborne.selenide.Selenide.$x;
import static io.restassured.RestAssured.given;

public class AuthorizationNoWork {
    /*
    Вначале получаем `csrftoken`, а получить его можно просто открыв наш сайт
    Далее отправляем HTTP запрос подставить обязательные значения в `multipart/form-data` и в куки
    Возвращаемся к методу авторизации путь в девтуле `cckies` `Request Cookies`

     */
    private final static String URL = "https://at-sandbox.workbench.lanit.ru/";

    @Test
    public void getRestCookiesTest() {
        Selenide.open(URL);
        String csrfToken = WebDriverRunner.getWebDriver().manage().getCookieNamed("csrftoken").getValue(); // получаем значение куку `csrftoken`
        System.out.println(csrfToken + " - this csrfToken");

        ResponseSpecification response = given()
                .contentType(ContentType.MULTIPART)
                .cookie("csrftoken", csrfToken)
                .multiPart("csrftoken", csrfToken)
                .multiPart("username", "admin")//username: admin
                .multiPart("password", "adminat")
                .multiPart("next", "/")
                .multiPart("csrfmiddlewaretoken", csrfToken) // теперь отправляем post по адрессу `https://at-sandbox.workbench.lanit.ru/login/`
                .then().log().all().expect().cookie("sessionid");
        String sessionId = response.toString();
        System.out.println(sessionId + " - this sessionId");
        //продлеваем жизнь кука
        Date expData = new Date();
        expData.setTime(expData.getTime() + (100 * 100));
        System.out.println(expData + " - this expData");

        //создаем куку от selenium или selenide
        Cookie cookie = new Cookie("sessionid", sessionId, "at-sandbox.workbench.lanit.ru", "/", expData);

        //прокидываем куки
        WebDriverRunner.getWebDriver().manage().addCookie(cookie);
        // обновляем страницу
        Selenide.refresh();
        WebElement logIn = $x("//a[@id='userDropdown']");
        System.out.println(logIn.getText() + " - this logIn");
        $x("//a[@id='userDropdown']").should(Condition.text("admin"));
    }

}
