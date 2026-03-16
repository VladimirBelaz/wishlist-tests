package exceptions;
/**
 * Исключение, выбрасываемое при истечении времени ожидания условия.
 */
public class WaitTimeoutException extends RuntimeException {
    /**
     * Конструктор с сообщением.
     *
     * @param message детали ошибки
     */
    public WaitTimeoutException(String message) {
        super(message);
    }
    /**
     * Конструктор для ожидания элемента.
     *
     * @param elementName    название элемента
     * @param timeoutSeconds таймаут в секундах
     */
    public WaitTimeoutException(String elementName, int timeoutSeconds) {
        super(String.format("Элемент '%s' не появился за %d секунд", elementName, timeoutSeconds));
    }
    /**
     * Конструктор с названием элемента и причиной.
     *
     * @param elementName название элемента
     * @param cause       причина
     */
    public WaitTimeoutException(String elementName, Throwable cause) {
        super(String.format("Таймаут при ожидании элемента '%s': %s", elementName, cause.getMessage()), cause);
    }
}