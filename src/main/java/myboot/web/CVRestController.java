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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import myboot.dao.CVRepository;
import myboot.dto.CVDTO;
import myboot.model.CV;

@RestController
@RequestMapping("/api/cvs")
public class CVRestController {
    @Autowired
    CVRepository c_repo;

    @Autowired
    LocalValidatorFactoryBean validationFactory;

    private ModelMapper mapper = new ModelMapper();

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
    public CVDTO postCV(@RequestBody CVDTO cvdto) throws CVNotFoundException {
        CV cv = mapper.map(cvdto, CV.class);


        Optional<CV> cv_data = c_repo.findById(cv.getId());
        Optional.ofNullable(cv_data.isPresent() ? null : cv).orElseThrow(CVNotFoundException::new);

        cv.setOwner(cv_data.get().getOwner());

        return mapper.map(c_repo.save(cv), CVDTO.class);
    }


    /// PutMapping

    @PutMapping("")
    public CVDTO putCV(@RequestBody CVDTO cvdto) throws CVNotFoundException {
        CV cv = mapper.map(cvdto, CV.class);

        c_repo.findById(cv.getId()).orElseThrow(CVNotFoundException::new);

        return mapper.map(c_repo.save(cv), CVDTO.class);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCV(@PathVariable int id) {
        c_repo.deleteById(id);
    }

    
    /// Exception

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class CVNotFoundException extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }
}
