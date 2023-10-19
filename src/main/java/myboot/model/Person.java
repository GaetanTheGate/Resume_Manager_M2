package myboot.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@Basic
    private String password;
    
	@Basic
    private String name;
	@Basic
    private String firstname;

	@Basic
    private String mail;
	@Basic
    private String website;

	@Basic
    private Date birthdate;

	@OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<CV> cvs;
}
