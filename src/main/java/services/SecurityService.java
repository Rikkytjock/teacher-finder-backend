package services;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import config.AppConfig;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import models.LoginDto;

@Singleton
public class SecurityService {

    @Inject
    AppConfig appConfig;

    public Response userLogin(@Valid LoginDto loginDto) {
        String jwt = getJwt(loginDto);
        return Response.ok(jwt).build();
    }

    private String getJwt(@Valid LoginDto loginDto) {

        Set<String> roles = new HashSet<> (
            Arrays.asList("teacher")
        );

        return Jwt.issuer(appConfig.jwtIssuer())
            .subject(loginDto.getEmail())
            .groups(roles)
            .expiresAt(System.currentTimeMillis() + 86400)
            .sign();        
    }    
}
