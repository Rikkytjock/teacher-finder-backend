package resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Teacher;
import services.TeacherService;

@Path("/teacher")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeacherResource {

    @Inject
    TeacherService teacherService;

    @POST
    @Path("/create-account")
    @PermitAll
    @Operation(
        summary = "Create a teacher account",
        description = "Creates a new teacher account with the provided details."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "201",
            description = "Teacher account created successfully.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @APIResponse(
            responseCode = "409",
            description = "Conflict: Email already in use.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Internal Server Error: Could not create account.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        )
    })
    public Response createAccount(@RequestBody Teacher teacher) {
        return teacherService.createAccount(teacher);
    }

    @GET
    @Path("/get-teacher")
    @RolesAllowed("teacher")
    @Operation(
        summary = "Get a single teacher",
        description = "Retrieves a teacher's details based on the provided JWT token."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Teacher details retrieved successfully.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = Teacher.class)
            )
        ),
        @APIResponse(
            responseCode = "401",
            description = "Unauthorized: Invalid or missing JWT token.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Teacher not found.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        )
    })
    public Response getTeacher(@HeaderParam("Authorization") String token) {
        return teacherService.getTeacher(token);
    }

    @PATCH
    @Path("/edit-account")
    @RolesAllowed("teacher")
    @Operation(
        summary = "Edit a teacher account",
        description = "Updates the details of an existing teacher account."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Teacher account updated successfully.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @APIResponse(
            responseCode = "401",
            description = "Unauthorized: Invalid or missing JWT token.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @APIResponse(
            responseCode = "404",
            description = "Teacher not found.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        ),
        @APIResponse(
            responseCode = "500",
            description = "Internal Server Error: Could not update account.",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON,
                schema = @Schema(implementation = String.class)
            )
        )
    })
    public Response editAccount(@HeaderParam("Authorization") String token, @RequestBody Teacher teacher) {
        return teacherService.editAccount(token, teacher);
    }
}
