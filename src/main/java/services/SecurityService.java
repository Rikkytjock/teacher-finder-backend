package services;

import java.time.Instant;
import java.util.Set;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jose4j.jwt.JwtClaims;

import config.AppConfig;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import models.JwtResponse;
import models.LoginDto;

@Singleton
public class SecurityService {

    @Inject
    AppConfig appConfig;

    @Inject JWTParser jwtParser;

    public Response userLogin(@Valid LoginDto loginDto) {
        String jwt = getJwt(loginDto);
        return Response.ok(jwt).build();
    }

    private String getJwt(@Valid LoginDto loginDto) {

        return Jwt.issuer(appConfig.jwtIssuer())
            .subject(loginDto.getEmail())
            .groups("teacher")
            .expiresAt(Instant.now().getEpochSecond() + 86400)
            .sign();        
    }    

    public Response checkJwt(String token) {

        try {

            if (token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            
            JsonWebToken jwt = jwtParser.parse(token);

            String issuer = jwt.getIssuer();
            String subject = jwt.getSubject();
            Long expirationTime = jwt.getExpirationTime();
            Set<String> roles = jwt.getGroups();

            Instant expirationInstant = Instant.ofEpochSecond(expirationTime);
            if (expirationInstant.isBefore(Instant.now())) {
                return Response.status(Response.Status.UNAUTHORIZED).entity("Token is expired").build();
            }

            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setIssuer(issuer);
            jwtResponse.setSubject(subject);
            jwtResponse.setRoles(roles);

            return Response.ok(jwtResponse).build();
        } catch (ParseException e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid token").build();
        }
    }
}
