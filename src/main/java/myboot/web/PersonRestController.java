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
import myboot.dao.PersonRepository;
import myboot.dto.PersonDTO;
import myboot.model.Person;

@RestController
@RequestMapping("/api/persons")
@Profile("usejwt")
public class PersonRestController {
    @Autowired
    PersonRepository p_repo;

    @Autowired
    LocalValidatorFactoryBean validationFactory;

    @Autowired
    IsEntityFromUserChecker userChecker;

    private ModelMapper mapper = new ModelMapper();

    /// GetMapping

    @GetMapping("")
    public Iterable<PersonDTO> getPersons(@RequestParam(defaultValue = "", required = false) String name, @RequestParam(defaultValue = "", required = false) String firstname){
        return p_repo.findByNameAndFirstName(name, firstname).stream().map( p -> mapper.map(p, PersonDTO.class)).toList() ;
    }

    @GetMapping("/{id}")
    public PersonDTO getPerson(@PathVariable int id) throws PersonNotFoundException {
        Optional<Person> p = p_repo.findById(id);
        p.orElseThrow(PersonNotFoundException::new);

        return mapper.map(p.get(), PersonDTO.class);
    }
    

    /// PostMapping

    @PostMapping("")
    public PersonDTO postPerson(@RequestBody PersonDTO pdto, HttpServletRequest req) throws PersonNotFoundException, IsEntityFromUserChecker.UserNotAllowedException {
        if(! (userChecker.isUserAdmin(req) || userChecker.isMySelf(req, pdto.getSelf().getUsername())))
            throw new IsEntityFromUserChecker.UserNotAllowedException();

        Person p = mapper.map(pdto, Person.class);

        Optional.ofNullable(p_repo.findById(p.getId()).isPresent() ? null : p).orElseThrow(PersonNotFoundException::new);

        return mapper.map(p_repo.save(p), PersonDTO.class);
    }


    /// PutMapping

    @PutMapping("")
    public PersonDTO putPerson(@RequestBody PersonDTO pdto, HttpServletRequest req) throws PersonNotFoundException, IsEntityFromUserChecker.UserNotAllowedException {
        if(! (userChecker.isUserAdmin(req) || userChecker.isMyPerson(req, pdto.getId())))
            throw new IsEntityFromUserChecker.UserNotAllowedException();
        
        Person p = mapper.map(pdto, Person.class);

        Optional<Person> p_data = p_repo.findById(p.getId());
        p_data.orElseThrow(PersonNotFoundException::new);
        p.setSelf(p_data.get().getSelf());

        return mapper.map(p_repo.save(p), PersonDTO.class);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePerson(@PathVariable int id, HttpServletRequest req) throws IsEntityFromUserChecker.UserNotAllowedException {
        if(! (userChecker.isUserAdmin(req) || userChecker.isMyPerson(req, id)))
            throw new IsEntityFromUserChecker.UserNotAllowedException();

        p_repo.deleteById(id);
    }


    /// Exception

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public static class PersonNotFoundException extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }
}
