package myboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import myboot.dao.XUserRepository;

/**
 * Une nouvelle version de la classe qui code la description d'un utilisateur
 * connecté.
 */
@Service
@Profile("usejwt")
public class JwtUserDetails implements UserDetailsService {

	@Autowired
	private XUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		final var user = userRepository.findById(username).orElseThrow(() -> {
			throw new UsernameNotFoundException("User '" + username + "' not found");
		});

		var authorites = user.getRoles().stream().map((role) -> {
			return new SimpleGrantedAuthority(role);
		}).toList();
		return org.springframework.security.core.userdetails.User//
				.withUsername(username)//
				.password(user.getPassword())//
				.authorities(authorites)//
				.accountExpired(false)//
				.accountLocked(false)//
				.credentialsExpired(false)//
				.disabled(false)//
				.build();
	}

}
