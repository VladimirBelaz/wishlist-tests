package pages;

import exceptions.WishListNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Page Object для главной страницы со списками желаний (/wishlists).
 * Позволяет создавать, просматривать, удалять списки,
 * а также получать информацию о существующих списках.
 */
public class WishListsPage extends AbsBasePage {
    /**
     * Конструктор страницы списков желаний.
     *
     * @param driver экземпляр WebDriver
     */
    public WishListsPage(WebDriver driver) {
        super(driver, "/wishlists");
    }

    // Локаторы
    private final By createNewListButtonBy = By.cssSelector(".btn-primary");
    private final By pageTitleBy = By.cssSelector("h2");
    private final By wishListCardsBy = By.cssSelector(".col .card");
    private final By modalTitleInputBy = By.cssSelector(".modal-content input[type='text']");
    private final By modalDescriptionInputBy = By.cssSelector(".modal-content textarea");
    private final By modalSubmitButtonBy = By.cssSelector(".modal-content button[type='submit']");

    private int countBeforeCreation = 0;

    @Override
    public void waitForPageToLoad() {
        logger.info("Ожидание загрузки страницы списков");
        waiters.waitForElementVisible(pageTitleBy);
        waiters.waitForElementVisible(createNewListButtonBy);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int cardsCount = getWishListsCount();
        logger.info("На странице найдено {} списков", cardsCount);
    }

    /**
     * Возвращает текущее количество списков на странице.
     *
     * @return количество списков
     */
    public int getWishListsCount() {
        return driver.findElements(wishListCardsBy).size();
    }

    /**
     * Возвращает все карточки списков (с ожиданием).
     *
     * @return список WebElement карточек
     */
    public List<WebElement> getWishLists() {
        return waiters.waitForElementsPresent(wishListCardsBy);
    }

    /**
     * Возвращает название списка по индексу.
     *
     * @param index индекс списка (0-based)
     * @return название списка
     */
    public String getWishListTitle(int index) {
        List<WebElement> cards = getWishLists();
        if (cards.isEmpty() || index >= cards.size()) {
            logger.warn("Список с индексом {} не найден", index);
            return "";
        }
        String title = cards.get(index).findElement(By.cssSelector(".card-title")).getText();
        return title;
    }

    /**
     * Кликает по кнопке "Создать новый список".
     */
    public void clickCreateNewList() {
        logger.info("Клик по кнопке 'Создать новый список'");
        waiters.waitForElementClickable(createNewListButtonBy).click();
        waiters.waitForElementVisible(modalTitleInputBy);
    }

    /**
     * Заполняет название списка в модальном окне.
     *
     * @param title название списка
     */
    public void fillListTitle(String title) {
        logger.info("Заполнение названия списка: {}", title);
        WebElement input = waiters.waitForElementVisible(modalTitleInputBy);
        input.clear();
        input.sendKeys(title);
    }

    /**
     * Заполняет описание списка в модальном окне.
     *
     * @param description описание
     */
    public void fillListDescription(String description) {
        logger.info("Заполнение описания списка: {}", description);
        WebElement textarea = waiters.waitForElementVisible(modalDescriptionInputBy);
        textarea.clear();
        textarea.sendKeys(description);
    }

    /**
     * Отправляет форму создания списка.
     */
    public void submitCreateList() {
        logger.info("Отправка формы создания списка");
        WebElement submitButton = waiters.waitForElementClickable(modalSubmitButtonBy);
        submitButton.click();
        waiters.waitForElementInvisible(By.cssSelector(".modal-content"));
    }

    /**
     * Создаёт новый список желаний.
     *
     * @param title       название
     * @param description описание
     */
    public void createWishList(String title, String description) {
        logger.info("Создание нового списка: {}", title);

        waitForPageToLoad();

        countBeforeCreation = getWishListsCount();
        logger.info("Количество списков до создания: {}", countBeforeCreation);

        clickCreateNewList();
        fillListTitle(title);
        fillListDescription(description);
        submitCreateList();

        waitForWishListToBeCreated();
    }

    /**
     * Ожидает, пока количество списков станет равным countBeforeCreation + 1.
     */
    public void waitForWishListToBeCreated() {
        logger.info("Ожидание создания нового списка. Было: {}, ожидаем: {}",
                countBeforeCreation, countBeforeCreation + 1);

        boolean created = waiters.waitForCondition(driver ->
                driver.findElements(wishListCardsBy).size() == countBeforeCreation + 1
        );

        if (created) {
            int newCount = getWishListsCount();
            logger.info("Новый список появился. Теперь списков: {}", newCount);
        } else {
            logger.error("Новый список НЕ появился за время ожидания");
        }
    }

    /**
     * Кликает по кнопке "Просмотр" для списка с указанным индексом.
     *
     * @param index индекс списка
     */
    public void clickViewWishList(int index) {
        logger.info("Клик по кнопке 'Просмотр' для списка с индексом: {}", index);

        waitForPageToLoad();

        List<WebElement> cards = getWishLists();

        if (cards.isEmpty()) {
            throw new WishListNotFoundException("Нет ни одного списка для просмотра");
        }

        if (index >= cards.size()) {
            throw new WishListNotFoundException(index);
        }

        WebElement card = cards.get(index);
        String currentUrl = driver.getCurrentUrl();

        WebElement link = card.findElement(By.cssSelector("a[href*='/wishlists/']"));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'});",
                link
        );

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        link.click();

        boolean urlChanged = waiters.waitForCondition(driver ->
                !driver.getCurrentUrl().equals(currentUrl) &&
                        driver.getCurrentUrl().contains("/wishlists/")
        );

        if (!urlChanged) {
            logger.error("URL не изменился после клика. Текущий URL: {}", driver.getCurrentUrl());
            throw new RuntimeException("Не удалось перейти на страницу списка");
        }

        logger.info("Перешли на страницу списка: {}", driver.getCurrentUrl());
    }

    /**
     * Кликает по кнопке "Просмотр" для последнего созданного списка.
     */
    public void clickLastWishListView() {
        int totalLists = getWishListsCount();
        if (totalLists > 0) {
            clickViewWishList(totalLists - 1);
        } else {
            throw new RuntimeException("Нет списков для просмотра");
        }
    }

    /**
     * Кликает по кнопке "Удалить" для списка с указанным индексом.
     *
     * @param index индекс списка
     */
    public void clickDeleteWishList(int index) {
        logger.info("Клик по кнопке 'Удалить' для списка с индексом: {}", index);

        List<WebElement> cards = getWishLists();

        if (cards.isEmpty()) {
            throw new WishListNotFoundException("Нет ни одного списка для удаления");
        }

        if (index >= cards.size()) {
            throw new WishListNotFoundException(index);
        }

        WebElement card = cards.get(index);
        WebElement deleteButton = card.findElement(By.cssSelector(".btn-danger"));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();",
                deleteButton
        );

        logger.info("Клик по кнопке 'Удалить' выполнен");
    }

    /**
     * Проверяет, отображается ли на странице список с заданным названием.
     *
     * @param title название списка
     * @return true, если список найден
     */
    public boolean isWishListDisplayed(String title) {
        logger.info("Проверка наличия списка с названием: {}", title);

        List<WebElement> cards = driver.findElements(wishListCardsBy);

        for (WebElement card : cards) {
            WebElement titleElement = card.findElement(By.cssSelector(".card-title"));
            String currentTitle = titleElement.getText();

            if (currentTitle.equals(title)) {
                logger.info("Список с названием '{}' найден", title);
                return true;
            }
        }

        logger.info("Список с названием '{}' не найден", title);
        return false;
    }

    /**
     * Ожидает, пока количество списков станет равным expectedCount.
     *
     * @param expectedCount ожидаемое количество
     */
    public void waitForWishListCount(int expectedCount) {
        logger.info("Ожидание количества списков: {}", expectedCount);
        waiters.waitForCondition(driver ->
                driver.findElements(wishListCardsBy).size() == expectedCount
        );
    }
}