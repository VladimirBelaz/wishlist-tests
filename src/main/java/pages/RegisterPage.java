package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object для страницы регистрации (/register).
 * Содержит методы регистрации нового пользователя,
 * получения сообщений об ошибках сервера и браузерной валидации.
 */
public class RegisterPage extends AbsBasePage {

    // Локаторы
    private final By usernameInputBy = By.cssSelector("input[type='text']");
    private final By emailInputBy = By.cssSelector("input[type='email']");
    private final By passwordInputBy = By.cssSelector("input[type='password']");
    private final By registerButtonBy = By.cssSelector("button[type='submit']");
    private final By errorAlertBy = By.cssSelector(".alert.alert-danger");

    /**
     * Конструктор страницы регистрации.
     *
     * @param driver экземпляр WebDriver
     */
    public RegisterPage(WebDriver driver) {
        super(driver, "/register");
    }

    /**
     * Заполняет поле имени пользователя.
     *
     * @param username имя пользователя
     */
    public void fillUsername(String username) {
        logger.info("Заполнение имени пользователя: {}", username);
        WebElement input = waiters.waitForElementVisible(usernameInputBy);
        input.clear();
        input.sendKeys(username);
    }

    /**
     * Заполняет поле email.
     *
     * @param email email
     */
    public void fillEmail(String email) {
        logger.info("Заполнение email: {}", email);
        WebElement input = waiters.waitForElementVisible(emailInputBy);
        input.clear();
        input.sendKeys(email);
    }

    /**
     * Заполняет поле пароля.
     *
     * @param password пароль
     */
    public void fillPassword(String password) {
        logger.info("Заполнение пароля");
        WebElement input = waiters.waitForElementVisible(passwordInputBy);
        input.clear();
        input.sendKeys(password);
    }

    /**
     * Кликает по кнопке "Зарегистрироваться".
     */
    public void clickRegister() {
        logger.info("Клик по кнопке 'Зарегистрироваться'");
        WebElement button = waiters.waitForElementClickable(registerButtonBy);
        button.click();
    }

    /**
     * Выполняет регистрацию с указанными данными.
     *
     * @param username имя пользователя
     * @param email    email
     * @param password пароль
     */
    public void register(String username, String email, String password) {
        fillUsername(username);
        fillEmail(email);
        fillPassword(password);
        clickRegister();
    }

    /**
     * Проверяет, отображается ли сообщение об ошибке (серверное).
     *
     * @return true, если ошибка видна
     */
    public boolean isErrorDisplayed() {
        try {
            return waiters.waitForElementVisible(errorAlertBy).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Возвращает текст сообщения об ошибке.
     *
     * @return текст ошибки
     */
    public String getErrorMessage() {
        WebElement alert = waiters.waitForElementVisible(errorAlertBy);
        return alert.getText();
    }

    /**
     * Возвращает сообщение валидации браузера для поля username.
     *
     * @return сообщение валидации
     */
    public String getUsernameValidationMessage() {
        WebElement input = driver.findElement(usernameInputBy);
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", input);
    }

    /**
     * Возвращает сообщение валидации браузера для поля email (HTML5).
     *
     * @return сообщение валидации
     */
    public String getEmailValidationMessage() {
        WebElement input = driver.findElement(emailInputBy);
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", input);
    }

    /**
     * Возвращает сообщение валидации браузера для поля password.
     *
     * @return сообщение валидации
     */
    public String getPasswordValidationMessage() {
        WebElement input = driver.findElement(passwordInputBy);
        return (String) ((JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", input);
    }

    /**
     * Проверяет наличие серверной ошибки без ожидания.
     *
     * @return true, если элемент ошибки присутствует на странице
     */
    public boolean isServerErrorPresent() {
        List<WebElement> errors = driver.findElements(errorAlertBy);
        return !errors.isEmpty();
    }
}