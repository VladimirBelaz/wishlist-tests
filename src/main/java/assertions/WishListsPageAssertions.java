package assertions;

import org.junit.jupiter.api.Assertions;
import pages.WishListsPage;

/**
 * Набор проверок для страницы со списками желаний.
 * Позволяет проверить количество списков, наличие/отсутствие конкретного списка,
 * изменение количества после удаления и полное удаление всех списков.
 */
public class WishListsPageAssertions {

    private final WishListsPage page;

    private WishListsPageAssertions(WishListsPage page) {
        this.page = page;
    }

    /**
     * Фабричный метод для создания экземпляра ассерта.
     *
     * @param page страница списков желаний
     * @return объект ассерта
     */
    public static WishListsPageAssertions assertThat(WishListsPage page) {
        return new WishListsPageAssertions(page);
    }

    /**
     * Проверяет, что количество списков желаний равно ожидаемому.
     *
     * @param expectedCount ожидаемое количество списков
     * @return этот же объект ассерта (для цепочки)
     */
    public WishListsPageAssertions hasWishListCount(int expectedCount) {
        int actual = page.getWishListsCount();
        Assertions.assertEquals(expectedCount, actual,
                "Количество списков желаний не совпадает");
        return this;
    }

    /**
     * Проверяет, что на странице присутствует список с указанным названием.
     *
     * @param expectedTitle ожидаемое название списка
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions hasWishListWithTitle(String expectedTitle) {
        boolean hasList = page.isWishListDisplayed(expectedTitle);
        Assertions.assertTrue(hasList,
                "Список желаний с названием '" + expectedTitle + "' не найден");
        return this;
    }

    /**
     * Проверяет, что на странице отсутствует список с указанным названием.
     *
     * @param expectedTitle название, которое не должно отображаться
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions hasNoWishListWithTitle(String expectedTitle) {
        boolean hasList = page.isWishListDisplayed(expectedTitle);
        Assertions.assertFalse(hasList,
                "Список желаний с названием '" + expectedTitle + "' не должен отображаться");
        return this;
    }

    /**
     * Проверяет, что название списка по заданному индексу совпадает с ожидаемым.
     *
     * @param index         индекс списка (0-based)
     * @param expectedTitle ожидаемое название
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions wishListTitleEquals(int index, String expectedTitle) {
        String actual = page.getWishListTitle(index);
        Assertions.assertEquals(expectedTitle, actual,
                "Название списка желаний не совпадает");
        return this;
    }

    /**
     * Проверяет, что текущее количество списков уменьшилось на заданную величину
     * относительно начального количества.
     *
     * @param initialCount   начальное количество списков
     * @param expectedDecrease ожидаемое уменьшение (обычно 1)
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions wishListCountDecreasedBy(int initialCount, int expectedDecrease) {
        int actual = page.getWishListsCount();
        Assertions.assertEquals(initialCount - expectedDecrease, actual,
                String.format("Количество списков должно уменьшиться на %d", expectedDecrease));
        return this;
    }

    /**
     * Проверяет, что количество списков уменьшилось на 1 относительно начального.
     *
     * @param initialCount начальное количество списков
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions wishListCountDecreasedByOne(int initialCount) {
        return wishListCountDecreasedBy(initialCount, 1);
    }

    /**
     * Проверяет, что список с указанным названием был удалён (не отображается).
     *
     * @param deletedTitle название удалённого списка
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions wishListIsDeleted(String deletedTitle) {
        boolean hasList = page.isWishListDisplayed(deletedTitle);
        Assertions.assertFalse(hasList,
                "Список желаний с названием '" + deletedTitle + "' не должен отображаться после удаления");
        return this;
    }

    /**
     * Проверяет, что список с указанным названием не был удалён (всё ещё отображается).
     *
     * @param title название списка
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions wishListIsNotDeleted(String title) {
        boolean hasList = page.isWishListDisplayed(title);
        Assertions.assertTrue(hasList,
                "Список желаний с названием '" + title + "' должен отображаться");
        return this;
    }

    /**
     * Проверяет, что на странице нет ни одного списка (количество равно 0).
     *
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions allWishListsDeleted() {
        int actual = page.getWishListsCount();
        Assertions.assertEquals(0, actual,
                "Все списки должны быть удалены, но найдено: " + actual);
        return this;
    }

    /**
     * Проверяет, что на странице нет ни одного списка (синоним для allWishListsDeleted).
     *
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions hasNoWishLists() {
        return allWishListsDeleted();
    }

    /**
     * Проверяет, что текущий URL содержит ожидаемую подстроку.
     *
     * @param expectedPart ожидаемая подстрока
     * @return этот же объект ассерта
     */
    public WishListsPageAssertions urlContains(String expectedPart) {
        Assertions.assertTrue(page.getCurrentUrl().contains(expectedPart),
                "URL должен содержать: " + expectedPart);
        return this;
    }
}