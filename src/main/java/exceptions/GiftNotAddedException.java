package exceptions;
/**
 * Исключение, возникающее при неудачной попытке добавить подарок.
 */
public class GiftNotAddedException extends RuntimeException {
    /**
     * Конструктор с названием подарка.
     *
     * @param giftName название подарка
     */
    public GiftNotAddedException(String giftName) {
        super(String.format("Подарок '%s' не был добавлен", giftName));
    }
}