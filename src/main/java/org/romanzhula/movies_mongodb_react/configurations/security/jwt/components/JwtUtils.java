package org.romanzhula.movies_mongodb_react.configurations.security.jwt.components;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.romanzhula.movies_mongodb_react.configurations.security.implementations.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpiration;

    @Value("${app.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, jwtCookie);

        return (cookie != null) ? cookie.getValue() : null;

    }

    public ResponseCookie generateJwtCookie(UserDetailsImpl userDetails) {
        int maxAgeDay = 60 * 60 * 24;
        int maxAgeHalfDay = 60 * 60 * 12;
        int maxAgeTwoHour = 60 * 60 * 2;

        String jwt = generateTokenFromUsername(userDetails.getUsername());

        return ResponseCookie
                .from(jwtCookie, jwt)
                .path("/api")
                .maxAge(maxAgeTwoHour)
                .httpOnly(true)
                .build()
        ;
    }

    private String generateTokenFromUsername(String username) {
        var issuedAt = LocalDateTime.now().toInstant(ZoneOffset.UTC);
        var expiration = issuedAt.plus(2, ChronoUnit.HOURS);

        return Jwts
                .builder()
                .subject(username)
                .issuedAt(Date.from(issuedAt)) //time now
                .expiration(Date.from(expiration)) //only 2 hours
                .signWith(key())
                .compact();
    }


    private SecretKey key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public ResponseCookie getCleanJwtCookie() {

        return ResponseCookie
                .from(jwtCookie, null)
                .path("/api")
                .build()
        ;
    }

    public String getUsernameFromJwtToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> function) {
        var claims = extractAllClaims(token);
        return function.apply(claims);
    }

    public Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts
                .parser()
                .verifyWith(key())
                .build()
                .parse(authToken)
            ;
        } catch (MalformedJwtException e) {
            logger.error("JWT token is invalid: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }

}
