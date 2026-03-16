package factory;

import exceptions.BrowserTypeNotSupportedException;
import factory.settings.ChromeDriverSettings;
import factory.settings.EdgeDriverSettings;
import factory.settings.FireFoxDriverSettings;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
/**
 * Фабрика для создания экземпляра WebDriver в зависимости от типа браузера,
 * заданного системным свойством "browser". Поддерживает Chrome, Firefox, Edge.
 */
public class WebDriverFactory {

    private final String browserType = System.getProperty("browser", "CHROME").trim().toUpperCase();

    //Базовый метод без опций
    public WebDriver create() {
        switch (browserType) {
            case "CHROME" -> {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = (ChromeOptions) new ChromeDriverSettings()
                        .settings("--no-sandbox",
                                "--disable-dev-shm-usage",
                                "--window-size=1920,1080",
                                "--remote-allow-origins=*");
                return new ChromeDriver(options);
            }
            case "FIREFOX" -> {
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions options = (FirefoxOptions) new FireFoxDriverSettings().settings();
                return new FirefoxDriver(options);
            }

            case "EDGE" -> {
                WebDriverManager.edgedriver().setup();
                EdgeOptions options = (EdgeOptions) new EdgeDriverSettings().settings();
                return new EdgeDriver(options);
            }
        }
        throw new BrowserTypeNotSupportedException(browserType);
    }
}