package de;

import de.data.Airport;
import de.utils.CgnAirportUtils;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;

public class PreisTestEurowings {


    @Test
    public void priceTest(){
        CgnAirportUtils cgnAirportUtils = new CgnAirportUtils();
        LocalDate date = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        String ew88 = cgnAirportUtils.getDelayPriceEurowings(Airport.CGN, "Berlin-Tegel", "EW20", fmt.print(date), true);
        System.out.println(ew88);
    }
}
