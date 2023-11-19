package myboot.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import myboot.dao.ActivityRepository;
import myboot.dao.CVRepository;
import myboot.dao.PersonRepository;

@Controller("/")
public class VueAppController {
    
    @RequestMapping(value = "")
    private ModelAndView myApp() {
        return new ModelAndView("app");
    }

    @Autowired
    PersonRepository p_repo;

    @Autowired
    CVRepository c_repo;

    @Autowired
    ActivityRepository a_repo;

    @RequestMapping(value = "/person/{id}")
    private ModelAndView myAppAtPerson(@PathVariable Integer id) {
        return new ModelAndView("app", Map.of("person", p_repo.findById(id)));
    }

    @RequestMapping(value = "/cv/{id}")
    private ModelAndView myAppAtCv(@PathVariable Integer id) {
        return new ModelAndView("app", Map.of("cv", c_repo.findById(id)));
    }

    @RequestMapping(value = "/activity/{id}")
    private ModelAndView myAppAtActivity(@PathVariable Integer id) {
        return new ModelAndView("app", Map.of("activity", a_repo.findById(id)));
    }
}
