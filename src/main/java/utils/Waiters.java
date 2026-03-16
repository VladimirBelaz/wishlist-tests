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
     * Ожидание видимости элемента.
     *
     * @param element элемент
     * @return тот же элемент, если он стал видимым
     */
    public WebElement waitForElementVisible(WebElement element) {
        return wait.until(ExpectedConditions.visibilityOf(element));
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
     * Ожидание кликабельности элемента.
     *
     * @param element элемент
     * @return тот же элемент, если он стал кликабельным
     */
    public WebElement waitForElementClickable(WebElement element) {
        return wait.until(ExpectedConditions.elementToBeClickable(element));
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
    /**
     * Ожидание присутствия элемента в DOM.
     *
     * @param locator локатор
     * @return элемент, когда он появится в DOM
     */
    public WebElement waitForElementPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
    }
    /**
     * Ожидание видимости всех элементов по локатору.
     *
     * @param locator локатор
     * @return список видимых элементов
     */
    public List<WebElement> waitForElementsVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }
    /**
     * Ожидание присутствия всех элементов в DOM.
     *
     * @param locator локатор
     * @return список элементов
     */
    public List<WebElement> waitForElementsPresent(By locator) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }
    /**
     * Ожидание текста в элементе.
     *
     * @param element элемент
     * @param text    ожидаемый текст
     * @return true, если текст появился
     */
    public boolean waitForTextToBePresentInElement(WebElement element, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElement(element, text));
    }

    public boolean waitForTextToBePresentInElement(By locator, String text) {
        return wait.until(ExpectedConditions.textToBePresentInElementLocated(locator, text));
    }
    /**
     * Ожидание значения атрибута.
     *
     * @param element   элемент
     * @param attribute атрибут
     * @param value     ожидаемое значение
     * @return true, если атрибут приобрёл нужное значение
     */
    public boolean waitForAttributeToBe(WebElement element, String attribute, String value) {
        return wait.until(ExpectedConditions.attributeToBe(element, attribute, value));
    }
    /**
     * Ожидание исчезновения элемента.
     *
     * @param element элемент
     * @return true, если элемент исчез
     */
    public boolean waitForElementInvisible(WebElement element) {
        return wait.until(ExpectedConditions.invisibilityOf(element));
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
     * Ожидание определённого количества элементов.
     *
     * @param locator       локатор
     * @param expectedCount ожидаемое количество
     * @return true, когда количество достигнуто
     */
    public boolean waitForNumberOfElementsToBe(By locator, int expectedCount) {
        return waitForCondition(ExpectedConditions.numberOfElementsToBe(locator, expectedCount));
    }
    /**
     * Возвращает список элементов, когда их количество станет равно ожидаемому.
     *
     * @param locator       локатор
     * @param expectedCount ожидаемое количество
     * @return список элементов, если количество достигнуто, иначе null
     */
    public List<WebElement> waitForElementsCount(By locator, int expectedCount) {
        return wait.until(driver -> {
            List<WebElement> elements = driver.findElements(locator);
            return elements.size() == expectedCount ? elements : null;
        });
    }
    /**
     * Ожидание появления модального окна (по классу .modal-content).
     *
     * @return элемент модального окна
     */
    public WebElement waitForModalToAppear() {
        return waitForElementVisible(By.cssSelector(".modal-content"));
    }
    /**
     * Ожидание исчезновения модального окна.
     *
     * @return true, если окно исчезло
     */
    public boolean waitForModalToDisappear() {
        return waitForElementInvisible(By.cssSelector(".modal-content"));
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
     * Ожидание появления alert.
     *
     * @return объект Alert, когда он появится
     */
    public Alert waitForAlert() {
        return wait.until(ExpectedConditions.alertIsPresent());
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
    /**
     * Ожидание, что элемент содержит определённый текст.
     *
     * @param element      элемент
     * @param expectedText ожидаемый текст
     * @return true, если текст содержится
     */
    public boolean waitForElementHasText(WebElement element, String expectedText) {
        return wait.until(driver -> element.getText().contains(expectedText));
    }
}