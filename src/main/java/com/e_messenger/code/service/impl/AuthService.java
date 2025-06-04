package com.e_messenger.code.service.impl;

import com.e_messenger.code.dto.requests.user.AuthRequest;
import com.e_messenger.code.dto.responses.AuthResponse;
import com.e_messenger.code.entity.User;
import com.e_messenger.code.exception.AppException;
import com.e_messenger.code.exception.StatusCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserService userService;
    PasswordEncoder encoder;
    RedisTemplate<String, String> redisTemplate;

    @NonFinal
    @Value("${jwt.issuer}")
    String issuer;
    @NonFinal
    @Value("${jwt.secret-key}")
    String secretKey;
    @NonFinal
    @Value("${jwt.duration.access-token}")
    int accessTokenDuration;
    @NonFinal
    @Value("${jwt.duration.refresh-token}")
    int refreshTokenDuration;

    private String getRefreshTokenKey(String userId){
        return "%s:refresh-token".formatted(userId);
    }

    private String getCachedRefreshToken(String userId){
        String refreshToken = redisTemplate.opsForValue().get(getRefreshTokenKey(userId));
        if(refreshToken == null)
            throw new AppException(StatusCode.UNCATEGORIZED);
        return refreshToken;
    }

    private String generateRefreshToken(User user){
        String refreshToken = getToken(user, refreshTokenDuration);
        redisTemplate.opsForValue().set(
                    getRefreshTokenKey(user.getId()),
                    refreshToken,
                    refreshTokenDuration,
                    TimeUnit.MINUTES
        );
        return refreshToken;
    }

    public AuthResponse logIn(AuthRequest request){
        User user = userService.getUserByIdentifier(request.getIdentifier());
        if(encoder.matches(request.getPassword(), user.getPassword())) {
            String refreshToken = generateRefreshToken(user);

            return AuthResponse.builder()
                    .userId(user.getId())
                    .accessToken(getToken(user, accessTokenDuration))
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new AppException(StatusCode.UNAUTHENTICATED);
    }

    public AuthResponse refreshAccessToken(String refreshToken){
        User user = userService.getCurrentUser();
        String getCachedRefreshToken = getCachedRefreshToken(user.getId());
        if(!refreshToken.equals(getCachedRefreshToken))
            throw new AppException(StatusCode.UNCATEGORIZED);

        String newRefreshToken = generateRefreshToken(user);
        return AuthResponse.builder()
                    .userId(user.getId())
                    .accessToken(getToken(user, accessTokenDuration))
                    .refreshToken(newRefreshToken)
                    .build();
    }

    private String getToken(User user, int duration){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issuer(issuer)
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(duration, ChronoUnit.MINUTES).toEpochMilli()
                ))
        .build();

        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject object = new JWSObject(header, payload);

        try{
            object.sign(new MACSigner(secretKey.getBytes()));
            return object.serialize();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
