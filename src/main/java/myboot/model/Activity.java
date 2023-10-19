package myboot.model;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode.Exclude;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

	@Basic
    @NotNull
    private Integer year;

	@Basic
    @NotBlank
    private String type;

	@Basic
    @NotBlank
    private String title;

	@Basic
    private String description;
	@Basic
    private String website;

	
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @Exclude
    @JsonBackReference
    @NotNull
    private CV cv;
}
