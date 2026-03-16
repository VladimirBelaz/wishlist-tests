package factory.settings;

import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
/**
 * Настройки для Google Chrome.
 */
public class ChromeDriverSettings implements ISettings {
    /**
     * Создаёт ChromeOptions с переданными аргументами.
     *
     * @param args аргументы командной строки
     * @return объект ChromeOptions
     */
    @Override
    public AbstractDriverOptions settings(String... args){

        ChromeOptions options = new ChromeOptions();
        options.addArguments(args);
        return options;
    }

}
