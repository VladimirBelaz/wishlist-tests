package factory.settings;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
/**
 * Настройки для Mozilla Firefox.
 */
public class FireFoxDriverSettings implements ISettings {
    /**
     * Создаёт FirefoxOptions с переданными аргументами.
     *
     * @param args аргументы командной строки
     * @return объект FirefoxOptions
     */
    @Override
    public AbstractDriverOptions settings(String... args){

        FirefoxOptions options = new FirefoxOptions();
        options.addArguments(args);

        return options;
    }
}
