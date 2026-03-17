package commons;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import utils.Waiters;
/**
 * Базовый класс для всех классов, работающих с WebDriver.
 * Содержит общие поля и методы: driver, actions, waiters, logger,
 * а также обёртки для поиска элементов.
 */
public abstract class AbsCommon {
    /** Экземпляр WebDriver. */
    protected WebDriver driver;
    /** Объект для выполнения сложных действий (наведение, перетаскивание и т.д.). */
    protected Actions actions;
    /** Утилита для явных ожиданий. */
    protected Waiters waiters;
    /** Логгер для записи сообщений. */
    protected Logger logger;  //
    /**
     * Конструктор, инициализирующий driver, actions, waiters, logger и PageFactory.
     *
     * @param driver экземпляр WebDriver
     */
    public AbsCommon(WebDriver driver) {
        this.driver = driver;
        this.actions = new Actions(driver);
        this.waiters = new Waiters(driver);
        this.logger = LogManager.getLogger(this.getClass());

        PageFactory.initElements(driver, this);
    }
}