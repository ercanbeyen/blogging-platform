package com.ercanbeyen.bloggingplatform.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ercanbeyen.bloggingplatform.constant.messages.JwtMessage;
import com.ercanbeyen.bloggingplatform.constant.values.TokenTimes;
import com.ercanbeyen.bloggingplatform.document.Author;
import com.ercanbeyen.bloggingplatform.service.AuthorService;
import com.ercanbeyen.bloggingplatform.util.JwtUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtService {
    private final AuthorService authorService;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Map<String, String> generateTokens(UserDetails userDetails) {
        return generateTokens(new HashMap<>(), userDetails);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Map<String, String> generateTokens(Map<String, Object> extraClaims, UserDetails userDetails) {
        Map<String, String> tokenMap = new HashMap<>();

        Algorithm algorithm = Algorithm.HMAC256(JwtMessage.SECRET_KEY.getBytes());

        String accessToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(JwtUtils.calculateExpirationDate(TokenTimes.ACCESS_TOKEN))
                .withClaim(JwtMessage.PAYLOAD_ROLES_KEY, userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);

        tokenMap.put(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN, accessToken);

        String refreshToken = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(JwtUtils.calculateExpirationDate(TokenTimes.REFRESH_TOKEN))
                .sign(algorithm);

        tokenMap.put(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN, refreshToken);

        return tokenMap;
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = JwtMessage.SECRET_KEY.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Map<String, String> refreshToken(String authorizationHeader) {
        String refreshToken = authorizationHeader.substring(JwtMessage.BEARER.length());
        Algorithm algorithm = Algorithm.HMAC256(JwtMessage.SECRET_KEY.getBytes());
        JWTVerifier jwtVerifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = jwtVerifier.verify(refreshToken);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put(JwtMessage.TOKEN_MAP_KEY_REFRESH_TOKEN, refreshToken);

        String username = decodedJWT.getSubject();
        Author author = authorService.getAuthorByUsername(username);

        String accessToken = JWT.create()
                .withSubject(author.getUsername())
                .withExpiresAt(JwtUtils.calculateExpirationDate(TokenTimes.ACCESS_TOKEN))
                .withClaim(JwtMessage.PAYLOAD_ROLES_KEY, author.getRoles().stream()
                        .map(role -> String.valueOf(role.getRoleName())).toList())
                .sign(algorithm);

        tokenMap.put(JwtMessage.TOKEN_MAP_KEY_ACCESS_TOKEN, accessToken);

        return tokenMap;
    }
}
