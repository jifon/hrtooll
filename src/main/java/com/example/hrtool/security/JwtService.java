package com.example.hrtool.security;


import com.example.hrtool.model.RefreshToken;
import com.example.hrtool.model.SystemUser;
import com.example.hrtool.repository.RefreshTokenRepository;
import com.example.hrtool.repository.SystemUserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${hrtool.secretkey}")
    private String SECRET_KEY;

    @Value("${hrtool.refreshToken.maxAge}")
    private int refreshMaxAge;

    @Value("${hrtool.accessToken.maxAge}")
    private int accessMaxAge;

    private final SystemUserRepository systemUserRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Long extractSystemUserId(String token) {
        return extractClaim(token, (claims) -> claims.get("System_UserId", Long.class));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateRefreshToken(Long id){

//        System.out.println("refreshToken MaxAge = " + refreshMaxAge);
//        System.out.println("accessToken MaxAge = " + accessMaxAge);
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(systemUserRepository.findById(id).orElseThrow());
        refreshToken.setExpiresAt(new Date(System.currentTimeMillis() + 1000 * refreshMaxAge));
        String randomString;
        do {
            randomString = generateRandomString();
        } while (refreshTokenRepository.existsByRefreshToken(randomString));
        refreshToken.setRefreshToken(randomString);
        refreshTokenRepository.save(refreshToken);
        return randomString;
    }

    public String generateAccessToken(String refreshToken){

        if(refreshTokenRepository.existsByRefreshToken(refreshToken)){
            RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken).get(0);
            if(token.getExpiresAt().after(new Date())){
                return generateAccessToken(token.getUser(), token.getExpiresAt());
            }
        }

        return "";

        //TODO hier in einem anderen Thread prüfen, ob irgendwelche abgelaufenen Token gelöscht werden können.
        // Wobei das theoretisch gar nicht aufgerufen wird. Also vielleicht in etwas wie dem DataCleanupJob im ttBackend
    }

    public String generateAccessToken(SystemUser user, Date expiresAt) {
        Map<String, Object> claims = new HashMap<>();

        UserDetails userDetails = toUserDetails(user);

        //Rolle aus dem UserDetails holen
        claims.put("ROLE", userDetails.getAuthorities().stream().toList().get(0).toString());
        claims.put("System_UserId", user.getId());

        return generateAccessToken(claims, userDetails, expiresAt);
    }

    public String generateAccessToken(Map<String, Object> extraClaims, UserDetails userDetails, Date expiresAt) {

        Date quarterHour = new Date(System.currentTimeMillis() + 1000 * accessMaxAge);
        Date expirationDate = quarterHour.before(expiresAt) ? quarterHour : expiresAt;

        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * THIS FUNCTION DOES NOT TEST, WETHER THE TOKEN IS EXPIRED
     * @param token
     * @param userDetails
     * @return
     */
    public boolean isAccessTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        final Long systemUserId = extractSystemUserId(token);
        return (username.equals(userDetails.getUsername())) && systemUserRepository.existsById(systemUserId);
    }

    private String generateRandomString() {
        byte[] randomBytes = new byte[64];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

    /**
     * not used because it will already throw an error that the token expired, when extracting the email
     * @param token
     * @return
     */
    public boolean isAccessTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public UserDetails toUserDetails(SystemUser user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
    }
}