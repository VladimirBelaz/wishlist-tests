package pages;

import exceptions.WishListNotFoundException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import java.util.List;
/**
 * Page Object для страницы конкретного списка желаний.
 * Содержит методы для добавления подарков, удаления списка,
 * получения информации о подарках и ожидания загрузки страницы.
 */
public class WishListsPage extends AbsBasePage {

    public WishListsPage(WebDriver driver) {
        super(driver, "/wishlists");
    }

    @FindBy(css = ".btn-primary")
    private WebElement createNewListButton;

    @FindBy(css = "h2")
    private WebElement pageTitle;

    private By wishListCardsBy = By.cssSelector(".col .card");

    private int countBeforeCreation = 0;

    @Override
    public void waitForPageToLoad() {
        logger.info("Ожидание загрузки страницы списков");

        // Ждем заголовок и кнопку - они есть всегда
        waiters.waitForElementVisible(pageTitle);
        waiters.waitForElementVisible(createNewListButton);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int cardsCount = getWishListsCount();
        logger.info("На странице найдено {} списков", cardsCount);
    }

    public int getWishListsCount() {
        return driver.findElements(wishListCardsBy).size();
    }

    public void clickCreateNewList() {
        logger.info("Клик по кнопке 'Создать новый список'");
        waiters.waitForElementClickable(createNewListButton).click();
        waiters.waitForElementVisible(By.cssSelector(".modal-content input[type='text']"));
    }

    public void fillListTitle(String title) {
        logger.info("Заполнение названия списка: {}", title);
        WebElement input = waiters.waitForElementVisible(By.cssSelector(".modal-content input[type='text']"));
        input.clear();
        input.sendKeys(title);
    }

    public void fillListDescription(String description) {
        logger.info("Заполнение описания списка: {}", description);
        WebElement textarea = waiters.waitForElementVisible(By.cssSelector(".modal-content textarea"));
        textarea.clear();
        textarea.sendKeys(description);
    }

    public void submitCreateList() {
        logger.info("Отправка формы создания списка");
        WebElement submitButton = waiters.waitForElementClickable(By.cssSelector(".modal-content button[type='submit']"));
        submitButton.click();
        waiters.waitForElementInvisible(By.cssSelector(".modal-content"));
    }

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

        // Прокручиваем к кнопке и кликаем
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block: 'center'}); arguments[0].click();",
                deleteButton
        );

        logger.info("Клик по кнопке 'Удалить' выполнен");
    }

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

    public List<WebElement> getWishLists() {
        return driver.findElements(wishListCardsBy);
    }

    public String getWishListTitle(int index) {
        List<WebElement> cards = getWishLists();
        if (cards.isEmpty() || index >= cards.size()) {
            logger.warn("Список с индексом {} не найден", index);
            return "";
        }
        String title = cards.get(index).findElement(By.cssSelector(".card-title")).getText();
        return title;
    }


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

    public void clickLastWishListView() {
        int totalLists = getWishListsCount();
        if (totalLists > 0) {
            clickViewWishList(totalLists - 1);
        } else {
            throw new RuntimeException("Нет списков для просмотра");
        }
    }



    public boolean isWishListDisplayed(String title) {
        logger.info("Проверка наличия списка с названием: {}", title);

        List<WebElement> cards = getWishLists();

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
}