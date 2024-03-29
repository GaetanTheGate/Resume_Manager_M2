package myboot.web;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import myboot.Utility.IsEntityFromUserChecker;
import myboot.dao.ActivityRepository;
import myboot.dto.ActivityDTO;
import myboot.model.Activity;

@RestController
@RequestMapping("/api/activities")
@Profile("usejwt")
public class ActivityRestController {
    @Autowired
    ActivityRepository a_repo;

    @Autowired
    LocalValidatorFactoryBean validationFactory;

    @Autowired
    IsEntityFromUserChecker userChecker;

    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    @GetMapping("")
    public Iterable<ActivityDTO> getActivitys(@RequestParam(defaultValue = "", required = false) String title, @RequestParam(defaultValue = "", required = false) Integer year){
        return a_repo.findByTitleAndYearMin(title, year).stream().map( a -> mapper.map(a, ActivityDTO.class)).toList();
    }

    @GetMapping("/{id}")
    public ActivityDTO getActivity(@PathVariable int id) throws ActivityNotFoundException {
        Optional<Activity> a = a_repo.findById(id);
        a.orElseThrow(ActivityNotFoundException::new);

        return mapper.map(a.get(), ActivityDTO.class);
    }


    /// PostMapping

    @PostMapping("")
    public ActivityDTO postActivity(@RequestBody ActivityDTO adto, HttpServletRequest req) throws ActivityNotFoundException, IsEntityFromUserChecker.UserNotAllowedException {
        if(! (userChecker.isUserAdmin(req) || userChecker.isMyCV(req, adto.getCv().getId())))
            throw new IsEntityFromUserChecker.UserNotAllowedException();

        Activity a = mapper.map(adto, Activity.class);

        Optional.ofNullable(a_repo.findById(a.getId()).isPresent() ? null : a).orElseThrow(ActivityNotFoundException::new);

        return mapper.map(a_repo.save(a), ActivityDTO.class);
    }


    /// PutMapping

    @PutMapping("")
    public ActivityDTO putActivity(@RequestBody ActivityDTO adto, HttpServletRequest req) throws ActivityNotFoundException, IsEntityFromUserChecker.UserNotAllowedException {
        if(! (userChecker.isUserAdmin(req) || userChecker.isMyActivity(req, adto.getId())))
            throw new IsEntityFromUserChecker.UserNotAllowedException();
        
        Activity a = mapper.map(adto, Activity.class);

        Optional<Activity> a_data = a_repo.findById(a.getId());
        a_data.orElseThrow(ActivityNotFoundException::new);

        a.setCv(a_data.get().getCv());

        return mapper.map(a_repo.save(a), ActivityDTO.class);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActivity(@PathVariable int id, HttpServletRequest req) throws IsEntityFromUserChecker.UserNotAllowedException {
        if(! (userChecker.isUserAdmin(req) || userChecker.isMyActivity(req, id)))
            throw new IsEntityFromUserChecker.UserNotAllowedException();

        a_repo.deleteById(id);
    }

    
    /// Exception

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class ActivityNotFoundException extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }
}
