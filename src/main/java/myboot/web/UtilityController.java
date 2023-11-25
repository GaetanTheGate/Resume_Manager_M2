package myboot.web;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.javafaker.Faker;

import myboot.dao.ActivityRepository;
import myboot.dao.CVRepository;
import myboot.dao.PersonRepository;
import myboot.dao.XUserRepository;
import myboot.model.Activity;
import myboot.model.CV;
import myboot.model.Person;
import myboot.model.XUser;

@Controller
public class UtilityController implements ErrorController {
    
	@RequestMapping("/error")
	@ResponseBody
	public String handleError(Exception e) {
		e.printStackTrace();
		return "Error " + e.getClass().getName() ;
	}

    @Autowired
    protected XUserRepository u_repo;
    @Autowired
    protected ActivityRepository a_repo;
    @Autowired
    protected CVRepository c_repo;
    @Autowired
    protected PersonRepository p_repo;


    @GetMapping("/add/{nb_p}/{nb_c}/{nb_a}")
    public String addPeople(@PathVariable int nb_p, @PathVariable int nb_c, @PathVariable int nb_a){
        Faker faker = new Faker();

        for(int x = 0 ; x < nb_p ; x++){
            var encoder = new BCryptPasswordEncoder(12);

            String n = faker.name().firstName();

            XUser u = u_repo.save(new XUser(n + x, encoder.encode(n + x), Set.of("USER"), null));

            Person p = p_repo.save(new Person(0, n, faker.name().lastName(), n + x+"@mail.com", n+".com", faker.date().birthday(), null, u));

            for(int y = 0 ; y < nb_c ; y++){
                CV cv = c_repo.save(new CV(0, faker.commerce().department(), faker.book().title(), null, p));

                for(int z = 0 ; z < nb_a ; z++){
                    a_repo.save(new Activity(0, faker.number().numberBetween(1995, 2018) , faker.job().field(), faker.job().title(), faker.company().industry(), faker.company().name()+".com", cv));
                }
            }
        }

        return "app";
    }

    @GetMapping("/addAdmin/{username}/{password}")
    public String addAdmin(@PathVariable String username, @PathVariable String password){
        var encoder = new BCryptPasswordEncoder(12);

        u_repo.save(new XUser(username, encoder.encode(password), Set.of("ADMIN","USER"), null));

        return "app";
    }
}
