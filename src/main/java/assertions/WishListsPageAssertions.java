package assertions;

import org.junit.jupiter.api.Assertions;
import pages.WishListsPage;
/**
 * Набор проверок для страницы со списками желаний.
 * Проверяет количество списков, наличие/отсутствие конкретного списка,
 * а также полное удаление всех списков.
 */
public class WishListsPageAssertions {

    private final WishListsPage page;

    private WishListsPageAssertions(WishListsPage page) {
        this.page = page;
    }

    public static WishListsPageAssertions assertThat(WishListsPage page) {
        return new WishListsPageAssertions(page);
    }

    public WishListsPageAssertions hasWishListCount(int expectedCount) {
        int actual = page.getWishListsCount();
        Assertions.assertEquals(expectedCount, actual,
                "Количество списков желаний не совпадает");
        return this;
    }

    public WishListsPageAssertions hasWishListWithTitle(String expectedTitle) {
        boolean hasList = page.isWishListDisplayed(expectedTitle);
        Assertions.assertTrue(hasList,
                "Список желаний с названием '" + expectedTitle + "' не найден");
        return this;
    }

    public WishListsPageAssertions wishListCountDecreasedBy(int initialCount, int expectedDecrease) {
        int actual = page.getWishListsCount();
        Assertions.assertEquals(initialCount - expectedDecrease, actual,
                String.format("Количество списков должно уменьшиться на %d", expectedDecrease));
        return this;
    }

    public WishListsPageAssertions wishListCountDecreasedByOne(int initialCount) {
        return wishListCountDecreasedBy(initialCount, 1);
    }

    public WishListsPageAssertions wishListIsDeleted(String deletedTitle) {
        boolean hasList = page.isWishListDisplayed(deletedTitle);
        Assertions.assertFalse(hasList,
                "Список желаний с названием '" + deletedTitle + "' не должен отображаться после удаления");
        return this;
    }


    public WishListsPageAssertions allWishListsDeleted() {
        int actual = page.getWishListsCount();
        Assertions.assertEquals(0, actual,
                "Все списки должны быть удалены, но найдено: " + actual);
        return this;
    }


    public WishListsPageAssertions urlContains(String expectedPart) {
        Assertions.assertTrue(page.getCurrentUrl().contains(expectedPart),
                "URL должен содержать: " + expectedPart);
        return this;
    }
}