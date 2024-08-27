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
        Document userDocument = mongoDBService.getAdminCollection()
            .find(new Document("email", loginDto.getEmail()))
            .first();

        if (userDocument == null) {
            userDocument = mongoDBService.getTeacherCollection()
                .find(new Document("email", loginDto.getEmail()))
                .first();
        }

        if (userDocument == null) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("No account found for email: " + loginDto.getEmail())
                .build();
        }

        int authStatus = checkPassword(loginDto, userDocument);

        switch (authStatus) {
            case 0:  
            case 3:  
                String jwt = generateJwt(userDocument);
                return Response.ok(jwt).build();
            case 1: 
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Incorrect password. Please try again.")
                    .build();
            case 2:
                return Response.status(Response.Status.FORBIDDEN)
                    .entity("Account not verified. If you created your account more than 48 hours ago please contact support.")
                    .build();
            default: 
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An unexpected error occurred. Please try again later.")
                    .build();
        }
    }

    private int checkPassword(@Valid LoginDto loginDto, Document userDocument) {
        if (BCrypt.checkpw(loginDto.getPassword(), userDocument.get("password").toString())) {
            if ("admin".equals(userDocument.get("role"))) {
                return 3;  
            }
            if (Boolean.TRUE.equals(userDocument.getBoolean("accountVerified"))) {
                return 0; 
            } else {
                return 2;  
            }
        }
        return 1; 
    }

    private String generateJwt(Document userDocument) {
        Instant expirationTime = Instant.now().plus(Duration.ofDays(1));
        return Jwt.issuer(appConfig.jwtIssuer())
                .subject(userDocument.get("email").toString())
                .groups(userDocument.get("role").toString())
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

        Document teacherDocument = mongoDBService.getTeacherCollection()
            .find(new Document("email", email))
            .first();
        if (teacherDocument == null) {
            return new JwtValidationResult(Response.status(Response.Status.NOT_FOUND)
                .entity("Teacher not found with email: " + email)
                .build(), null);
        }

        return new JwtValidationResult(null, teacherDocument);
    }

    public Response checkJwt(String token) {
        if (token == null || token.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                .entity("Token is missing or empty")
                .build();
        }

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            JsonWebToken jwt = jwtParser.parse(token);
            String issuer = jwt.getIssuer();
            String subject = jwt.getSubject();
            Long expirationTime = jwt.getExpirationTime();
            Set<String> roles = jwt.getGroups();

            if (issuer == null || subject == null || expirationTime == null || roles == null) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token is missing required claims")
                    .build();
            }

            Instant expirationInstant = Instant.ofEpochSecond(expirationTime);
            if (Instant.now().isAfter(expirationInstant)) {
                return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Token is expired")
                    .build();
            }

            JwtResponse jwtResponse = new JwtResponse();
            jwtResponse.setIssuer(issuer);
            jwtResponse.setSubject(subject);
            jwtResponse.setRoles(roles);

            return Response.ok(jwtResponse).build();
        } catch (ParseException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Invalid token format")
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("An unexpected error occurred while processing the token.")
                .build();
        }
    }
}
