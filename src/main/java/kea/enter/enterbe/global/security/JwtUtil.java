package kea.enter.enterbe.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import java.time.ZonedDateTime;
import java.util.Date;
import javax.crypto.SecretKey;
import kea.enter.enterbe.api.auth.dto.MemberInfoDto;
import kea.enter.enterbe.global.common.exception.CustomException;
import kea.enter.enterbe.global.common.exception.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * [JWT 관련 메서드를 제공하는 클래스]
 */
@Slf4j
@Component
public class JwtUtil {

    private final SecretKey key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;

    public JwtUtil(
        @Value("${jwt.secret}") String secretKey,
        @Value("${jwt.access_exp}") long accessTokenExpTime,
        @Value("${jwt.refresh_exp}") long refreshTokenExpTime
    ) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }


    public String createAccessToken(MemberInfoDto member) {
        return createToken(member, accessTokenExpTime);
    }

    public String createRefreshToken(MemberInfoDto member) {
        return createToken(member, refreshTokenExpTime);
    }


    private String createToken(MemberInfoDto member, long expireTime) {
        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

            return Jwts.builder()
                .claim("memberName", member.getName())
                .claim("memberId", member.getId())
                .claim("role", member.getRole())
                .issuedAt(Date.from(now.toInstant()))
                .expiration(Date.from(tokenValidity.toInstant()))
                .signWith(key)
                .compact();
    }


    public Long getMemberId(String token) {
        return parseClaims(token).get("memberId", Long.class);
    }


    public boolean validateToken(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            throw new CustomException(ResponseCode.INVALID_TOKEN); // Invalid JWT signature
        } catch (ExpiredJwtException e) {
            throw new CustomException(ResponseCode.EXPIRED_TOKEN); // Expired JWT token
        } catch (UnsupportedJwtException e) {
            throw new CustomException(ResponseCode.UNSUPPORTED_TOKEN); // Unsupported JWT token
        } catch (IllegalArgumentException e) {
            throw new CustomException(ResponseCode.INVALID_HEADER_OR_COMPACT_JWT); // JWT token compact of handler are invalid
        }
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken).getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


}
