package myboot.security;

import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import myboot.model.XUser;

/**
 * Création/vérification/gestion d'un JWT
 */
@Component
@Profile("usejwt")
public class JwtProvider {

	/**
	 * Par simplicité, nous stockons la clef de manière statique. il est sans doute
	 * préférable d'avoir un autre API (sur un serveur de configuration) qui nous
	 * fournisse la clé.
	 */
	@Value("${security.jwt.token.secret-key:secret-key}")
	private String secretKey;

	@Value("${security.jwt.token.expire-length:3600000}")
	private long validityInMilliseconds = 3600000; // 1h

	@Autowired
	private JwtUserDetails myUserDetails;

	private List<String> tokens = new ArrayList<>();

	@PostConstruct
	protected void init() {
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	public String createToken(XUser user) {

		Claims claims = Jwts.claims().setSubject(user.getUserName());
		claims.put("auth", user.getRoles().stream().filter(Objects::nonNull).collect(Collectors.toList()));

		Date now = new Date();
		Date validity = new Date(now.getTime() + validityInMilliseconds);

		String token = Jwts.builder()//
				.setClaims(claims)//
				.setIssuedAt(now)//
				.setExpiration(validity)//
				.signWith(SignatureAlgorithm.HS256, secretKey)//
				.compact();

		tokens.add(token);

		return token;
	}

	public Authentication getAuthentication(String token) {
		UserDetails userDetails = myUserDetails.loadUserByUsername(getUsername(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	public String getUsername(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest req) {
		String bearerToken = req.getHeader("Authorization");
		if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring(7);
		}
		return null;
	}

	public boolean validateToken(String token) {
		System.out.println("Before validate token " + token);
		try {
			if(token == null)
				return false;
			if(! tokens.contains(token))
				return false;

			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (MyJwtException | IllegalArgumentException e) {
			removeToken(token);
			throw new MyJwtException("Expired or invalid JWT token", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public void removeToken(String token){
		tokens.remove(token);
	}

}
