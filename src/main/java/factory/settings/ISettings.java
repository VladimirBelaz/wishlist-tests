package factory.settings;

import org.openqa.selenium.remote.AbstractDriverOptions;
/**
 * Интерфейс для настроек браузера.
 * Каждая реализация предоставляет свои опции для конкретного браузера.
 */
public interface ISettings {
    /**
     * Возвращает объект настроек для драйвера.
     *
     * @param args аргументы командной строки для браузера
     * @return объект AbstractDriverOptions с заданными аргументами
     */
    AbstractDriverOptions settings(String... args);

}
