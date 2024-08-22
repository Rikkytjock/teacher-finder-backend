package resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import services.AdminService;

@Path("/admin")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AdminResource {

    @Inject
    AdminService adminService;

    @GET
    @Operation(summary = "Get a list of teachers", description = "After log in a list of all teachers currently in database is returned for use by the admin.")
    @APIResponse(responseCode = "401", description = "The user does not have permission to do this.")
    @APIResponse(responseCode = "403", description = "The user does not have permission to do this.")
    @APIResponse(responseCode = "404", description = "The database did not return any teachers.")
    @APIResponse(responseCode = "200", description = "Teacher list returned successfully.")
    @APIResponse(responseCode = "500", description = "Server error. Please try again or contact support.")
    @RolesAllowed({"admin"})
    @Path("/get-all-teachers")
    public Response getAllTeachers() {
        return adminService.getAllTeachers();
    }
    
    @PATCH
    @Operation(summary = "Change permission status for a teacher", description = "Admin toggles the current 'accountVerified' status of the teacher.")
    @APIResponse(responseCode = "401", description = "The user does not have permission to do this.")
    @APIResponse(responseCode = "403", description = "The user does not have permission to do this.")
    @APIResponse(responseCode = "404", description = "The database did not find the teacher requested.")
    @APIResponse(responseCode = "200", description = "Teacher verification changed successfully.")
    @APIResponse(responseCode = "500", description = "Server error. Please try again or contact support.")
    @RolesAllowed({"admin"})
    @Path("/change-verification-status")
    public Response changeVerificationStatus(@HeaderParam("email") String email) {
        return adminService.changeVerificationStatus(email);
    }
    

}
