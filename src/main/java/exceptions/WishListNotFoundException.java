package exceptions;
/**
 * Исключение, выбрасываемое когда запрашиваемый список желаний не найден.
 */
public class WishListNotFoundException extends RuntimeException {

    public WishListNotFoundException(String listTitle) {
        super(String.format("Список желаний с названием '%s' не найден", listTitle));
    }

    public WishListNotFoundException(int index) {
        super(String.format("Список желаний с индексом %d не найден", index));
    }

    public WishListNotFoundException(String listId, Throwable cause) {
        super(String.format("Список желаний с ID '%s' не найден: %s", listId, cause.getMessage()), cause);
    }
}