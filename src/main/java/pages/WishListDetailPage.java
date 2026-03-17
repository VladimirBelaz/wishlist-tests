package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.util.List;

public class WishListDetailPage extends AbsBasePage {
    /**
     * Page Object для страницы конкретного списка желаний.
     * Содержит методы для добавления подарков, удаления списка,
     * получения информации о подарках и ожидания загрузки страницы.
     */
    public WishListDetailPage(WebDriver driver) {
        super(driver, "");
    }

    // Локаторы страницы
    private final By listTitleBy = By.cssSelector("h2");
    private final By listDescriptionBy = By.cssSelector("p");
    private final By addGiftButtonBy = By.cssSelector(".btn-primary:first-of-type");
    private final By deleteListButtonBy = By.cssSelector(".btn-danger");
    private final By errorMessageBy = By.xpath("//*[contains(text(), 'Ошибка')]");

    // Локаторы модального окна
    private final By modalDialogBy = By.cssSelector(".modal-dialog");
    private final By giftNameInputBy = By.cssSelector(".modal-content input[type='text']");          // поле "Название"
    private final By giftDescriptionInputBy = By.cssSelector(".modal-content textarea");              // поле "Описание"
    private final By giftStoreUrlInputBy = By.cssSelector("input[placeholder='https://example.com/product']"); // поле "Ссылка на магазин"
    private final By giftPriceInputBy = By.cssSelector(".modal-content input[type='number']");        // поле "Цена"
    private final By giftImageUrlInputBy = By.cssSelector("input[placeholder='https://example.com/image.jpg']"); // поле "Ссылка на изображение"
    private final By addGiftSubmitButtonBy = By.cssSelector(".modal-content button[type='submit']"); // кнопка "Добавить"
    // Локатор карточек подарков
    private final By giftCardsBy = By.cssSelector(".col .card");

    /**
     * Ожидает загрузки страницы (появление заголовка и кнопки добавления).
     */
    public void waitForPageToLoad() {
        logger.info("Ожидание загрузки страницы деталей списка");
        waiters.waitForElementVisible(listTitleBy);
        waiters.waitForElementVisible(addGiftButtonBy);
    }
    /**
     * Возвращает заголовок списка.
     *
     * @return заголовок
     */
    public String getListTitle() {
        WebElement titleElement = waiters.waitForElementVisible(listTitleBy);
        return titleElement.getText();
    }
    /**
     * Возвращает описание списка.
     *
     * @return описание
     */
    public String getListDescription() {
        WebElement descElement = waiters.waitForElementVisible(listDescriptionBy);
        return descElement.getText();
    }
    /**
     * Кликает по кнопке "Добавить подарок" и ожидает появления модального окна.
     */
    public void clickAddGift() {
        logger.info("Клик по кнопке 'Добавить подарок'");
        WebElement addButton = waiters.waitForElementClickable(addGiftButtonBy);
        addButton.click();

        waiters.waitForElementVisible(modalDialogBy);
        waiters.waitForElementVisible(giftNameInputBy);
        logger.info("Модальное окно добавления подарка открылось");
    }
    /**
     * Заполняет поле названия подарка.
     *
     * @param name название
     */
    public void fillGiftName(String name) {
        logger.info("Заполнение названия подарка: {}", name);
        WebElement input = waiters.waitForElementVisible(giftNameInputBy);
        input.clear();
        input.sendKeys(name);
    }
    /**
     * Заполняет поле описания подарка.
     *
     * @param description описание
     */
    public void fillGiftDescription(String description) {
        logger.info("Заполнение описания подарка: {}", description);
        WebElement textarea = waiters.waitForElementVisible(giftDescriptionInputBy);
        textarea.clear();
        textarea.sendKeys(description);
    }
    /**
     * Заполняет поле ссылки на магазин (если не пусто).
     *
     * @param storeUrl URL магазина
     */
    public void fillGiftStoreUrl(String storeUrl) {
        if (storeUrl != null && !storeUrl.isEmpty()) {
            logger.info("Заполнение ссылки на магазин: {}", storeUrl);
            WebElement input = waiters.waitForElementVisible(giftStoreUrlInputBy);
            input.clear();
            input.sendKeys(storeUrl);
        }
    }
    /**
     * Заполняет поле цены подарка.
     *
     * @param price цена
     */
    public void fillGiftPrice(String price) {
        logger.info("Заполнение цены подарка: {}", price);
        WebElement priceInput = waiters.waitForElementVisible(giftPriceInputBy);
        priceInput.clear();
        priceInput.sendKeys(price);
    }
    /**
     * Заполняет поле ссылки на изображение (если не пусто).
     *
     * @param imageUrl URL изображения
     */
    public void fillGiftImageUrl(String imageUrl) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            logger.info("Заполнение ссылки на изображение: {}", imageUrl);
            WebElement input = waiters.waitForElementVisible(giftImageUrlInputBy);
            input.clear();
            input.sendKeys(imageUrl);
        }
    }
    /**
     * Отправляет форму добавления подарка и обрабатывает возможную ошибку.
     *
     * @return true, если подарок успешно добавлен
     */
    public boolean submitAddGift() {
        logger.info("Отправка формы добавления подарка");
        WebElement submitButton = waiters.waitForElementClickable(addGiftSubmitButtonBy);
        submitButton.click();

        waiters.waitForCondition(driver ->
                driver.findElements(modalDialogBy).isEmpty() ||
                        driver.findElements(errorMessageBy).size() > 0
        );

        if (driver.findElements(errorMessageBy).size() > 0) {
            String errorText = getErrorMessage();
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
    /**
     * Добавляет подарок с опциональными полями storeUrl и imageUrl.
     *
     * @param name        название
     * @param description описание
     * @param price       цена
     * @param storeUrl    ссылка на магазин (может быть null)
     * @param imageUrl    ссылка на изображение (может быть null)
     * @return true, если подарок успешно добавлен
     */
    public boolean addGift(String name, String description, String price,
                           String storeUrl, String imageUrl) {
        logger.info("Начинаем добавление подарка: {}", name);

        // Запоминаем количество ДО добавления
        int beforeCount = getGiftsCount();
        logger.info("Количество подарков до добавления: {}", beforeCount);

        clickAddGift();
        fillGiftName(name);
        fillGiftDescription(description);
        fillGiftStoreUrl(storeUrl);
        fillGiftPrice(price);
        fillGiftImageUrl(imageUrl);

        boolean isSuccess = submitAddGift();

        if (isSuccess) {
            boolean giftAdded = waitForGiftToBeAdded(beforeCount);
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
    /**
     * Ожидает появления нового подарка после добавления.
     *
     * @param previousCount количество подарков до добавления
     * @return true, если новый подарок появился
     */
    private boolean waitForGiftToBeAdded(int previousCount) {
        logger.info("Ожидание появления нового подарка. Было: {}, ожидаем > {}", previousCount, previousCount);
        boolean giftAdded = waiters.waitForCondition(driver ->
                driver.findElements(giftCardsBy).size() > previousCount
        );
        if (giftAdded) {
            int newCount = getGiftsCount();
            logger.info("Новый подарок появился. Теперь подарков: {}", newCount);
            return true;
        }
        return false;
    }
    /**
     * Возвращает текущее количество подарков на странице.
     *
     * @return количество подарков
     */
    public int getGiftsCount() {
        return driver.findElements(giftCardsBy).size();
    }
    /**
     * Возвращает список всех подарков (с ожиданием появления).
     *
     * @return список WebElement карточек подарков
     */
    public List<WebElement> getGifts() {
        return waiters.waitForElementsPresent(giftCardsBy);
    }

    /**
     * Возвращает название подарка по индексу.
     *
     * @param index индекс
     * @return название
     */
    public String getGiftName(int index) {
        List<WebElement> gifts = getGifts();
        if (gifts.isEmpty() || index >= gifts.size()) {
            return "";
        }
        return gifts.get(index).findElement(By.cssSelector(".card-title")).getText();
    }
    /**
     * Возвращает текст сообщения об ошибке.
     *
     * @return текст ошибки
     */
    public String getErrorMessage() {
        WebElement error = waiters.waitForElementVisible(errorMessageBy);
        return error.getText();
    }
}