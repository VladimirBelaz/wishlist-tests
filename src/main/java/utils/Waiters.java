package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
/**
 * Утилитный класс для явных ожиданий WebDriver.
 * Содержит методы ожидания видимости, кликабельности,
 * присутствия элементов, изменения URL и другие.
 */
public class Waiters {
    private WebDriver driver;
    private WebDriverWait wait;
    private Logger logger;
    private static final int DEFAULT_TIMEOUT = 10;
    /**
     * Конструктор с таймаутом по умолчанию 10 секунд.
     *
     * @param driver экземпляр WebDriver
     */
    public Waiters(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        this.logger = LogManager.getLogger(Waiters.class);
    }
    /**
     * Конструктор с пользовательским таймаутом.
     *
     * @param driver         экземпляр WebDriver
     * @param timeoutSeconds таймаут в секундах
     */
    public Waiters(WebDriver driver, int timeoutSeconds) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
    }
    /**
     * Ожидание выполнения условия.
     *
     * @param condition условие для ожидания
     * @return true, если условие выполнилось, иначе false
     */
    public boolean waitForCondition(ExpectedCondition<?> condition) {
        try {
            wait.until(condition);
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }
    /**
     * Ожидание видимости элемента по локатору.
     *
     * @param locator локатор
     * @return видимый элемент
     */
    public WebElement waitForElementVisible(By locator) {
        logger.info("Ожидание видимости элемента: {}", locator);
        try {
            WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            logger.info("Элемент найден: {}", locator);
            return element;
        } catch (TimeoutException e) {
            logger.error("Элемент не найден за {} секунд: {}", DEFAULT_TIMEOUT, locator);
            throw e;
        }
    }
    /**
     * Ожидание кликабельности элемента по локатору.
     *
     * @param locator локатор
     * @return кликабельный элемент
     */
    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public List<WebElement> waitForElementsPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    /**
     * Ожидание исчезновения элемента по локатору.
     *
     * @param locator локатор
     * @return true, если элемент исчез
     */
    public boolean waitForElementInvisible(By locator) {
        return wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }
    /**
     * Ожидание загрузки страницы (document.readyState == 'complete').
     *
     * @return true, когда страница загружена
     */
    public boolean waitForPageToLoad() {
        ExpectedCondition<Boolean> pageLoadCondition = driver ->
                ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
        return wait.until(pageLoadCondition);
    }
    /**
     * Ожидание, что URL содержит определённую подстроку.
     *
     * @param fraction подстрока
     * @return true, когда URL содержит подстроку
     */
    public boolean waitForUrlContains(String fraction) {
        return wait.until(ExpectedConditions.urlContains(fraction));
    }
}