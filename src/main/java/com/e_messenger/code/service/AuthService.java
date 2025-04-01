package com.e_messenger.code.service;

import com.e_messenger.code.dto.requests.AuthRequest;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    UserService userService;
    PasswordEncoder encoder;

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

    public AuthResponse logIn(AuthRequest request){
        User user = userService.getUserByIdentifier(request.getIdentifier());
        if(encoder.matches(request.getPassword(), user.getPassword()))
            return AuthResponse.builder()
                    .accessToken(getToken(user, accessTokenDuration))
                    .refreshToken(getToken(user, refreshTokenDuration))
                    .build();
        throw new AppException(StatusCode.UNAUTHENTICATED);
    }

    private String getToken(User user, int duration){
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
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
