package myboot.dao;


import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import myboot.model.Activity;
import myboot.model.CV;
import myboot.model.Person;


@Repository
@Transactional
public interface CVRepository extends CrudRepository<CV, Integer>{

    @Query("SELECT c FROM CV c WHERE c.title LIKE CONCAT('%', CONCAT(:title, '%')) AND c.description LIKE CONCAT('%', CONCAT(:description, '%'))")
    List<CV> findByTitleAndDescription(String title, String description);

    @Query("SELECT c.owner FROM CV c WHERE c.id = :id")
    Person getOwner(int id);

    @Query("SELECT c.activities FROM CV c WHERE c.id = :id")
    List<Activity> getActivities(int id);
}
