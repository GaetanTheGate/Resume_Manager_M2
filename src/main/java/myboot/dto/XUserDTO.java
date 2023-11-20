package myboot.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XUserDTO {

	private String username;
	private List<String> roles;

    @JsonManagedReference
    private PersonDTO self;
}
