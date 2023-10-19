package myboot.dao;

import myboot.model.Activity;
import myboot.model.CV;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface ActivityRepository extends CrudRepository<Activity, Integer>  {

    @Query("SELECT a FROM Activity a WHERE a.title LIKE CONCAT('%', CONCAT(:title, '%')) AND a.description LIKE CONCAT('%', CONCAT(:description, '%'))")
    List<Activity> findByTitleAndDescription(String title, String description);

    @Query("SELECT a.cv FROM Activity a WHERE a.id = :id")
    CV getCV(int id);
}
