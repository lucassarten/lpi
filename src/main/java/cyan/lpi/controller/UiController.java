package cyan.lpi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UiController {
    /**
     * This function maps the root URL to the index.html of the React
     * front end.
     *
     * @return Path of the HTML file to display
     */
    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }
}
