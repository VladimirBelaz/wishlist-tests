package exceptions;
/**
 * Исключение, сигнализирующее о том, что страница не загрузилась за отведённое время.
 */
public class PageNotLoadedException extends RuntimeException {
    /**
     * Конструктор с названием страницы.
     *
     * @param pageName название страницы
     */
    public PageNotLoadedException(String pageName) {
        super(String.format("Страница '%s' не загрузилась за отведенное время", pageName));
    }
    /**
     * Конструктор с названием страницы и таймаутом.
     *
     * @param pageName       название страницы
     * @param timeoutSeconds таймаут в секундах
     */
    public PageNotLoadedException(String pageName, int timeoutSeconds) {
        super(String.format("Страница '%s' не загрузилась за %d секунд", pageName, timeoutSeconds));
    }
    /**
     * Конструктор с названием страницы и причиной.
     *
     * @param pageName название страницы
     * @param cause    причина
     */
    public PageNotLoadedException(String pageName, Throwable cause) {
        super(String.format("Ошибка загрузки страницы '%s': %s", pageName, cause.getMessage()), cause);
    }
}