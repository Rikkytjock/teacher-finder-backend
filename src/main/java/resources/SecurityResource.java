package resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.LoginDto;
import models.JwtResponse;
import services.SecurityService;

@SecurityScheme(
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
@Path("/security")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SecurityResource {

    @Inject
    SecurityService securityService;
    
    @POST
    @Operation(
        summary = "User Login",
        description = "Both admin and teachers log in here."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "The user has successfully logged in and received their Json Web Token.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = JwtResponse.class)
            )
        ),
        @APIResponse(
            responseCode = "401",
            description = "The user has entered bad credentials.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Error response for bad credentials.")
            )
        ),
        @APIResponse(
            responseCode = "403",
            description = "The teacher's account has not yet been verified.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Error response for unverified account.")
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "The teacher was not found.",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(description = "Error response for not found teacher.")
            )
        )
    })
    @Path("/login")
    @PermitAll
    public Response userLogin(@Valid @RequestBody final LoginDto loginDto) {
        return securityService.userLogin(loginDto);
    }
}
