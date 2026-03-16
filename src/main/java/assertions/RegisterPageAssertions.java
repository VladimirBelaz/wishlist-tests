package assertions;

import org.junit.jupiter.api.Assertions;
import pages.RegisterPage;
/**
 * Набор проверок для страницы регистрации.
 * Включает проверки серверных ошибок, сообщений валидации HTML5 и URL.
 */
public class RegisterPageAssertions {

    private final RegisterPage page;

    private RegisterPageAssertions(RegisterPage page) {
        this.page = page;
    }
    /**
     * Фабричный метод для создания экземпляра ассерта.
     *
     * @param page страница регистрации
     * @return объект ассерта
     */
    public static RegisterPageAssertions assertThat(RegisterPage page) {
        return new RegisterPageAssertions(page);
    }
    /**
     * Проверяет, что отображается сообщение об ошибке.
     *
     * @return этот же объект ассерта
     */
    public RegisterPageAssertions hasErrorMessage() {
        Assertions.assertTrue(page.isErrorDisplayed(),
                "Должно отображаться сообщение об ошибке");
        return this;
    }
    /**
     * Проверяет, что сообщение об ошибке содержит ожидаемый текст.
     *
     * @param expected ожидаемая подстрока
     * @return этот же объект ассерта
     */
    public RegisterPageAssertions hasErrorMessageContaining(String expected) {
        String actual = page.getErrorMessage();
        Assertions.assertTrue(actual.contains(expected),
                "Сообщение об ошибке должно содержать: '" + expected + "', но получено: '" + actual + "'");
        return this;
    }
    /**
     * Проверяет, что текущий URL содержит ожидаемую подстроку.
     *
     * @param expectedPart ожидаемая подстрока
     * @return этот же объект ассерта
     */
    public RegisterPageAssertions urlContains(String expectedPart) {
        Assertions.assertTrue(page.getCurrentUrl().contains(expectedPart),
                "URL должен содержать: " + expectedPart);
        return this;
    }
    /**
     * Проверяет сообщение валидации поля username.
     *
     * @param expected ожидаемое сообщение
     * @return этот же объект ассерта
     */
    public RegisterPageAssertions hasUsernameValidationMessage(String expected) {
        String actual = page.getUsernameValidationMessage();
        Assertions.assertEquals(expected, actual,
                "Сообщение валидации для username не совпадает");
        return this;
    }
    /**
     * Проверяет сообщение валидации поля email.
     *
     * @param expected ожидаемое сообщение
     * @return этот же объект ассерта
     */
    public RegisterPageAssertions hasEmailValidationMessage(String expected) {
        String actual = page.getEmailValidationMessage();
        Assertions.assertEquals(expected, actual,
                "Сообщение валидации для email не совпадает");
        return this;
    }
    /**
     * Проверяет сообщение валидации поля password.
     *
     * @param expected ожидаемое сообщение
     * @return этот же объект ассерта
     */
    public RegisterPageAssertions hasPasswordValidationMessage(String expected) {
        String actual = page.getPasswordValidationMessage();
        Assertions.assertEquals(expected, actual,
                "Сообщение валидации для password не совпадает");
        return this;
    }
    /**
     * Проверяет, что сообщение валидации поля email содержит ожидаемую подстроку.
     *
     * @param expected ожидаемая подстрока
     * @return этот же объект ассерта
     */
    public RegisterPageAssertions hasEmailValidationMessageContaining(String expected) {
        String actual = page.getEmailValidationMessage();
        Assertions.assertTrue(actual.toLowerCase().contains(expected.toLowerCase()),
                "Сообщение валидации email должно содержать: '" + expected + "'");
        return this;
    }
}