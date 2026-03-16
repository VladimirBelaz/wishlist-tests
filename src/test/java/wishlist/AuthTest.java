package wishlist;

import assertions.LoginPageAssertions;
import org.junit.jupiter.api.Test;
/**
 * Тесты авторизации: проверка входа с неверными данными,
 * редирект на логин при попытке доступа к защищённой странице,
 * валидация пустых полей.
 */
public class AuthTest extends AbsBaseTest {

    @Test
    public void testLoginWithInvalidCredentials() {
        logger.info("Тест логина с неверными данными");

        String invalidUsername = "wrong_user";
        String invalidPassword = "wrong_pass";

        loginPage.open();
        loginPage.login(invalidUsername, invalidPassword);

        LoginPageAssertions.assertThat(loginPage)
                .hasErrorMessage()
                .hasErrorMessageContaining("Неверное имя пользователя или пароль")
                .hasUsername(invalidUsername)
                .urlContains("/login");

        logger.info("Ошибка отображается корректно ✅");
    }

    @Test
    public void testLoginWithEmptyCredentials() {
        logger.info("Тест логина с пустыми данными");

        loginPage.open();
        loginPage.login("", "");  // заполнит поля пустыми строками и нажать кнопку

        LoginPageAssertions.assertThat(loginPage)
                .urlContains("/login")
                .hasUsernameValidationMessage("Заполните это поле.")
                .hasPasswordValidationMessage("Заполните это поле.")
                .hasNoServerError();

        logger.info("Валидация пустых полей работает корректно ✅");
    }

    @Test
    public void testAccessWithoutLogin() {
        logger.info("Тест доступа к странице списков без авторизации");

        wishListsPage.open();

        waiters.waitForUrlContains("/login");

        LoginPageAssertions.assertThat(loginPage)
                .urlContains("/login");

        logger.info("Редирект работает корректно ✅");
    }
}