package wishlist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pages.WishListDetailPage;
import assertions.WishListsPageAssertions;
import assertions.WishListDetailPageAssertions;
import java.util.List;
/**
 * Тесты для работы со списками желаний:
 * создание списка, добавление подарка, удаление списка,
 * просмотр деталей, удаление всех списков.
 */
public class WishListTest extends AbsBaseTest {

    @Test
    public void testCreateWishList() {
        logger.info("Тест создания списка желаний");

        login();

        String title = "Мой список желаний " + System.currentTimeMillis();
        String description = "Описание моего списка";

        wishListsPage.waitForPageToLoad();
        //Получить начальное количество списков
        int initialCount = wishListsPage.getWishListsCount();
        logger.info("Начальное количество списков: {}", initialCount);

        wishListsPage.createWishList(title, description);

        WishListsPageAssertions.assertThat(wishListsPage)
                .hasWishListCount(initialCount + 1)
                .hasWishListWithTitle(title);

        logger.info("Список желаний успешно создан ✅");
    }

    @Test
    public void testAddGiftToWishList() {
        logger.info("Тест добавления подарка в список");

        login();

        String listTitle = createTestWishList("Список для подарков");

        wishListsPage.waitForPageToLoad();

        wishListsPage.clickLastWishListView();

        WishListDetailPage detailPage = new WishListDetailPage(driver);
        detailPage.waitForPageToLoad();

        String giftName = "PlayStation 5";
        String giftDescription = "Очень хочу!";
        String giftPrice = "50000";

        int initialGiftsCount = detailPage.getGiftsCount();
        logger.info("Начальное количество подарков: {}", initialGiftsCount);

        boolean isAdded = detailPage.addGift(giftName, giftDescription, giftPrice);

        if (!isAdded) {
            logger.error("Подарок не был добавлен из-за ошибки на сервере");
            Assertions.fail("Не удалось добавить подарок: ошибка сервера");
        }

        WishListDetailPageAssertions.assertThat(detailPage)
                .hasTitle(listTitle)
                .hasGiftCount(initialGiftsCount + 1)
                .hasGiftWithName(0, giftName);

        logger.info("Тест завершен успешно ✅");
    }

    @Test
    public void testDeleteWishList() {
        logger.info("Тест удаления списка желаний");

        login();

        wishListsPage.waitForPageToLoad();
        int currentCount = wishListsPage.getWishListsCount();

        // Если списков нет - создаем
        if (currentCount == 0) {
            logger.info("Списков нет, создаем новый");
            createTestWishList("Список для удаления");
            currentCount = wishListsPage.getWishListsCount();
        }

        int beforeDelete = wishListsPage.getWishListsCount();
        String deletedTitle = wishListsPage.getWishListTitle(beforeDelete - 1);

        wishListsPage.clickDeleteWishList(beforeDelete - 1);

        driver.navigate().refresh();
        wishListsPage.waitForPageToLoad();

        WishListsPageAssertions.assertThat(wishListsPage)
                .wishListCountDecreasedByOne(beforeDelete)
                .wishListIsDeleted(deletedTitle);
    }

    @Test
    public void testDeleteAllWishLists() {
        logger.info("Тест удаления всех списков желаний");
        login();
        wishListsPage.waitForPageToLoad();
        ensureAtLeastOneWishListExists();

        List<String> listTitles = getAllWishListTitles();
        int beforeCount = listTitles.size();
        logger.info("Найдено списков для удаления: {}", beforeCount);

        for (int i = 0; i < beforeCount; i++) {
            int expectedAfter = beforeCount - i - 1;
            wishListsPage.clickDeleteWishList(expectedAfter);
            refreshAndWait();
        }

        WishListsPageAssertions.assertThat(wishListsPage)
                .allWishListsDeleted();

        logger.info("Все списки успешно удалены ✅");
    }

    @Test
    public void testViewWishListDetails() {
        logger.info("Тест просмотра деталей списка");

        login();

        String title = "Детальный список " + System.currentTimeMillis();
        String description = "Подробное описание";

        // Создаем список и сразу получаем его индекс
        wishListsPage.waitForPageToLoad();
        int beforeCount = wishListsPage.getWishListsCount();

        wishListsPage.createWishList(title, description);

        int afterCount = wishListsPage.getWishListsCount();
        logger.info("Создан список. Всего списков: {}", afterCount);

        // Кликаем по последнему созданному списку (индекс = afterCount - 1)
        wishListsPage.clickViewWishList(afterCount - 1);

        WishListDetailPage detailPage = new WishListDetailPage(driver);
        detailPage.waitForPageToLoad();

        WishListDetailPageAssertions.assertThat(detailPage)
                .hasTitle(title)
                .hasDescription(description)
                .hasGiftCount(0);

        logger.info("Детали списка отображаются корректно ✅");
    }

    @Test
    public void testCreateMultipleWishLists() {
        logger.info("Тест создания нескольких списков");

        login();

        wishListsPage.waitForPageToLoad();
        int initialCount = wishListsPage.getWishListsCount();
        logger.info("Начальное количество списков: {}", initialCount);

        // Создаем три списка
        String title1 = createTestWishList("Первый список");
        String title2 = createTestWishList("Второй список");
        String title3 = createTestWishList("Третий список");

        int finalCount = wishListsPage.getWishListsCount();
        logger.info("После создания трех списков: {} списков", finalCount);

        WishListsPageAssertions.assertThat(wishListsPage)
                .hasWishListCount(initialCount + 3)  // ← теперь правильно: initialCount + 3
                .hasWishListWithTitle(title1)
                .hasWishListWithTitle(title2)
                .hasWishListWithTitle(title3);

        logger.info("Все три списка успешно созданы ✅");
    }
}