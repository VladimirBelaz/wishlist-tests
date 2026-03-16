package exceptions;
/**
 * Исключение, выбрасываемое при указании неподдерживаемого типа браузера.
 */
public class BrowserTypeNotSupportedException extends RuntimeException{
    /**
     * Конструктор с указанием неподдерживаемого типа.
     *
     * @param browserType тип браузера
     */
    public  BrowserTypeNotSupportedException(String browserType) {
        super(String.format("Browser %s not supported", browserType));
    }

}
