package de;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class FlightController {
    
    @RequestMapping(value = "/flightDelay/{delay}", method = RequestMethod.GET)
    public String index(@PathVariable("delay") int delay) throws Exception{
        return FlightChecker.flightSearch(delay, true);
    }
    
}
