package myboot.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import myboot.model.Activity;
import myboot.model.CV;
import myboot.model.Person;

@SpringBootTest
public class TestRestController {
    
    private RestTemplate rest = new RestTemplate();

	@Value("${server.port:8081}")
	private Integer port;

    private String url = "http://localhost:8081/api";


    /// Person

    // Get

    @SuppressWarnings("all")
    @Test
    public void testGetPersonsWithNoValueInTable(){
        ResponseEntity<Iterable> response = rest.getForEntity(url+"/persons", Iterable.class);
        Iterable persons = response.getBody();
        List person_list = new ArrayList<>();

        persons.forEach(person_list::add);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, person_list.size());
    }

    @SuppressWarnings("all")
    @Test
    public void testPostAndGetPersons(){
        testPostPerson();
        testPostPerson();


        ResponseEntity<Iterable> response = rest.getForEntity(url+"/persons", Iterable.class);
        Iterable persons = response.getBody();
        List person_list = new ArrayList<>();

        persons.forEach(person_list::add);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(0, person_list.size());
    }

    @Test
    public void testGetPerson(){
        testPostPerson();

        ResponseEntity<Person> response = rest.getForEntity(url+"/persons/1", Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(0, response.getBody().getId());
    }

    @Test
    public void testGetNotExistingPerson(){
        assertThrows(Exception.class, () -> rest.getForEntity(url+"/persons/99999999", Person.class));
        assertThrows(Exception.class, () -> rest.getForEntity(url+"/persons/0", Person.class));
    }

    // Post

    @Test
    public void testPostPerson(){
        Person p = new Person(0, "test", "test", "test", "test", "test", null, new ArrayList<>());
        ResponseEntity<Person> response = rest.postForEntity(url+"/persons", p, Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(0, response.getBody().getId());
    }

    @Test
    public void testPostPersonWithInvalidData(){
        // No constraint to violate
    }

    // todo corriger pour que le test fonctionne
    @Test
    public void testPostPersonAlreadyExisting(){
        Person p = new Person(0, "test", "test", "test", "test", "test", null, new ArrayList<>());
        ResponseEntity<Person> response = rest.postForEntity(url+"/persons", p, Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(0, response.getBody().getId());

        p = response.getBody();
        System.out.println(p.getId());
        response = rest.postForEntity(url+"/persons", p, Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(p.getId(), response.getBody().getId());
    }

    // todo corriger pour que le test fonctionne
    @Test
    public void testPutPerson(){
        Person p = new Person(0, "test", "test", "test", "test", "test", null, new ArrayList<>());
        ResponseEntity<Person> response = rest.postForEntity(url+"/persons", p, Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(0, response.getBody().getId());

        p = response.getBody();
        p.setName("Another one");

        rest.put(url+"/persons", p);

        response = rest.getForEntity(url+"/persons/"+p.getId(), Person.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(p.getName(), response.getBody().getName());
    }

    // Delete

    @Test
    public void testDeletePerson(){
        Person p = rest.postForEntity(url+"/persons", new Person(0, "test", "test", "test", "test", "test", null, new ArrayList<>()), Person.class).getBody();

        rest.getForEntity(url+"/persons/"+p.getId(), Person.class);
        rest.delete(url+"/persons/"+p.getId());
        assertThrows(Exception.class, () -> rest.getForEntity(url+"/persons/"+p.getId(), Person.class));
    }

    @Test
    public void testDeleteNonExistingPerson(){
        assertThrows(Exception.class, () -> rest.delete(url+"/persons/0"));
    }

}
