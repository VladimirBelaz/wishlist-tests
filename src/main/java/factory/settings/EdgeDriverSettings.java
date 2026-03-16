package factory.settings;

import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.AbstractDriverOptions;
/**
 * Настройки для Microsoft Edge.
 */
public class EdgeDriverSettings implements ISettings{

    /**
     * Создаёт EdgeOptions с переданными аргументами.
     *
     * @param args аргументы командной строки
     * @return объект EdgeOptions
     */
    @Override
    public AbstractDriverOptions settings(String... args){

        EdgeOptions options = new EdgeOptions();
        options.addArguments(args);

        return options;
    }

}
