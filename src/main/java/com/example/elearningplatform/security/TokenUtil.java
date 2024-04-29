package com.example.elearningplatform.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.elearningplatform.user.user.User;
import com.example.elearningplatform.user.user.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class TokenUtil {
@Autowired HttpServletRequest request;
@Autowired
UserRepository userRepository;

    /****************** */

    private final String CLAIMS_SUBJECT = "sub";
    private final String CLAIMS_CREATED = "created";

    // @Value("${auth.expiration}")
    // private Long TOKEN_VALIDITY = 604800L;

    @Value("${auth.secret}")
    private String TOKEN_SECRET;

    public String generateToken(String email,Integer userId ,Long TOKEN_VALIDITY) {
  
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIMS_SUBJECT, email);
        claims.put("userId", userId);
        claims.put(CLAIMS_CREATED, new Date());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(generateExpirationDate(TOKEN_VALIDITY))
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET).compact();
    }

    public String getUserName(String token) {
        if(token == null) return null;
      
        
        try {
            Claims claims = getClaims(token);

            return claims.getSubject();
        } catch (Exception ex) {
            return null;
        }
    }

    public Integer getUserId() {

            if(request.getHeader("Authorization") == null) return null;
            String token = request.getHeader("Authorization").substring("Bearer ".length());
            Claims claims = getClaims(token);

            return (Integer) claims.get("userId");
        }

        public User getUser() {

            if (request.getHeader("Authorization") == null)
            return null;
        String token = request.getHeader("Authorization").substring("Bearer ".length());
        Claims claims = getClaims(token);

        return userRepository.findById((Integer) claims.get("userId")).orElseThrow();

    }

    private Date generateExpirationDate(Long TOKEN_VALIDITY) {
        return new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = getUserName(token);

        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(TOKEN_SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception ex) {
            claims = null;
        }

        return claims;
    }
}
