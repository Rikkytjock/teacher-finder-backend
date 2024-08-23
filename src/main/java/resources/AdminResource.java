package resources;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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
    @Operation(
        summary = "Retrieve a list of all teachers",
        description = "Fetches a list of all teachers currently in the database. This endpoint is accessible only by admin users."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Successful retrieval of the teacher list. The response body contains an array of teacher objects."
        ),
        @APIResponse(
            responseCode = "401",
            description = "User is not authenticated or the authentication token is missing or invalid."
        ),
        @APIResponse(
            responseCode = "403",
            description = "Forbidden. The user does not have the necessary permissions to access this resource."
        ),
        @APIResponse(
            responseCode = "404",
            description = "Not found. No teachers were found in the database. This may occur if the database is empty."
        ),
        @APIResponse(
            responseCode = "500",
            description = "Internal server error. An unexpected error occurred while processing the request. Please try again later or contact support."
        )
    })
    @RolesAllowed({"admin"})
    @Path("/get-all-teachers")
    public Response getAllTeachers(@HeaderParam("Authorization") String token) {
        return adminService.getAllTeachers(token);
    }
    
    @PATCH
    @Operation(
        summary = "Change permission status for a teacher", 
        description = "Allows an admin to toggle the 'accountVerified' status of a teacher."
    )
    @APIResponse(
        responseCode = "200", 
        description = "Teacher verification status changed successfully.",
        content = @Content(mediaType = "application/json")
    )
    @APIResponse(
        responseCode = "401", 
        description = "User is not authenticated or the authentication token is missing or invalid."
    )
    @APIResponse(
        responseCode = "403", 
        description = "Forbidden. User does not have the necessary permissions."
    )
    @APIResponse(
        responseCode = "404", 
        description = "Not Found. Teacher with the provided email was not found in the database."
    )
    @APIResponse(
        responseCode = "500", 
        description = "Internal server error. An unexpected error occurred while processing the request. Please try again later or contact support."
    )
    @RolesAllowed({"admin"})
    @Path("/change-verification-status")
    public Response changeVerificationStatus(@QueryParam("email") String email, @HeaderParam("Authorization") String token) {
        return adminService.changeVerificationStatus(email, token);
    }

    @DELETE
    @Operation(
        summary = "Delete a teacher account",
        description = "Removes a teacher's account from the database, including all associated data. This endpoint is accessible only by admin users."
    )
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "The teacher account was successfully deleted from the database."
        ),
        @APIResponse(
            responseCode = "400",
            description = "Bad request. The provided email is invalid or missing."
        ),
        @APIResponse(
            responseCode = "401",
            description = "Unauthorized. User is not authenticated or the authentication token is missing or invalid."
        ),
        @APIResponse(
            responseCode = "403",
            description = "Forbidden. The user does not have the necessary permissions to perform this operation."
        ),
        @APIResponse(
            responseCode = "404",
            description = "Not found. The teacher with the provided email could not be found in the database."
        ),
        @APIResponse(
            responseCode = "500",
            description = "Internal server error. An unexpected error occurred while processing the request. Please try again later or contact support."
        )
    })
    @RolesAllowed({"admin"})
    @Path("/delete-teacher-account")
    public Response deleteTeacherAccount(@QueryParam("email") String email, @HeaderParam("Authorization") String token) {
        return adminService.deleteTeacherAccount(email, token);
    }



    

}
