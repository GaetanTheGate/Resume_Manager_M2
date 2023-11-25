package myboot.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import myboot.model.View;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO {

    @JsonView(View.Public.class)
    private int id;
    
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

    @JsonBackReference
    @Exclude
    private XUserDTO self;
}
