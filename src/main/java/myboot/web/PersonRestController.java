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
import myboot.dao.PersonRepository;
import myboot.dto.PersonDTO;
import myboot.model.Person;

@RestController
@RequestMapping("/api/persons")
public class PersonRestController {
    @Autowired
    PersonRepository p_repo;

    @Autowired
    LocalValidatorFactoryBean validationFactory;

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
    public Person postPerson(@RequestBody PersonDTO pdto) throws PersonNotFoundException {
        Person p = mapper.map(pdto, Person.class);

        Optional.ofNullable(p_repo.findById(p.getId()).isPresent() ? null : p).orElseThrow(PersonNotFoundException::new);
        return p_repo.save(p);
    }


    /// PutMapping

    @PutMapping("")
    public Person putPerson(@RequestBody PersonDTO pdto) throws PersonNotFoundException {

        Person p = mapper.map(pdto, Person.class);
        
        p_repo.findById(p.getId()).orElseThrow(PersonNotFoundException::new);

        return p_repo.save(p);
    }


    /// DeleteMapping

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(@PathVariable int id) {
        p_repo.deleteById(id);
    }


    /// Exception

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public class PersonNotFoundException extends RuntimeException{
        private static final long serialVersionUID = 1L;
    }
}
