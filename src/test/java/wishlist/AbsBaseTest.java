package wishlist;

import exceptions.AuthenticationException;
import factory.WebDriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import pages.WishListsPage;
import utils.Waiters;
import java.util.ArrayList;
import java.util.List;
/**
 * Базовый класс для всех тестов.
 * Инициализирует WebDriver, Waiters, страницы, а также содержит
 * вспомогательные методы для логина и создания тестовых данных.
 */
public abstract class AbsBaseTest {
    protected WebDriver driver;
    protected Logger logger;
    protected Waiters waiters;
    protected LoginPage loginPage;
    protected WishListsPage wishListsPage;

    protected static final String USERNAME = System.getProperty("USERNAME", "vladimirbv");
    protected static final String PASSWORD = System.getProperty("PASSWORD", "E17wfz25Zm");
    protected static final String BASE_URL = System.getProperty("BASE_URL", "https://wishlist.otus.kartushin.su");

    @BeforeEach
    public void setUp() {
        driver = new WebDriverFactory().create();
        logger = LogManager.getLogger(this.getClass());
        waiters = new Waiters(driver);

        loginPage = new LoginPage(driver);
        wishListsPage = new WishListsPage(driver);

        driver.manage().window().maximize();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected void login() {
        loginPage.open();
        loginPage.login(USERNAME, PASSWORD);
        boolean loggedIn = waiters.waitForUrlContains("/wishlists");
        if (!loggedIn) {
            throw AuthenticationException.invalidCredentials(USERNAME);
        }
        logger.info("Успешный логин пользователя: {}", USERNAME);
    }

    protected void login(String username, String password) {
        loginPage.open();
        loginPage.login(username, password);
        logger.info("Логин пользователя: {}", username);
    }

    protected String createTestWishList(String titlePrefix) {
        String title = titlePrefix + System.currentTimeMillis();
        wishListsPage.open();
        wishListsPage.createWishList(title, "Тестовое описание");
        logger.info("Создан тестовый список: {}", title);
        return title;
    }

    protected void ensureAtLeastOneWishListExists() {
        int currentCount = wishListsPage.getWishListsCount();
        if (currentCount == 0) {
            logger.info("Списков нет, создаём один тестовый список");
            createTestWishList("Тестовый список для удаления");
        } else {
            logger.info("Списки уже есть ({} шт.), продолжаем", currentCount);
        }
    }

    protected void ensureWishListsExist(int requiredCount) {
        int currentCount = wishListsPage.getWishListsCount();
        if (currentCount < requiredCount) {
            int toCreate = requiredCount - currentCount;
            logger.info("Создаём ещё {} тестовых списков", toCreate);
            for (int i = 1; i <= toCreate; i++) {
                createTestWishList("Тестовый список " + (currentCount + i));
            }
        }
    }

    protected void refreshAndWait() {
        driver.navigate().refresh();
        wishListsPage.waitForPageToLoad();
    }

    protected List<String> getAllWishListTitles() {
        List<String> titles = new ArrayList<>();
        int count = wishListsPage.getWishListsCount();
        for (int i = 0; i < count; i++) {
            titles.add(wishListsPage.getWishListTitle(i));
        }
        logger.info("Найдены списки: {}", titles);
        return titles;
    }

    protected void deleteAllWishLists() {
        int count = wishListsPage.getWishListsCount();
        logger.info("Удаляем все списки (всего: {})", count);

        for (int i = 0; i < count; i++) {
            wishListsPage.clickDeleteWishList(0);
            driver.navigate().refresh();
            wishListsPage.waitForPageToLoad();
        }
    }

    protected void openWishListsPage() {
        wishListsPage.open();
    }
}