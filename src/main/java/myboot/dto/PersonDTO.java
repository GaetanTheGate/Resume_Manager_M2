package myboot.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.Data;
import myboot.model.View;

@Data
public class PersonDTO {

    @JsonView(View.Public.class)
    private int id;

    @JsonView(View.Public.class)
    private String password;
    
    @JsonView(View.Public.class)
    private String name;

    @JsonView(View.Public.class)
    private String firstname;

    @JsonView(View.Public.class)
    private String mail;

    @JsonView(View.Public.class)
    private String website;

    @JsonView(View.Public.class)
    private Date birthdate;

    @JsonManagedReference
    private List<CVDTO> cvs;

    @JsonView(View.Public.class)
    private XUserDTO self;
}
