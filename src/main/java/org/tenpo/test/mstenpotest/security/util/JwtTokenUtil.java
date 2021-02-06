package org.tenpo.test.mstenpotest.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.tenpo.test.mstenpotest.security.user.User;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Component
@Slf4j
public class JwtTokenUtil {

    @Value("${auth.jwt.issuer}")
    private String issuer;

    @Value("${auth.jwt.secret}")
    private String secret;

    @Value("${auth.jwt.ttl-in-seconds}")
    private long timeToLiveInSeconds;

    private static final String CLAIM_FIRST_NAME_KEY = "FirstName";
    private static final String CLAIM_LAST_NAME_KEY = "LastName";

    private SecretKey secretKey;

    @PostConstruct
    public void setUpSecretKey() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateToken(User user) {

        String jwt =
                Jwts.builder()
                        .setId(UUID.randomUUID().toString())
                        .setSubject(user.getUsername())
                        .setIssuer(issuer)
                        .setIssuedAt(Date.from(Instant.now()))
                        .setExpiration(Date.from(Instant.now().plus(Duration.ofSeconds(timeToLiveInSeconds))))
                        .claim(CLAIM_FIRST_NAME_KEY, user.getFirstName())
                        .claim(CLAIM_LAST_NAME_KEY, user.getLastName())
                        .signWith(secretKey)
                        .compact();
        return jwt;
    }

    public Claims parseToken(String jwtString) {

        Jws<Claims> headerClaimsJwt =
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(jwtString);

        Claims claims = headerClaimsJwt.getBody();

        return claims;
    }

}
