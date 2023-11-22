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
import myboot.dao.CVRepository;
import myboot.dto.CVDTO;
import myboot.model.CV;

@RestController
@RequestMapping("/api/cvs")
@Profile("usejwt")
public class CVRestController {
    @Autowired
    CVRepository c_repo;

    @Autowired
    LocalValidatorFactoryBean validationFactory;

    private ModelMapper mapper = new ModelMapper();

    @Autowired
    IsEntityFromUserChecker userChecker;

    /// GetMapping

    @GetMapping("")
    public Iterable<CVDTO> getCVs(@RequestParam(defaultValue = "", required = false) String title, @RequestParam(defaultValue = "", required = false) String description){
        return c_repo.findByTitleAndDescription(title, description).stream().map( cv -> mapper.map(cv, CVDTO.class)).toList();
    }

    @GetMapping("/{id}")
    public CVDTO getCV(@PathVariable int id) throws CVNotFoundException {
        Optional<CV> c = c_repo.findById(id);
        c.orElseThrow(CVNotFoundException::new);

        return mapper.map(c.get(), CVDTO.class);
    }


    /// PostMapping

    @PostMapping("")
    public CVDTO postCV(@RequestBody CVDTO cvdto, HttpServletRequest req) throws CVNotFoundException, IsEntityFromUserChecker.UserNotAllowedException {
        if(! (userChecker.isUserAdmin(req) || userChecker.isMyPerson(req, cvdto.getOwner().getId())))
            throw new IsEntityFromUserChecker.UserNotAllowedException();

        CV cv = mapper.map(cvdto, CV.class);

        Optional.ofNullable(c_repo.findById(cv.getId()).isPresent() ? null : cv).orElseThrow(CVNotFoundException::new);

        return mapper.map(c_repo.save(cv), CVDTO.class);
    }


    /// PutMapping

    @PutMapping("")
    public CVDTO putCV(@RequestBody CVDTO cvdto, HttpServletRequest req) throws CVNotFoundException, IsEntityFromUserChecker.UserNotAllowedException {
        if(! (userChecker.isUserAdmin(req) || userChecker.isMyCV(req, cvdto.getId())))
            throw new IsEntityFromUserChecker.UserNotAllowedException();
        
        CV cv = mapper.map(cvdto, CV.class);

        Optional<CV> cv_data = c_repo.findById(cv.getId());
        cv_data.orElseThrow(CVNotFoundException::new);

        cv.setOwner(cv_data.get().getOwner());

        return mapper.map(c_repo.save(cv), CVDTO.class);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCV(@PathVariable int id, HttpServletRequest req) throws IsEntityFromUserChecker.UserNotAllowedException {
        if(! (userChecker.isUserAdmin(req) || userChecker.isMyCV(req, id)))
            throw new IsEntityFromUserChecker.UserNotAllowedException();

        c_repo.deleteById(id);
    }

    
    /// Exception

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class CVNotFoundException extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }
}
