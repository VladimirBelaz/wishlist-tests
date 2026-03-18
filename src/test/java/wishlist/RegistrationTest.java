package wishlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.RegisterPage;
import assertions.RegisterPageAssertions;
import assertions.LoginPageAssertions;
import assertions.WishListsPageAssertions;

/**
 * Тесты для проверки функциональности регистрации пользователей.
 * Содержит позитивные и негативные сценарии, включая проверки
 * ограничений длины полей, уникальности имени/email и корректности форматов.
 */
public class RegistrationTest extends AbsBaseTest {

    private RegisterPage registerPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp(); // инициализация driver, waiters, loginPage, wishListsPage (без логина)
        registerPage = new RegisterPage(driver);
    }

    /**
     * Тест успешной регистрации нового пользователя.
     * Создаёт уникального пользователя (имя + timestamp), заполняет все поля
     * и отправляет форму. После успешной регистрации ожидается редирект на
     * страницу входа (/login), так как приложение не выполняет автоматический вход.
     * Затем выполняется вход новым пользователем, и проверяется переход на /wishlists.
     */
    @Test
    public void testSuccessfulRegistration() {
        logger.info("Тест успешной регистрации нового пользователя");

        String uniqueUsername = "testuser" + System.currentTimeMillis();
        String email = uniqueUsername + "@example.com";
        String password = "Test123!";

        registerPage.open();
        registerPage.register(uniqueUsername, email, password);

        // Ждём редиректа на страницу входа (приложение может не сразу перенаправить)
        waiters.waitForUrlContains("/login");

        // Проверяем, что действительно на странице логина
        LoginPageAssertions.assertThat(loginPage)
                .urlContains("/login");

        // Выполняем вход новым пользователем
        loginPage.login(uniqueUsername, password);
        waiters.waitForUrlContains("/wishlists");

        WishListsPageAssertions.assertThat(wishListsPage)
                .urlContains("/wishlists");

        logger.info("Регистрация и последующий вход выполнены успешно ✅");
    }

    /**
     * Тест регистрации с уже существующим именем пользователя.
     * Использует существующего пользователя (vladimirbv) и уникальный email,
     * чтобы проверить реакцию сервера. Ожидается, что останемся на странице
     * регистрации и увидим сообщение об ошибке.
     */
    @Test
    public void testRegistrationWithExistingUsername() {
        logger.info("Тест регистрации с уже существующим именем пользователя");

        String existingUsername = "vladimirbv";
        String uniqueEmail = "user" + System.currentTimeMillis() + "@example.com";
        String password = "Valid123";

        registerPage.open();
        registerPage.register(existingUsername, uniqueEmail, password);

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasErrorMessage();

        logger.info("Ошибка при регистрации существующего пользователя отображается корректно ✅");
    }

    /**
     * Тест регистрации с уже существующим email.
     * Использует существующий email (testers@mail.ru) и уникальное имя,
     * проверяет появление ошибки на странице регистрации.
     */
    @Test
    public void testRegistrationWithExistingEmail() {
        logger.info("Тест регистрации с уже существующим email");

        String uniqueUsername = "newuser" + System.currentTimeMillis();
        String existingEmail = "testers@mail.ru";
        String password = "Valid123";

        registerPage.open();
        registerPage.register(uniqueUsername, existingEmail, password);

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasErrorMessage();

        logger.info("Ошибка при регистрации с существующим email отображается корректно ✅");
    }

    /**
     * Тест регистрации с пустыми полями.
     * Проверяет браузерную HTML5‑валидацию: после клика по кнопке
     * "Зарегистрироваться" поля должны показать сообщение
     * "Заполните это поле." (точный текст зависит от локали браузера).
     */
    @Test
    public void testRegistrationWithEmptyFields() {
        logger.info("Тест регистрации с пустыми полями");

        registerPage.open();
        registerPage.register("", "", "");

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasUsernameValidationMessageContaining("заполните это поле")
                .hasEmailValidationMessageContaining("заполните это поле")
                .hasPasswordValidationMessageContaining("заполните это поле");

        logger.info("Валидация пустых полей работает корректно ✅");
    }

    /**
     * Тест регистрации с некорректным форматом email.
     * Вводит email без домена верхнего уровня (например, "test@test").
     * Сервер должен вернуть ошибку, и мы остаёмся на странице регистрации.
     */
    @Test
    public void testRegistrationWithMalformedEmail() {
        logger.info("Тест регистрации с некорректным email");

        String username = "testuser" + System.currentTimeMillis();
        String malformedEmail = "test@test"; // проходит браузерную проверку, сервер вернёт ошибку
        String password = "Valid123";

        registerPage.open();
        registerPage.register(username, malformedEmail, password);

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasErrorMessage();

        logger.info("Ошибка при некорректном email отображается корректно ✅");
    }

    /**
     * Тест валидации длины имени пользователя (слишком короткое – 2 символа).
     * Минимальная длина имени по спецификации – 3 символа.
     * Ожидается ошибка от сервера (страница регистрации с сообщением об ошибке).
     */
    @Test
    public void testRegistrationWithUsernameTooShort() {
        logger.info("Тест регистрации с именем короче 3 символов");

        String shortUsername = "ab"; // длина 2
        String email = shortUsername + "@example.com";
        String password = "Valid123";

        registerPage.open();
        registerPage.register(shortUsername, email, password);

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasErrorMessage();

        logger.info("Ошибка при коротком имени отображается корректно ✅");
    }

    /**
     * Тест валидации длины имени пользователя (слишком длинное – 51 символ).
     * Максимальная длина имени по спецификации – 50 символов.
     * Ожидается ошибка от сервера.
     */
    @Test
    public void testRegistrationWithUsernameTooLong() {
        logger.info("Тест регистрации с именем длиннее 50 символов");

        String longUsername = "a".repeat(51); // длина 51
        String email = "test@example.com";
        String password = "Valid123";

        registerPage.open();
        registerPage.register(longUsername, email, password);

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasErrorMessage();

        logger.info("Ошибка при слишком длинном имени отображается корректно ✅");
    }

    /**
     * Тест валидации длины пароля (слишком короткий – 5 символов).
     * Минимальная длина пароля по спецификации – 6 символов.
     * Ожидается ошибка от сервера.
     */
    @Test
    public void testRegistrationWithPasswordTooShort() {
        logger.info("Тест регистрации с паролем короче 6 символов");

        String username = "testuser" + System.currentTimeMillis();
        String email = username + "@example.com";
        String shortPassword = "12345"; // длина 5

        registerPage.open();
        registerPage.register(username, email, shortPassword);

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasErrorMessage();

        logger.info("Ошибка при коротком пароле отображается корректно ✅");
    }
}