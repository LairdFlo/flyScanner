package de.utils;

import de.data.Airport;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static de.config.Configuration.FIREFOX_GECKO_WINDOWS;
import static de.config.Configuration.PHANTOMJS_LINUX;
import static de.config.Configuration.PHANTOMJS_WINDOWS;

public class WebDriverUtils {

    private static final Logger log = LoggerFactory.getLogger(WebDriverUtils.class);

    public WebDriver getPhantomDriver() {
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
            System.setProperty("phantomjs.binary.path", PHANTOMJS_WINDOWS);
        } else {
            System.setProperty("phantomjs.binary.path", PHANTOMJS_LINUX);
        }
        return new PhantomJSDriver();
    }

    public WebDriver getFireFoxDriver() {
        System.setProperty("webdriver.gecko.driver", FIREFOX_GECKO_WINDOWS);
        return new FirefoxDriver();
    }

    public String[] getPricesForDelayedEurowings(Airport start, String ende, String datum, WebDriver webdriver) {

        try {
            WebDriver driver = webdriver;
            JavascriptExecutor js = (JavascriptExecutor) driver;

            driver.get("https://mobile.eurowings.com/booking/Select.aspx?culture=de-DE");

            driver.findElement(By.xpath("//label[2]")).click();

            driver.findElement(By.xpath("//div[2]/span[2]")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//div[3]/div[3]/div/input")).sendKeys(start.toPlainString());
            Thread.sleep(1000);
            driver.findElement(By.xpath("//div[@id='siteContent']/div[3]/div[4]/ul[2]/li[5]")).click();

            driver.findElement(By.xpath("//div[3]/span[2]")).click();
            Thread.sleep(1000);
            driver.findElement(By.xpath("//div[4]/div[3]/div/input")).sendKeys(ende);
            Thread.sleep(1000);
            driver.findElement(By.xpath("//div[@id='siteContent']/div[4]/div[4]/ul/li[5]")).click();

            js.executeScript("document.getElementById('SearchViewControlGroupFlightSelection_SearchViewFlightSearchControl_departureDate').setAttribute('type', '')");
            driver.findElement(By.id("SearchViewControlGroupFlightSelection_SearchViewFlightSearchControl_departureDate")).sendKeys(datum);

            driver.findElement(By.id("SearchViewControlGroupFlightSelection_SearchViewFlightSearchControl_ButtonSubmit")).click();
            Thread.sleep(1000);
            driver.findElement(By.id("SearchViewControlGroupFlightSelection_SearchViewFlightSearchControl_ButtonSubmit")).click();

            WebElement tripDeparture = driver.findElement(By.id("tripDeparture"));

            String[] fluegeArray = tripDeparture.getText().split(start.toPlainString() + " " + ende);

            if (fluegeArray.length == 1) {
                if (!fluegeArray[0].contains("Leider sind für die ausgewählte Flugstrecke an diesem Datum keine Flüge verfügbar.")) {
                    log.error("getPricesForDelayedEurowings(): Keine Fluege verfuegbar bzw. nicht auswertbar \n" + start.toPlainString() + "-" + ende + ":" + tripDeparture.getText());
                } else {
                    log.error("Kein EW-Flug buchbar");
                }

                return null;
            }

            return fluegeArray;
        } catch (Exception e) {
            log.error("Error {}", e.getMessage());
            return null;
        }
    }
}
