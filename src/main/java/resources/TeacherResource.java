package resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    @Path("/find-teacher")
    public Response findTeacher() {
        return teacherService.findTeacher();
    }
}

