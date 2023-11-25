package myboot.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import myboot.model.Activity;
import myboot.model.CV;
import myboot.model.Person;
import myboot.model.XUser;

@SpringBootTest
public class TestRepository {
    private int nb_a = 5;
    private int nb_c = 4;
    private int nb_p = 3;

    @Autowired
    protected ActivityRepository a_repo;
    @Autowired
    protected CVRepository c_repo;
    @Autowired
    protected PersonRepository p_repo;
    @Autowired
    protected XUserRepository u_repo;

    @SuppressWarnings("unused")
    private Activity createAndSaveActivity(){
        return a_repo.save(createActivity());
    }

    private Activity createActivity(){
        return new Activity(0, 2000, "test", "test", "description", "test", createAndSaveCV());
    }

    private CV createAndSaveCV(){
        return c_repo.save(createCV());
    }

    private CV createCV(){
        return new CV(0, "test", "test", null, createAndSavePerson());
    }

    private Person createAndSavePerson(){
        return p_repo.save(createPerson());
    }

    private Person createPerson(){
        return new Person(0, "test", "test", "test", "test", null, null, createUserAndSave());
    }

    private XUser createUserAndSave(){
        return u_repo.save(createUser());
    }

    private XUser createUser(){
        return new XUser("test", "test", Set.of("TEST"), null);
    }

    @BeforeEach
    @Test
    public void clearTables(){
        a_repo.deleteAll();
        c_repo.deleteAll();
        p_repo.deleteAll();


        assertEquals("[]", a_repo.findAll().toString());
        assertEquals("[]", c_repo.findAll().toString());
        assertEquals("[]", p_repo.findAll().toString());
    }

    @Test
    public void testBasicAddAndRemoveOnAllEntityWithLink(){
            XUser u = u_repo.save(new XUser("Username", "Password", null, null));
            Person p = p_repo.save(new Person(0, "Name", "FirstName", "mail", "web", null, null, u));
            CV cv = c_repo.save(new CV(0, "test", "test", null, p));
            Activity a = a_repo.save(new Activity(0, 2000, "test", "test", "description test", "test"+".fr", cv));

            assertEquals(u.getUserName(), p_repo.getSelf(p.getId()).getUserName());
            assertEquals(p.getId(), u_repo.getSelf(u.getUserName()).getId());

            assertEquals(p.getId(), c_repo.getOwner(cv.getId()).getId());
            assertEquals(cv.getId(), p_repo.getCVs(p.getId()).get(0).getId());

            assertEquals(cv.getId(), a_repo.getCV(a.getId()).getId());
            assertEquals(a.getId(), c_repo.getActivities(cv.getId()).get(0).getId());


            a_repo.delete(a);


            assertEquals(true, a_repo.findById(a.getId()).isEmpty());
            assertEquals(0, c_repo.getActivities(cv.getId()).size());


            c_repo.delete(cv);


            assertEquals(true, c_repo.findById(cv.getId()).isEmpty());
            assertEquals(0, p_repo.getCVs(p.getId()).size());


            p_repo.delete(p);


            assertEquals(true, p_repo.findById(p.getId()).isEmpty());
            assertEquals(null, u_repo.getSelf(u.getUserName()));

            u_repo.delete(u);

            assertEquals(true, u_repo.findById(u.getUserName()).isEmpty());
    }


    @Test
    public void testBasicAddOnActivityWithNonExistentCV(){
        assertThrows(Exception.class, () -> { a_repo.save(new Activity(0, 2000, "test", "test", "description test", "test"+".fr", createCV())); });
        assertEquals("[]", a_repo.findAll().toString());
    }

    @Test
    public void testBasicAddOnCVWithNonExistentPerson(){
        assertThrows(Exception.class, () -> { c_repo.save(new CV(0, "test", "test", new ArrayList<>(), createPerson())); });
        assertEquals("[]", c_repo.findAll().toString());
    }

    @Test
    public void testBasicAddOnPersonWithNonExistentUser(){
        assertThrows(Exception.class, () -> { p_repo.save(new Person(0, "test", "test", "test", "test", null, null, createUser())); });
        assertEquals("[]", a_repo.findAll().toString());
    }

    @Test
    void testAddMultipleCVsToPerson(){
        Person p = createAndSavePerson();
        for(int i = 0 ; i < 5 ; i++){
            CV cv = c_repo.save(new CV(0, "test", "test", null, p));

            assertEquals(p.getId(), c_repo.getOwner(cv.getId()).getId());
            assertEquals(cv.getId(), p_repo.getCVs(p.getId()).get(i).getId());
        }

        assertEquals(5, p_repo.getCVs(p.getId()).size());
    }

    @Test
    void testAddMultipleActivitiesToCV(){
        CV cv = createAndSaveCV();
        for(int i = 0 ; i < 5 ; i++){
            Activity a = a_repo.save(new Activity(0, 2000+i, String.valueOf(i), "test"+i, "description test"+i, "test"+i+".fr", cv));

            assertEquals(cv.getId(), a_repo.getCV(a.getId()).getId());
            assertEquals(a.getId(), c_repo.getActivities(cv.getId()).get(i).getId());
        }

        assertEquals(5, c_repo.getActivities(cv.getId()).size());
    }

    @Test
    public void testFillTablesWithData(){
        clearTables();

        for(int x = 0 ; x < nb_p ; x++){
            XUser u = u_repo.save(new XUser("test"+x, "test", null, null));
            Person p = p_repo.save(new Person(0+x, "Name"+x, "FirstName"+x, "mail"+x, "web"+x, null, null, u));

            for(int y = 0 ; y < nb_c ; y++){
                CV cv = c_repo.save(new CV(0, "test", "test", new ArrayList<>(), p));


                assertEquals(p.getId(), c_repo.getOwner(cv.getId()).getId());
                assertEquals(cv.getId(), p_repo.getCVs(p.getId()).get(y).getId());


                for(int z = 0 ; z < nb_a ; z++){
                    Activity a = a_repo.save(new Activity(0, 2000+z, String.valueOf(z), "test"+z, "description test"+z, "test"+z+".fr", cv));


                    assertEquals(cv.getId(), a_repo.getCV(a.getId()).getId());
                    assertEquals(a.getId(), c_repo.getActivities(cv.getId()).get(z).getId());
                }


                assertEquals(nb_a, c_repo.getActivities(cv.getId()).size());
            }
            assertEquals(nb_c, p_repo.getCVs(p.getId()).size());
        }


        List<Activity> as = new ArrayList<>();
        List<CV> cs = new ArrayList<>();
        List<Person> ps = new ArrayList<>();

        a_repo.findAll().forEach(as::add);
        c_repo.findAll().forEach(cs::add);
        p_repo.findAll().forEach(ps::add);

        assertEquals(nb_p*nb_c*nb_a,    as.size());
        assertEquals(nb_p*nb_c,         cs.size());
        assertEquals(nb_p,              ps.size());
    }
    
    @Test
    public void testSaveActivityWithInvalidConstraint(){
        Activity a = createActivity();
        a.setYear(null);
        assertThrows(Exception.class, () -> { a_repo.save(a) ; } );

        Activity b = createActivity();
        b.setType(null);
        assertThrows(Exception.class, () -> { a_repo.save(b) ; } );
        
        b.setType("");
        assertThrows(Exception.class, () -> { a_repo.save(b) ; } );

        Activity c = createActivity();
        c.setTitle(null);
        assertThrows(Exception.class, () -> { a_repo.save(c) ; } );

        c.setTitle("");
        assertThrows(Exception.class, () -> { a_repo.save(c) ; } );

        Activity d = createActivity();
        d.setCv(null);
        assertThrows(Exception.class, () -> { a_repo.save(d) ; } );

        assertEquals("[]", a_repo.findAll().toString());
    }
    
    @Test
    public void testSaveCVWithInvalidConstraint(){
        CV cv1 = createCV();
        cv1.setOwner(null);
        assertThrows(Exception.class, () -> { c_repo.save(cv1) ; } );

        CV cv2 = createCV();
        cv2.setTitle("");
        assertThrows(Exception.class, () -> { c_repo.save(cv2) ; } );


        assertEquals("[]", c_repo.findAll().toString());
    }
    
    @Test
    public void testSavePersonWithInvalidConstraint(){
        Person p1 = createPerson();
        p1.setName(null);
        assertThrows(Exception.class, () -> { p_repo.save(p1);});

        Person p2 = createPerson();
        p2.setName("");
        assertThrows(Exception.class, () -> { p_repo.save(p2);});

        Person p3 = createPerson();
        p3.setFirstname(null);
        assertThrows(Exception.class, () -> { p_repo.save(p3);});

        Person p4 = createPerson();
        p4.setFirstname("");
        assertThrows(Exception.class, () -> { p_repo.save(p4);});


        assertEquals("[]", p_repo.findAll().toString());
    }

    @Test
    public void testSaveUserWithInvalidConstraint(){
        XUser u1 = createUser();
        u1.setPassword(null);
        assertThrows(Exception.class, () -> { u_repo.save(u1);});


        XUser u2 = createUser();
        u2.setPassword("");
        assertThrows(Exception.class, () -> { u_repo.save(u2);});


        assertEquals("[]", u_repo.findAll().toString());
    }
}
