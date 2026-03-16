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

    public static WishListDetailPageAssertions assertThat(WishListDetailPage page) {
        return new WishListDetailPageAssertions(page);
    }

    public WishListDetailPageAssertions hasTitle(String expectedTitle) {
        Assertions.assertEquals(expectedTitle, page.getListTitle(),
                "Заголовок списка не совпадает");
        return this;
    }

    public WishListDetailPageAssertions hasDescription(String expectedDescription) {
        Assertions.assertEquals(expectedDescription, page.getListDescription(),
                "Описание списка не совпадает");
        return this;
    }

    public WishListDetailPageAssertions hasGiftCount(int expectedCount) {
        Assertions.assertEquals(expectedCount, page.getGiftsCount(),
                "Количество подарков не совпадает");
        return this;
    }

    public WishListDetailPageAssertions hasGiftWithName(int index, String expectedName) {
        Assertions.assertEquals(expectedName, page.getGiftName(index),
                "Название подарка не совпадает");
        return this;
    }
}