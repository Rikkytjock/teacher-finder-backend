package services;

import java.time.Duration;
import java.time.Instant;
import java.util.Set;

import org.bson.Document;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.mindrot.jbcrypt.BCrypt;

import config.AppConfig;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import models.JwtResponse;
import models.JwtValidationResult;
import models.LoginDto;

@Singleton
public class SecurityService {

    @Inject
    AppConfig appConfig;

    @Inject 
    JWTParser jwtParser;

    @Inject 
    MongoDBService mongoDBService;

    public Response userLogin(@Valid LoginDto loginDto) {

        int checkPasswordAndAccountVerification = checkPassword(loginDto);

        if (checkPasswordAndAccountVerification == 0) {
            String jwt = getJwt(loginDto);
            return Response.ok(jwt).build();
        } else if (checkPasswordAndAccountVerification == 1) {
            return Response.status(Response.Status.UNAUTHORIZED).entity("Incorrect email or password. Please try again.").build();
        } else if (checkPasswordAndAccountVerification == 2) {
            return Response.status(Response.Status.FORBIDDEN).entity("Your account has not yet been verified. If you created your account more than 48 hours ago please contact support.").build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Something went wrong. Please try again.").build();
        }
    }

    private int checkPassword(@Valid LoginDto loginDto) {
        Document teacherDocument = mongoDBService.getTeacherCollection().find(new Document("email", loginDto.getEmail())).first();
        if (teacherDocument == null || !BCrypt.checkpw(loginDto.getPassword(), teacherDocument.get("password").toString())) {
            return 1;
        } else if (teacherDocument.getBoolean("accountVerified") == false) {
            return 2;
        } else if (teacherDocument != null && BCrypt.checkpw(loginDto.getPassword(), teacherDocument.get("password").toString())) {
            return 0;
        } else {
            return -1;
        }
         
    }

    private String getJwt(@Valid LoginDto loginDto) {
        Instant expirationTime = Instant.now().plus(Duration.ofDays(1));
    
        return Jwt.issuer(appConfig.jwtIssuer())
                .subject(loginDto.getEmail())
                .groups("teacher")
                .expiresAt(expirationTime.getEpochSecond())
                .sign();
    }
    
    
    public JwtValidationResult validateJwtAndGetTeacher(String token) {

        Response validateJwt = checkJwt(token);
        if (validateJwt.getStatus() != Response.Status.OK.getStatusCode()) {
            return new JwtValidationResult(validateJwt, null); 
        }

        JwtResponse jwtResponse = (JwtResponse) validateJwt.getEntity();
        String email = jwtResponse.getSubject();

        Document teacherDocument = mongoDBService.getTeacherCollection().find(new Document("email", email)).first();
        if (teacherDocument == null) {
            return new JwtValidationResult(Response.status(Response.Status.NOT_FOUND).entity("Teacher not found").build(), null);
        }

        return new JwtValidationResult(null, teacherDocument);
    }

    private Response checkJwt(String token) {

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
            if (Instant.now().isAfter(expirationInstant)) { 
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
