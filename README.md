# UI_substitutions_Cookies_AQAJava
## UI Автотесты авторизация на сайте с помощью подстановки Cookies
Тестовый стенд `https://at-sandbox.workbench.lanit.ru/`

### Также получим токен либо иные данные, что бы авторизоваться в системе
Step
1. Переходим на стенд
2. Переходим по кнопке `Log in`
3. Открываем `DevTools`
4. Регистрируемся или входим имеющиеся логин и пароль
5. Заходим на вкладку `Network`
6. Переходим в столбце `Name` в файл `login/`
7. Изучаем вкладку `Headers` `отлично :)) Погнали`
8. Переходим в `Respons` о чудо `пусто :))`
9. Наконец переходим в `Payload` есть `multipart/form-data`
Поля с отправкой данных username: `username`, `password`, `next` - опредяляет на какую страницу сделать переадресацию, `csrfmiddlewaretoken` - авторизация
10. Итог `multipart/form-data` - это **ОБЯЗАТЕЛЬНОЕ** тело для успешной авторизации
11. Токен CSRF – это уникальное и непредсказуемое значение, которое сервер генерирует для защиты уязвимых перед CSRF ресурсов. После выполнения запроса приложение на стороне сервера сравнивает два маркера, найденные в сеансе пользователя и в самом запросе
12. Переходим в `Header` смотрим в `Response Headers` находим `Set-Cookie:` и видим еще одну куку `sessionid` вот что мы будем подставлять в браузер, чтобы пропустить момент авторизации.
13. Тукже эти куки можно увидеть на вкладке `Cookies`. Важное правило `Нужно находиться на томже сайте и куки живут заданое время`. Но в случае необходимости можно задать время жизни.
14. Время жизни `Cookies` во вкладке `Cookies/Expires`
15. Дня наших задач нам нужна `csrftoken` и `sessionid`

### Подключаем зависимости в `build.gradle`
* testImplementation 'org.junit.jupiter:junit-jupiter-api:5.9.2'
* testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.9.2'
* testImplementation 'com.codeborne:selenide:6.14.1'
* testImplementation 'io.rest-assured:rest-assured:5.3.0'
* testImplementation 'org.jsoup:jsoup:1.15.3' на всякий случай
### Проходим авторизацию в проекте это класс Authorization.java
### Где можно ошибится показано в классе AuthorizationNoWork.java
