package myboot.web;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller("/")
public class VueAppController {
    
    @RequestMapping(value = "")
    private ModelAndView myApp() {
        return new ModelAndView("app");
    }
}
