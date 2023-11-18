package myboot.model;

import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class XUser {

	@Id
	private String userName;

	@Basic
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> roles;

	@OneToOne(mappedBy = "self", fetch = FetchType.LAZY)
	@JsonManagedReference
	private Person self;
}
