package assertions;

import org.junit.jupiter.api.Assertions;
import pages.WishListDetailPage;

/**
 * Набор проверок для страницы деталей списка желаний.
 * Позволяет проверить заголовок, описание, количество подарков
 * и наличие конкретного подарка по индексу.
 */
public class WishListDetailPageAssertions {

    private final WishListDetailPage page;

    private WishListDetailPageAssertions(WishListDetailPage page) {
        this.page = page;
    }

    /**
     * Фабричный метод для создания экземпляра ассерта.
     *
     * @param page страница деталей списка
     * @return объект ассерта
     */
    public static WishListDetailPageAssertions assertThat(WishListDetailPage page) {
        return new WishListDetailPageAssertions(page);
    }

    /**
     * Проверяет, что заголовок списка совпадает с ожидаемым.
     *
     * @param expectedTitle ожидаемый заголовок
     * @return этот же объект ассерта (для цепочки)
     */
    public WishListDetailPageAssertions hasTitle(String expectedTitle) {
        String actual = page.getListTitle();
        Assertions.assertEquals(expectedTitle, actual,
                "Заголовок списка не совпадает");
        return this;
    }

    /**
     * Проверяет, что описание списка совпадает с ожидаемым.
     *
     * @param expectedDescription ожидаемое описание
     * @return этот же объект ассерта
     */
    public WishListDetailPageAssertions hasDescription(String expectedDescription) {
        String actual = page.getListDescription();
        Assertions.assertEquals(expectedDescription, actual,
                "Описание списка не совпадает");
        return this;
    }

    /**
     * Проверяет, что количество подарков в списке равно ожидаемому.
     *
     * @param expectedCount ожидаемое количество подарков
     * @return этот же объект ассерта
     */
    public WishListDetailPageAssertions hasGiftCount(int expectedCount) {
        int actual = page.getGiftsCount();
        Assertions.assertEquals(expectedCount, actual,
                "Количество подарков не совпадает");
        return this;
    }

    /**
     * Проверяет, что подарок с указанным индексом имеет ожидаемое название.
     *
     * @param index         индекс подарка (начиная с 0)
     * @param expectedName ожидаемое название подарка
     * @return этот же объект ассерта
     */
    public WishListDetailPageAssertions hasGiftWithName(int index, String expectedName) {
        String actual = page.getGiftName(index);
        Assertions.assertEquals(expectedName, actual,
                "Название подарка с индексом " + index + " не совпадает");
        return this;
    }
}