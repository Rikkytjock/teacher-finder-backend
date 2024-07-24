package resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Email;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Teacher;
import services.TeacherService;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TeacherResource {
    
    @Inject
    TeacherService teacherService;


    @GET
    @Operation(summary = "Get a single teacher", description = "After log in the JWT is sent here to retrieve a teachers details.")
    @APIResponse(responseCode = "401", description = "The teacher does not have permission to do this.")
    @APIResponse(responseCode = "404", description = "The teacher was not found")
    @APIResponse(responseCode = "200", description = "Teacher details returned successfully.")
    @RolesAllowed({"teacher"})
    @Path("/find-teacher")
    public Response findTeacher(@HeaderParam("Authorization") String token) {
        return teacherService.findTeacher(token);
    }

    @POST
    @Operation(summary = "Create a teacher account", description = "A new user enters all required fields and create a teacher account.")
    @APIResponse(responseCode = "404", description = "Page not found.")
    @APIResponse(responseCode = "200", description = "Teacher account created successfully.")
    @PermitAll
    @Path("/create-account")
    public Response createAccount(@RequestBody Teacher teacher) {
        return teacherService.createAccount(teacher);
    }

}

