package myboot.security;

import javax.annotation.PostConstruct;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration de Spring Security.
 */
@Configuration
@EnableWebSecurity
@Profile("usejwt")
public class JwtWebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JwtProvider jwtTokenProvider;

	protected final Log logger = LogFactory.getLog(getClass());

	@PostConstruct
	public void init() {
		logger.debug("--- INIT SPRING SECURITY JWT");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
        // Pas de vérification CSRF (cross site request forgery)
        http.csrf(csrf -> csrf.disable());

        // Spring security se doit gérer les sessions
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Déclaration des end-points
        http.authorizeRequests(requests -> requests//
                .antMatchers("/secu-users/login").permitAll()//
                .antMatchers("/secu-users/signup").permitAll()//
                .antMatchers("/secu-users/**").permitAll()//
                // Autoriser le reste...
                .anyRequest().permitAll());

		// Mise en place du filtre JWT
		http.apply(new JwtFilterConfigurer(jwtTokenProvider));

	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(12);
	}

	@Override
	@Bean
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

}
