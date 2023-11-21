package myboot.dto;

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
public class CVDTO {

    @JsonView(View.Public.class)
    private int id;

    @JsonView(View.Public.class)
    private String title;

    @JsonView(View.Public.class)
    private String description;

    @JsonManagedReference
    private List<ActivityDTO> activities;

    @JsonBackReference
    @Exclude
    private PersonDTO owner;
}
