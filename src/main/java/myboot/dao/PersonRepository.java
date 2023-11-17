package myboot.dao;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import myboot.model.CV;
import myboot.model.Person;

@Repository
@Transactional
public interface PersonRepository extends CrudRepository<Person, Integer> {
    @Query("SELECT p FROM Person p WHERE UPPER(p.name) LIKE CONCAT('%', CONCAT(UPPER(:name), '%')) AND UPPER(p.firstname) LIKE CONCAT('%', CONCAT(UPPER(:firstname), '%'))")
    List<Person> findByNameAndFirstName(String name, String firstname);

    @Query("SELECT p.cvs FROM Person p WHERE p.id = :id")
    List<CV> getCVs(int id);
}