package de.config;

public class Configuration {

    public static int DELAY_TIME = 180;

    public static String SMTP_HOST = "smtp.gmail.com";
    public static String SMTP_PORT = "587";
    public static String SMTP_MAIL_FROM = "laird.florian@gmail.com";
    public static String SMTP_MAIL_TO = "aq5y2do59f@pomail.net";
    public static String SMTP_PASSWORD = "";

    public static boolean CALC_EW_PRICE = false;

    public static String EUROWINGS_FLIGHTS = "http://flightapp.creativeapis.com/api/Services/JSON/current_flights_for_airline.php?data=Ze7TyFpGrW7FVjWyBkLqwyFsmmiu2O7QhcoCAndEK2cW3ZptiTCi9q%2FRCN2uXzti%0A";
    public static String CGN_AIRPORT = "http://flightapp.creativeapis.com/api/Services/JSON/current_departures_for_airport.php?data=zvoOWl4vxF1gr3M6rwe475iptYMZjzdCfRw5M5iPpWWriyp%2FThgm59N1JYDunUYz%0A";
    public static String DUS_AIRPORT = "http://flightapp.creativeapis.com/api/Services/JSON/current_departures_for_airport.php?data=p8CJ%2BquXhdGjobp39tmTkMwwkBjZgfiaauvbgjpmcogTuEBXmzGnoP%2F%2FjEqF0r5C%0A";

    //Webdriver
    public static String PHANTOMJS_LINUX = "/usr/phantom/phantomjs";
    public static String PHANTOMJS_WINDOWS = System.getProperty("user.dir") + "/src/main/resources/phantomjs.exe";
    public static String FIREFOX_GECKO_WINDOWS = System.getProperty("user.dir") + "/src/main/resources/geckodriver.exe";

    public static String[] DATA = {
            CGN_AIRPORT,
            DUS_AIRPORT,
            EUROWINGS_FLIGHTS
    };

    public static String[] NEGATIV_AIRLINE = {
            "FR",
            "OE"
    };
}