package exceptions;
/**
 * Исключение, связанное с ошибками аутентификации.
 */
public class AuthenticationException extends RuntimeException {
    /**
     * Конструктор с указанием пользователя и причины.
     *
     * @param username имя пользователя
     * @param reason   причина ошибки
     */
    public AuthenticationException(String username, String reason) {
        super(String.format("Ошибка аутентификации пользователя '%s'. Причина: %s", username, reason));
    }
    /**
     * Фабричный метод для неверных учётных данных.
     *
     * @param username имя пользователя
     * @return исключение AuthenticationException
     */
    public static AuthenticationException invalidCredentials(String username) {
        return new AuthenticationException(username, "неверный логин или пароль");
    }
}