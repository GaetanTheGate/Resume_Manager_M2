package myboot.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonView;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import myboot.model.View;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class XUserDTO {

	private String username;
	private List<String> roles;

    @JsonView(View.Public.class)
    private PersonDTO self;
}
