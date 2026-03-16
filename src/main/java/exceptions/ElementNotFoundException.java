package exceptions;
/**
 * Исключение, возникающее при отсутствии элемента на странице.
 */
public class ElementNotFoundException extends RuntimeException {
    /**
     * Конструктор с названием элемента.
     *
     * @param elementName название элемента
     */
    public ElementNotFoundException(String elementName) {
        super(String.format("Элемент '%s' не найден на странице", elementName));
    }
}