package myboot.web;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import myboot.model.Person;
import myboot.model.XUser;
import myboot.dao.PersonRepository;
import myboot.dto.XUserDTO;
import myboot.dto.XUserSignUpDTO;
import myboot.security.JwtProvider;
import myboot.security.UserService;

/**
 * L'API d'authentification
 */
@RestController
@RequestMapping("/secu-users")
@Profile("usejwt")
public class UserController {

	@Autowired
	private UserService userService;

	private ModelMapper modelMapper = new ModelMapper();

	/**
	 * Authentification et récupération d'un JWT
	 */
	@PostMapping("/login")
	public String login(//
			@RequestParam String username, //
			@RequestParam String password) {
		return userService.login(username, password);
	}


	@Autowired
	PasswordEncoder encoder;

	@Autowired
	JwtProvider jwtTokenProvider;

	@Autowired
	PersonRepository p_repo;

	/**
	 * Ajouter un utilisateur
	 */
	@PostMapping("/signup")
	public String signup(@RequestBody XUserSignUpDTO user) {
		XUser u = modelMapper.map(user, XUser.class);
		u.setRoles(Set.of("USER"));

		String token = userService.signup(u);

		u = userService.search(jwtTokenProvider.getUsername(token));
		p_repo.save(new Person(0, "", user.getUsername(), user.getUsername(), "", "", null, new ArrayList<>(), u));

		return token;
	}

	/**
	 * Supprimer un utilisateur
	 */
	@DeleteMapping("/{username}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public String delete(@PathVariable String username) {
		System.out.println("delete " + username);
		userService.delete(username);
		return username;
	}

	/**
	 * Récupérer des informations sur un utilisateur
	 */
	@GetMapping("/{username}")
	@PreAuthorize("hasAuthority('ADMIN')")
	public XUserDTO search(@PathVariable String username) {
		return modelMapper.map(userService.search(username), XUserDTO.class);
	}

	/**
	 * Récupérer des informations sur l'utilisateur courant
	 */
	@GetMapping(value = "/me")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
	public XUserDTO whoami(HttpServletRequest req) {
		return modelMapper.map(userService.whoami(req), XUserDTO.class);
	}

	/**
	 * Récupérer un nouveau JWT
	 */
	@GetMapping("/refresh")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
	public String refresh(HttpServletRequest req) {
		return userService.refresh(req.getRemoteUser());
	}

	/**
	 * Oublier le jeton
	 */
	@GetMapping("/logout")
	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER')")
	public void logout(HttpServletRequest req){
		userService.logout(req);

	}
}
