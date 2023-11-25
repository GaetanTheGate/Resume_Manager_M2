package myboot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import myboot.model.Person;
import myboot.model.XUser;


@Repository
@Transactional
public interface XUserRepository extends JpaRepository<XUser, String> {
    @Query("SELECT x.self FROM XUser x WHERE x.userName = :userName")
    Person getSelf(String userName);
}
