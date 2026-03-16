package exceptions;
/**
 * Исключение, выбрасываемое при ошибках, связанных с тестовыми данными.
 * Например, если обязательное системное свойство не задано,
 * тестовый пользователь не найден или данные не соответствуют ожидаемому формату.
 */
public class TestDataException extends RuntimeException {
    /**
     * Конструктор с сообщением.
     *
     * @param message детали ошибки
     */
    public TestDataException(String message) {
        super(message);
    }
    /**
     * Конструктор с названием данных, ожидаемым и фактическим значением.
     *
     * @param dataName название проверяемых данных
     * @param expected ожидаемое значение
     * @param actual   фактическое значение
     */
    public TestDataException(String dataName, String expected, String actual) {
        super(String.format("Тестовые данные не соответствуют ожиданию. %s: ожидалось '%s', получено '%s'",
                dataName, expected, actual));
    }
    /**
     * Фабричный метод для случая, когда пользователь не найден в тестовых данных.
     *
     * @param username имя пользователя
     * @return исключение TestDataException
     */
    public static TestDataException userNotFound(String username) {
        return new TestDataException(String.format("Пользователь '%s' не найден в тестовых данных", username));
    }
    /**
     * Фабричный метод для случая, когда не задано обязательное системное свойство.
     *
     * @param propertyName имя свойства
     * @return исключение TestDataException
     */
    public static TestDataException propertyNotSet(String propertyName) {
        return new TestDataException(String.format("Системное свойство '%s' не установлено", propertyName));
    }
}