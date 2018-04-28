package de;

import de.utils.FlightTrackerUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class FlightApi {

    private FlightTrackerUtils flightTrackerUtils = new FlightTrackerUtils();

    @RequestMapping(value = "/flightDelay/{delay}", method = RequestMethod.GET)
    public String index(@PathVariable("delay") int delay){
        return flightTrackerUtils.getFlightJson(flightTrackerUtils.getDelayFlight(delay));
    }
    
}
