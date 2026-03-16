package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
/**
 * Page Object для страницы входа (/login).
 * Предоставляет методы для заполнения формы логина,
 * получения сообщений об ошибках и проверки состояния элементов.
 */
public class LoginPage extends AbsBasePage {
    /**
     * Конструктор страницы логина.
     *
     * @param driver экземпляр WebDriver
     */
    public LoginPage(WebDriver driver) {
        super(driver, "/login");
    }

    // Локаторы
    private By usernameInputBy = By.cssSelector("input[type='text']");
    private By passwordInputBy = By.cssSelector("input[type='password']");
    private By loginButtonBy = By.cssSelector("button[type='submit']");
    private By errorAlertBy = By.cssSelector(".alert.alert-danger");
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
     * Кликает по кнопке "Войти".
     */
    public void clickLogin() {
        logger.info("Клик по кнопке 'Войти'");
        waiters.waitForElementClickable(loginButtonBy).click();
    }
    /**
     * Выполняет вход с указанными учётными данными.
     *
     * @param username имя пользователя
     * @param password пароль
     */
    public void login(String username, String password) {
        fillUsername(username);
        fillPassword(password);
        clickLogin();
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
        WebElement errorAlert = waiters.waitForElementVisible(errorAlertBy);
        return errorAlert.getText();
    }
    /**
     * Возвращает введённое значение поля username.
     *
     * @return текст в поле username
     */
    public String getEnteredUsername() {
        WebElement input = driver.findElement(usernameInputBy);
        return input.getAttribute("value");
    }
    /**
     * Возвращает сообщение валидации браузера для поля username (HTML5).
     *
     * @return сообщение валидации
     */
    public String getUsernameValidationMessage() {
        WebElement input = driver.findElement(passwordInputBy);
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
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", input);
    }
    /**
     * Проверяет наличие серверной ошибки (элемент .alert.alert-danger).
     * Используется для быстрой проверки без ожидания.
     *
     * @return true, если элемент ошибки присутствует на странице
     */
    public boolean isServerErrorPresent() {
        List<WebElement> errors = driver.findElements(errorAlertBy);
        return !errors.isEmpty();
    }
}