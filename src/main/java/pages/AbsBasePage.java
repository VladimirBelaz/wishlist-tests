package pages;

import commons.AbsCommon;
import org.openqa.selenium.WebDriver;
/**
 * Базовый класс для всех Page Object.
 * Содержит общие методы: открытие страницы, получение URL,
 * а также инициализацию WebDriver и Waiters.
 */
public abstract class AbsBasePage extends AbsCommon {
    /** Базовый URL приложения, берётся из системного свойства "BASE_URL". */
    protected final String BASE_URL = System.getProperty("BASE_URL", "https://wishlist.otus.kartushin.su");
    /** Путь к конкретной странице (например, "/login"). */
    protected final String path;
    /**
     * Конструктор для инициализации страницы.
     *
     * @param driver экземпляр WebDriver
     * @param path   путь к странице
     */
    public AbsBasePage(WebDriver driver, String path) {
        super(driver);
        this.path = path;
    }
    /**
     * Открывает страницу по составному URL (BASE_URL + path).
     */
    public void open() {
        driver.get(BASE_URL + path);
    }
    /**
     * Возвращает текущий URL страницы.
     *
     * @return текущий URL
     */
    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
    /**
     * Ожидает полной загрузки страницы (по умолчанию – ничего не делает,
     * переопределяется в наследниках при необходимости).
     */
    public void waitForPageToLoad() {
        logger.info("Ожидание загрузки страницы по умолчанию");
        waiters.waitForPageToLoad();
    }
}