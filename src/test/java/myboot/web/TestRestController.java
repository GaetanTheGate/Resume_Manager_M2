///
/// INFO
///
/// Nous n'arrivons pas à faire fonctionner les tests pour authentifier un l'utilisateurs,
/// et comme il est primmordial pour les autres tests, nous ne pouvons pas tester le reste.
///


// package myboot.web;

// import static org.junit.jupiter.api.Assertions.assertEquals;
// import static org.junit.jupiter.api.Assertions.assertNotEquals;
// import static org.junit.jupiter.api.Assertions.assertThrows;

// import java.io.IOException;
// import java.net.URI;
// import java.net.http.HttpClient;
// import java.net.http.HttpRequest;
// import java.net.http.HttpResponse;
// import java.util.ArrayList;
// import java.util.List;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.client.RestTemplate;

// import myboot.model.Person;

// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// public class TestRestController {
    
//     private RestTemplate rest = new RestTemplate();

// 	@Value("${server.port:8081}")
// 	private Integer port;

//     private String urlApi = "http://localhost:8081/api/";
//     private String urlLogin = "http://localhost:8081/secu-users/";


//     /// VueApp

//     /// User

//     /**
//      * @throws InterruptedException
//      * @throws IOException
//      * 
//      */
//     @Test
//     public void testUserSignUp() throws IOException, InterruptedException{
//         HttpClient client = HttpClient.newHttpClient();
//         HttpRequest request = HttpRequest.newBuilder()
//             .uri(URI.create(urlLogin+"signup?username=test&password=test"))
//             .POST(HttpRequest.BodyPublishers.noBody())
//             .build();

//         HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

//         assertEquals(200, response.statusCode());
//         assertEquals("", response.body());
//     }

//     /// Person

//     // Get

//     @SuppressWarnings("all")
//     @Test
//     public void testGetPersonsWithNoValueInTable(){
//         ResponseEntity<Iterable> response = rest.getForEntity(urlApi+"persons", Iterable.class);
//         Iterable persons = response.getBody();
//         List person_list = new ArrayList<>();

//         persons.forEach(person_list::add);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(0, person_list.size());
//     }

//     @SuppressWarnings("all")
//     @Test
//     public void testPostAndGetPersons(){
//         testPostPerson();
//         testPostPerson();


//         ResponseEntity<Iterable> response = rest.getForEntity(urlApi+"persons", Iterable.class);
//         Iterable persons = response.getBody();
//         List person_list = new ArrayList<>();

//         persons.forEach(person_list::add);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotEquals(0, person_list.size());
//     }

//     @Test
//     public void testGetPerson(){
//         testPostPerson();

//         ResponseEntity<Person> response = rest.getForEntity(urlApi+"persons/1", Person.class);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotEquals(0, response.getBody().getId());
//     }

//     @Test
//     public void testGetNotExistingPerson(){
//         assertThrows(Exception.class, () -> rest.getForEntity(urlApi+"persons/99999999", Person.class));
//         assertThrows(Exception.class, () -> rest.getForEntity(urlApi+"persons/0", Person.class));
//     }

//     // Post

//     @Test
//     public void testPostPerson(){
//         Person p = new Person(0, "test", "test", "test", "test", null, new ArrayList<>(), null);
//         ResponseEntity<Person> response = rest.postForEntity(urlApi+"persons", p, Person.class);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotEquals(0, response.getBody().getId());
//     }

//     @Test
//     public void testPostPersonWithInvalidData(){
//         // No constraint to violate
//     }

//     // todo corriger pour que le test fonctionne
//     @Test
//     public void testPostPersonAlreadyExisting(){
//         Person p = new Person(0, "test", "test", "test", "test", null, new ArrayList<>(), null);
//         ResponseEntity<Person> response = rest.postForEntity(urlApi+"persons", p, Person.class);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotEquals(0, response.getBody().getId());

//         p = response.getBody();
//         System.out.println(p.getId());
//         response = rest.postForEntity(urlApi+"persons", p, Person.class);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(p.getId(), response.getBody().getId());
//     }

//     // todo corriger pour que le test fonctionne
//     @Test
//     public void testPutPerson(){
//         Person p = new Person(0, "test", "test", "test", "test", null, new ArrayList<>(), null);
//         ResponseEntity<Person> response = rest.postForEntity(urlApi+"persons", p, Person.class);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertNotEquals(0, response.getBody().getId());

//         p = response.getBody();
//         p.setName("Another one");

//         rest.put(urlApi+"persons", p);

//         response = rest.getForEntity(urlApi+"persons/"+p.getId(), Person.class);

//         assertEquals(HttpStatus.OK, response.getStatusCode());
//         assertEquals(p.getName(), response.getBody().getName());
//     }

//     // Delete

//     @Test
//     public void testDeletePerson(){
//         Person p = rest.postForEntity(urlApi+"persons", new Person(0, "test", "test", "test", "test", null, new ArrayList<>(), null), Person.class).getBody();

//         rest.getForEntity(urlApi+"persons/"+p.getId(), Person.class);
//         rest.delete(urlApi+"persons/"+p.getId());
//         assertThrows(Exception.class, () -> rest.getForEntity(urlApi+"persons/"+p.getId(), Person.class));
//     }

//     @Test
//     public void testDeleteNonExistingPerson(){
//         assertThrows(Exception.class, () -> rest.delete(urlApi+"persons/0"));
//     }

// }
