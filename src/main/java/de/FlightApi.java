package de;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import static de.Utils.getDelayFlight;
import static de.Utils.getFlightJson;

@RestController
public class FlightApi {
    
    @RequestMapping(value = "/flightDelay/{delay}", method = RequestMethod.GET)
    public String index(@PathVariable("delay") int delay){
        return getFlightJson(getDelayFlight(delay));
    }
    
}
