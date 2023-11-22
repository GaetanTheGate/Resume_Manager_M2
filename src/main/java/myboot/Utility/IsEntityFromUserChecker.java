package myboot.Utility;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import myboot.dao.ActivityRepository;
import myboot.dao.CVRepository;
import myboot.dao.PersonRepository;
import myboot.dao.XUserRepository;
import myboot.model.Activity;
import myboot.model.CV;
import myboot.model.Person;
import myboot.model.XUser;
import myboot.security.UserService;
import myboot.web.ActivityRestController.ActivityNotFoundException;
import myboot.web.CVRestController.CVNotFoundException;
import myboot.web.PersonRestController.PersonNotFoundException;
import myboot.web.UserController.UserNotFoundException;

@Service
@Profile("usejwt")
public class IsEntityFromUserChecker {
    
    public static Exception UserNotAllowedException;

    @Autowired
    UserService userService;

    @Autowired
    XUserRepository u_repo;

    @Autowired
    ActivityRepository a_repo;
    
    @Autowired
    CVRepository c_repo;
    
    @Autowired
    PersonRepository p_repo;

    public boolean isUserAdmin(HttpServletRequest req){
        XUser user = userService.whoami(req);
        
        return user.getRoles().contains("ADMIN");
    }

    public boolean isMySelf(HttpServletRequest req, String username) throws UserNotFoundException {
        Optional<XUser> u_data = u_repo.findById(username);
        u_data.orElseThrow(UserNotFoundException::new);

        XUser u = u_data.get();
        XUser user = userService.whoami(req);
        
        return u.getUserName() == user.getUserName();
    }

    public boolean isMyPerson(HttpServletRequest req, int id) throws PersonNotFoundException {
        Optional<Person> p_data = p_repo.findById(id);
        p_data.orElseThrow(PersonNotFoundException::new);

        Person person = p_data.get();
        XUser user = userService.whoami(req);
        
        if(user.getSelf() != null)
            return person.getId() == user.getSelf().getId();

        return false;
    }

    public boolean isMyCV(HttpServletRequest req, int id) throws CVNotFoundException {
        Optional<CV> c_data = c_repo.findById(id);
        c_data.orElseThrow(CVNotFoundException::new);

        CV cv = c_data.get();
        XUser user = userService.whoami(req);
        
        if(user.getSelf() != null)
            return cv.getOwner().getId() == user.getSelf().getId();

        return false;
    }

    public boolean isMyActivity(HttpServletRequest req, int id) throws ActivityNotFoundException {
        Optional<Activity> a_data = a_repo.findById(id);
        a_data.orElseThrow(ActivityNotFoundException::new);

        Activity activity = a_data.get();
        XUser user = userService.whoami(req);
        
        if(user.getSelf() != null)
            return activity.getCv().getOwner().getId() == user.getSelf().getId();

        return false;
    }

    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public static class UserNotAllowedException extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }
}
