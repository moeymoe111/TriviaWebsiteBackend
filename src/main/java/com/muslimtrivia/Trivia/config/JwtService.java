package com.muslimtrivia.Trivia.config;

import com.muslimtrivia.Trivia.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String secretKey = "5971337336763979244226452948404D635166546A576E5A7234753777217A25\n";

    public String extractEmail(String token) {
        final Claims claims = extractClaims(token);
        return (String) claims.get("email");

    }

    public String extractUserName(String token) {
        final Claims claims = extractClaims(token);
        return (String) claims.get("username");
    }

    public Integer extractPoints(String token) {
        final Claims claims = extractClaims(token);
        return (Integer) claims.get("score");
    }

    public String extractFlag(String token) {
        final Claims claims = extractClaims(token);
        return (String) claims.get("flag");
    }

    public String tokenGenerator(User user) {
        return tokenGenerator(new HashMap<>(), user);
    }

    public String tokenGenerator(Map<String, Object> extraClaims, User user) {
        Claims claims = Jwts.claims();
        claims.putAll(extraClaims);
        claims.put("email", user.getEmail()); // Adding email to the claims
        claims.put("username", user.getUsername()); // Adding username to the claims
        claims.put("flag", user.getFlag()); // Adding flag to the claims
        claims.put("score", user.getScore()); // Adding flag to the claims

        return Jwts.builder()
                .setId(UUID.randomUUID().toString()) // Generate a random UUID for token ID
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean tokenValidation(String token, UserDetails details) {
        final String email = extractUserName(token);
        return email.equals(details.getUsername()) && !tokenExpiration(token);
    }

    private boolean tokenExpiration(String token) {
        return extractExp(token).before(new Date());
    }

    private Date extractExp(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        final Claims claims = extractClaims(token);
        return resolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
