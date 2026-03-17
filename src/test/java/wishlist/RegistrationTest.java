package wishlist;

import assertions.LoginPageAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.RegisterPage;
import assertions.RegisterPageAssertions;
import assertions.WishListsPageAssertions;
/**
 * Тесты регистрации: успешная регистрация нового пользователя,
 * попытка регистрации с уже существующим пользователем,
 * проверка валидации пустых полей и короткого пароля.
 */
public class RegistrationTest extends AbsBaseTest {

    private RegisterPage registerPage;

    @BeforeEach
    @Override
    public void setUp() {
        super.setUp();
        registerPage = new RegisterPage(driver);
    }

    @Test
    public void testSuccessfulRegistration() {
        logger.info("Тест успешной регистрации нового пользователя");

        String uniqueUsername = "testuser" + System.currentTimeMillis();
        String email = uniqueUsername + "@example.com";
        String password = "Test123!";

        registerPage.open();
        registerPage.register(uniqueUsername, email, password);

        waiters.waitForUrlContains("/login");
        LoginPageAssertions.assertThat(loginPage).urlContains("/login");

        loginPage.login(uniqueUsername, password);
        waiters.waitForUrlContains("/wishlists");

        WishListsPageAssertions.assertThat(wishListsPage)
                .urlContains("/wishlists");
    }

    /**
     * Проверка регистрации с уже существующим пользователем.
     */
    @Test
    public void testRegistrationWithExistingUser() {
        logger.info("Тест регистрации с уже существующим пользователем");

        String existingUsername = "petrIvanovich";
        String email = "testers@mail.ru";
        String password = "alpenGold";

        registerPage.open();
        registerPage.register(existingUsername, email, password);

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasErrorMessage()
                .hasErrorMessageContaining("Не удалось зарегистрировать пользователя");
    }

    @Test
    public void testRegistrationWithEmptyFields() {
        logger.info("Тест регистрации с пустыми полями");

        registerPage.open();
        registerPage.register("", "", "");

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasUsernameValidationMessage("Заполните это поле.")
                .hasEmailValidationMessage("Заполните это поле.")
                .hasPasswordValidationMessage("Заполните это поле.");

        logger.info("Валидация пустых полей работает корректно ✅");
    }

    /**
     * Проверка регистрации с некорректным форматом email.
     */
    @Test
    public void testRegistrationWithInvalidEmail() {
        logger.info("Тест регистрации с некорректным email");

        String username = "testuser" + System.currentTimeMillis();
        String invalidEmail = "not-an-email";
        String password = "Test123!";

        registerPage.open();
        registerPage.register(username, invalidEmail, password);

        RegisterPageAssertions.assertThat(registerPage)
                .urlContains("/register")
                .hasEmailValidationMessageContaining("адрес электронной почты"); // или точное сообщение

        logger.info("Валидация email работает корректно ✅");
    }

    /**
     * Проверка валидации длины пароля (слишком короткий – меньше 6 символов).
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
    }
    /**
     * Проверка валидации длины имени пользователя (слишком короткое – меньше 3 символов).
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
    }
    /**
     * Проверка валидации длины имени пользователя (слишком длинное – больше 50 символов).
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
    }

}