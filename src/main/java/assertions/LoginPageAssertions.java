package assertions;

import org.junit.jupiter.api.Assertions;
import pages.LoginPage;
/**
 * Набор проверок для страницы входа.
 * Позволяет проверить наличие ошибок, текст сообщений,
 * состояние полей и URL.
 */
public class LoginPageAssertions {

    private final LoginPage page;

    private LoginPageAssertions(LoginPage page) {
        this.page = page;
    }
    /**
     * Фабричный метод для создания экземпляра ассерта.
     *
     * @param page страница логина
     * @return объект ассерта
     */
    public static LoginPageAssertions assertThat(LoginPage page) {
        return new LoginPageAssertions(page);
    }
    /**
     * Проверяет, что отображается сообщение об ошибке.
     *
     * @return этот же объект ассерта (для цепочки)
     */
    public LoginPageAssertions hasErrorMessage() {
        Assertions.assertTrue(page.isErrorDisplayed(),
                "Должно отображаться сообщение об ошибке");
        return this;
    }
    /**
     * Проверяет, что сообщение об ошибке содержит ожидаемый текст.
     *
     * @param expectedText ожидаемая подстрока
     * @return этот же объект ассерта
     */
    public LoginPageAssertions hasErrorMessageContaining(String expectedText) {
        String actual = page.getErrorMessage();
        Assertions.assertTrue(actual.contains(expectedText),
                "Сообщение об ошибке должно содержать: '" + expectedText + "', но получено: '" + actual + "'");
        return this;
    }
    /**
     * Проверяет, что в поле username введено ожидаемое значение.
     *
     * @param expectedUsername ожидаемое имя пользователя
     * @return этот же объект ассерта
     */
    public LoginPageAssertions hasUsername(String expectedUsername) {
        String actual = page.getEnteredUsername();
        Assertions.assertEquals(expectedUsername, actual,
                "Введенное имя пользователя не совпадает");
        return this;
    }
    /**
     * Проверяет сообщение валидации поля username.
     *
     * @param expected ожидаемое сообщение
     * @return этот же объект ассерта
     */
    public LoginPageAssertions hasUsernameValidationMessage(String expected) {
        String actual = page.getUsernameValidationMessage();
        Assertions.assertEquals(expected, actual,
                "Сообщение валидации для username не совпадает");
        return this;
    }
    /**
     * Проверяет сообщение валидации поля password.
     *
     * @param expected ожидаемое сообщение
     * @return этот же объект ассерта
     */
    public LoginPageAssertions hasPasswordValidationMessage(String expected) {
        String actual = page.getPasswordValidationMessage();
        Assertions.assertEquals(expected, actual,
                "Сообщение валидации для password не совпадает");
        return this;
    }
    /**
     * Проверяет, что серверная ошибка отсутствует.
     *
     * @return этот же объект ассерта
     */
    public LoginPageAssertions hasNoServerError() {
        Assertions.assertFalse(page.isServerErrorPresent(),
                "Не должно быть серверной ошибки");
        return this;
    }
    /**
     * Проверяет, что текущий URL содержит ожидаемую подстроку.
     *
     * @param expectedPart ожидаемая подстрока
     * @return этот же объект ассерта
     */
    public LoginPageAssertions urlContains(String expectedPart) {
        Assertions.assertTrue(page.getCurrentUrl().contains(expectedPart),
                "URL должен содержать: " + expectedPart);
        return this;
    }
}