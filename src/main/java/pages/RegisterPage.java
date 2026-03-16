package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
/**
 * Page Object для страницы регистрации (/register).
 * Содержит методы регистрации нового пользователя,
 * получения сообщений об ошибках сервера и браузерной валидации.
 */
public class RegisterPage extends AbsBasePage {
    /**
     * Конструктор страницы регистрации.
     *
     * @param driver экземпляр WebDriver
     */
    public RegisterPage(WebDriver driver) {
        super(driver, "/register");
    }

    // Локаторы
    private By usernameInputBy = By.cssSelector("input[type='text']");
    private By emailInputBy = By.cssSelector("input[type='email']");
    private By passwordInputBy = By.cssSelector("input[type='password']");
    private By registerButtonBy = By.cssSelector("button[type='submit']");
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
     * Возвращает сообщение валидации браузера для поля username (HTML5).
     *
     * @return сообщение валидации
     */
    public String getUsernameValidationMessage() {
        WebElement input = driver.findElement(usernameInputBy);
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", input);
    }
    /**
     * Возвращает сообщение валидации браузера для поля email (HTML5).
     *
     * @return сообщение валидации
     */
    public String getEmailValidationMessage() {
        WebElement input = driver.findElement(emailInputBy);
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", input);
    }
    /**
     * Возвращает сообщение валидации браузера для поля password (HTML5).
     *
     * @return сообщение валидации
     */
    public String getPasswordValidationMessage() {
        WebElement input = driver.findElement(passwordInputBy);
        return (String) ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript("return arguments[0].validationMessage;", input);
    }

}