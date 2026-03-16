package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;
/**
 * Page Object для главной страницы со списками желаний (/wishlists).
 * Позволяет создавать, просматривать, удалять списки,
 * а также получать информацию о существующих списках.
 */
public class WishListDetailPage extends AbsBasePage {

    public WishListDetailPage(WebDriver driver) {
        super(driver, "");
    }

    // Локаторы
    private By listTitleBy = By.cssSelector("h2");
    private By listDescriptionBy = By.cssSelector("p");
    private By addGiftButtonBy = By.cssSelector(".btn-primary:first-of-type");
    private By deleteListButtonBy = By.cssSelector(".btn-danger");
    private By errorMessageBy = By.xpath("//*[contains(text(), 'Ошибка')]");

    // Локаторы для модального окна
    private By modalDialogBy = By.cssSelector(".modal-dialog");
    private By giftNameInputBy = By.cssSelector(".modal-content input[type='text']");
    private By giftDescriptionInputBy = By.cssSelector(".modal-content textarea");
    private By giftPriceInputBy = By.cssSelector(".modal-content input[type='number']");
    private By addGiftSubmitButtonBy = By.cssSelector(".modal-content button[type='submit']");
    private By giftCardsBy = By.cssSelector(".col .card");

    public void waitForPageToLoad() {
        logger.info("Ожидание загрузки страницы деталей списка");
        waiters.waitForElementVisible(listTitleBy);
        waiters.waitForElementVisible(addGiftButtonBy);
    }

    public String getListTitle() {
        WebElement titleElement = waiters.waitForElementVisible(listTitleBy);
        return titleElement.getText();
    }

    public String getListDescription() {
        WebElement descElement = waiters.waitForElementVisible(listDescriptionBy);
        return descElement.getText();
    }

    public void clickAddGift() {
        logger.info("Клик по кнопке 'Добавить подарок'");
        WebElement addButton = waiters.waitForElementClickable(addGiftButtonBy);
        addButton.click();

        waiters.waitForElementVisible(modalDialogBy);
        waiters.waitForElementVisible(giftNameInputBy);
        logger.info("Модальное окно добавления подарка открылось");
    }

    public void fillGiftName(String name) {
        logger.info("Заполнение названия подарка: {}", name);
        WebElement input = waiters.waitForElementVisible(giftNameInputBy);
        input.clear();
        input.sendKeys(name);
    }

    public void fillGiftDescription(String description) {
        logger.info("Заполнение описания подарка: {}", description);
        WebElement textarea = waiters.waitForElementVisible(giftDescriptionInputBy);
        textarea.clear();
        textarea.sendKeys(description);
    }

    public void fillGiftPrice(String price) {
        logger.info("Заполнение цены подарка: {}", price);
        WebElement priceInput = waiters.waitForElementVisible(giftPriceInputBy);
        priceInput.clear();
        priceInput.sendKeys(price);
    }

    public String getErrorMessage() {
        logger.info("Получение текста сообщения об ошибке");
        try {
            WebElement errorElement = waiters.waitForElementVisible(errorMessageBy);
            String errorText = errorElement.getText();
            logger.info("Текст ошибки: {}", errorText);
            return errorText;
        } catch (Exception e) {
            logger.warn("Сообщение об ошибке не найдено");
            return "";
        }
    }

    public boolean submitAddGift() {
        logger.info("Отправка формы добавления подарка");
        WebElement submitButton = waiters.waitForElementClickable(addGiftSubmitButtonBy);
        submitButton.click();

        waiters.waitForCondition(driver ->
                driver.findElements(modalDialogBy).isEmpty() ||
                        driver.findElements(errorMessageBy).size() > 0
        );

        if (driver.findElements(errorMessageBy).size() > 0) {
            String errorText = getErrorMessage();  // ← используем новый метод
            logger.error("Получена ошибка от сервера: {}", errorText);


            if (!driver.findElements(modalDialogBy).isEmpty()) {
                WebElement closeButton = driver.findElement(By.cssSelector(".modal-content .btn-close"));
                if (closeButton.isDisplayed()) {
                    closeButton.click();
                }
            }

            return false;
        }

        waiters.waitForElementInvisible(modalDialogBy);
        logger.info("Модальное окно закрылось");
        return true;
    }

    public boolean addGift(String name, String description, String price) {
        logger.info("Начинаем добавление подарка: {}", name);

        clickAddGift();
        fillGiftName(name);
        fillGiftDescription(description);
        fillGiftPrice(price);

        boolean isSuccess = submitAddGift();

        if (isSuccess) {
            boolean giftAdded = waitForGiftToBeAdded();
            if (giftAdded) {
                logger.info("Подарок успешно добавлен ✅");
                return true;
            } else {
                logger.error("Подарок не появился в списке после успешного ответа сервера");
                return false;
            }
        } else {
            logger.error("Подарок НЕ добавлен из-за ошибки на сервере ❌");
            return false;
        }
    }

    private boolean waitForGiftToBeAdded() {
        int currentCount = getGiftsCount();
        logger.debug("Текущее количество подарков: {}", currentCount);

        boolean giftAdded = waiters.waitForCondition(driver ->
                driver.findElements(giftCardsBy).size() > currentCount
        );

        if (giftAdded) {
            int newCount = getGiftsCount();
            logger.debug("Новое количество подарков: {}", newCount);
            return true;
        }

        return false;
    }

    public int getGiftsCount() {
        return driver.findElements(giftCardsBy).size();
    }

    public List<WebElement> getGifts() {
        return waiters.waitForElementsPresent(giftCardsBy);
    }

    public String getGiftName(int index) {
        List<WebElement> gifts = getGifts();
        if (gifts.isEmpty() || index >= gifts.size()) {
            return "";
        }
        return gifts.get(index).findElement(By.cssSelector(".card-title")).getText();
    }

}