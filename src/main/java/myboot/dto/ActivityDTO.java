package myboot.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString.Exclude;
import myboot.model.View;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ActivityDTO {

    @JsonView(View.Public.class)
    private int id;

    @JsonView(View.Public.class)
    private Integer year;

    @JsonView(View.Public.class)
    private String type;

    @JsonView(View.Public.class)
    private String title;

    @JsonView(View.Public.class)
    private String description;

    @JsonView(View.Public.class)
    private String website;

    @JsonBackReference
    @Exclude
    private CVDTO cv;
}
