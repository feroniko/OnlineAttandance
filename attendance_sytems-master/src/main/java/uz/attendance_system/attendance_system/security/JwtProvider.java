package uz.attendance_system.attendance_system.security;

import java.util.Base64;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import uz.attendance_system.attendance_system.domain.UserRoles;

@Component
public class JwtProvider {
    
    @Value("${jwt.token.secret}")
    private String secret;

    @Value("${jwt.token.validity}")
    private Long validityMilLong;

    @PostConstruct
    protected void init(){
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    private final UserDetailsService userDetailsService;

    public JwtProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public String createToken(String userName, Set<UserRoles> set){
        String roles = set.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));
        Claims claims = Jwts.claims().setSubject(userName);
        claims.put("role", roles);
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityMilLong);
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(validity)
            .signWith(SignatureAlgorithm.HS256, this.secret)
            .compact();  
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails = userDetailsService.loadUserByUsername(getUserName(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    };

    private String getUserName(String token){
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validityToken(String token){
        try {
            Jws<Claims> claimJws = Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
            if(claimJws.getBody().getExpiration().before(new Date())){
                return false;
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
