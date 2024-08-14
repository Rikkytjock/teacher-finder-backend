package resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import models.Program;
import services.ProgramService;

@Path("program")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProgramResource {

    @Inject
    ProgramService programService;

    @POST
    @Operation(summary = "Submit a program", description = "A teacher enters required details and submits a new program.")
    @APIResponse(responseCode = "404", description = "Page not found.")
    @APIResponse(responseCode = "200", description = "Program submitted successfully.")
    @APIResponse(responseCode = "401", description = "You are not authorised to do this.")
    @RolesAllowed("teacher")
    @Path("/submit-program")
    public Response submitProgram(@HeaderParam("Authorization") String token, @RequestBody Program program) {
        return programService.submitProgram(token, program);
    }

    @PATCH
    @Operation(summary = "Edit a program", description = "A teacher enters required details to edit an existing program.")
    @APIResponse(responseCode = "404", description = "Page not found.")
    @APIResponse(responseCode = "200", description = "Program edited successfully.")
    @APIResponse(responseCode = "401", description = "You are not authorised to do this.")
    @RolesAllowed("teacher")
    @Path("/edit-program")
    public Response editProgram(@HeaderParam("Authorization") String token, @RequestBody Program program) {
        return programService.editProgram(token, program);
    }
}
