package User.Util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @SuppressWarnings("deprecation")
   	public String generateToken(String username) {
           return Jwts.builder()
                   .setSubject(username)
                   .setIssuedAt(new Date())
//                   .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                   .signWith(SignatureAlgorithm.HS256, secret)
                   .compact();
       }

       public String extractUsername(String token) {
           return getClaims(token).getSubject();
       }

       public boolean validateToken(String token, UserDetails userDetails) {
           final String username = extractUsername(token);
           return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
       }

       @SuppressWarnings("deprecation")
   	private Claims getClaims(String token) {
           return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
       }

       private boolean isTokenExpired(String token) {
           Date expiration = getClaims(token).getExpiration();
           return expiration.before(new Date());
       }
}
