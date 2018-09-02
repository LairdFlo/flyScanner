
package de.utils;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EurowingsFlugnummerTest {

    CgnAirportUtils cgnAirportUtils;

    @Before
    public void init(){
        cgnAirportUtils = new CgnAirportUtils();
    }
    @Test
    public void defaultTest(){
        String defaultFlug = "EW80";

        String cleanFlug = cgnAirportUtils.cleanEurowingsFlugnummer(defaultFlug);

        assertEquals(cleanFlug, defaultFlug);
    }

    @Test
    public void multiTest(){
        String multiFlug = "EW 206,LH 5448";

        String cleanFlug = cgnAirportUtils.cleanEurowingsFlugnummer(multiFlug);

        assertEquals(cleanFlug, "EW206");
    }

    @Test
    public void fuehrendeNullTest(){
        String fuehrendeNull = "EW008";

        String cleanFlug = cgnAirportUtils.cleanEurowingsFlugnummer(fuehrendeNull);

        assertEquals(cleanFlug, "EW8");
    }

}
