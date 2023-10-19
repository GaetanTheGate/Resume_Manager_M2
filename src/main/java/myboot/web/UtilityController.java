package myboot.web;

import java.util.ArrayList;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import myboot.dao.ActivityRepository;
import myboot.dao.CVRepository;
import myboot.dao.PersonRepository;
import myboot.model.Activity;
import myboot.model.CV;
import myboot.model.Person;

@Controller
public class UtilityController implements ErrorController {
    
	@RequestMapping("/error")
	@ResponseBody
	public String handleError(Exception e) {
		e.printStackTrace();
		return "Error " + e.getClass().getName() ;
	}


    private int nb_a = 5;
    private int nb_c = 4;
    private int nb_p = 3;

    @Autowired
    protected ActivityRepository a_repo;
    @Autowired
    protected CVRepository c_repo;
    @Autowired
    protected PersonRepository p_repo;

	@PostConstruct
	public void init(){

        for(int x = 0 ; x < nb_p ; x++){
            Person p = p_repo.save(new Person(0, "password"+x, "Name"+x, "FirstName"+x, "mail"+x, "web"+x, null, null));

            for(int y = 0 ; y < nb_c ; y++){
                CV cv = c_repo.save(new CV(0, "titre"+y, "description"+y, new ArrayList<>(), p));

                for(int z = 0 ; z < nb_a ; z++){
                    a_repo.save(new Activity(0, 2000+z, String.valueOf(z), "test"+z, "description test"+z, "test"+z+".fr", cv));
                }
            }
        }
	}
}
