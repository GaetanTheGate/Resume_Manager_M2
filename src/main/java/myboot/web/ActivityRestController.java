package myboot.web;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import myboot.dao.ActivityRepository;
import myboot.dto.ActivityDTO;
import myboot.model.Activity;

@RestController
@RequestMapping("/api/activities")
public class ActivityRestController {
    @Autowired
    ActivityRepository a_repo;

    @Autowired
    LocalValidatorFactoryBean validationFactory;

    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    @GetMapping("")
    public Iterable<ActivityDTO> getActivitys(@RequestParam(defaultValue = "", required = false) String title, @RequestParam(defaultValue = "", required = false) String description){
        return a_repo.findByTitleAndDescription(title, description).stream().map( a -> mapper.map(a, ActivityDTO.class)).toList();
    }

    @GetMapping("/{id}")
    public ActivityDTO getActivity(@PathVariable int id) throws ActivityNotFoundException {
        Optional<Activity> a = a_repo.findById(id);
        a.orElseThrow(ActivityNotFoundException::new);

        return mapper.map(a.get(), ActivityDTO.class);
    }


    /// PostMapping

    @PostMapping("")
    public Activity postActivity(@RequestBody ActivityDTO adto) throws ActivityNotFoundException {
        Activity a = mapper.map(adto, Activity.class);

        Optional.ofNullable(a_repo.findById(a.getId()).isPresent() ? null : a).orElseThrow(ActivityNotFoundException::new);

        return a_repo.save(a);
    }


    /// PutMapping

    @PutMapping("")
    public Activity putActivity(@RequestBody ActivityDTO adto) throws ActivityNotFoundException {
        Activity a = mapper.map(adto, Activity.class);

        a_repo.findById(a.getId()).orElseThrow(ActivityNotFoundException::new);

        return a_repo.save(a);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable int id) {
        a_repo.deleteById(id);
    }

    
    /// Exception

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class ActivityNotFoundException extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }
}
