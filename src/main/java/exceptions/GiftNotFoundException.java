package exceptions;
/**
 * Исключение, когда подарок не найден в списке.
 */
public class GiftNotFoundException extends RuntimeException {
    /**
     * Конструктор с названием подарка.
     *
     * @param giftName название подарка
     */
    public GiftNotFoundException(String giftName) {
        super(String.format("Подарок с названием '%s' не найден", giftName));
    }
}