package services;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class AdminService {

    @Inject
    MongoClient mongoClient;

    @Inject
    MongoDBService mongoDBService;

    @Inject
    SecurityService securityService;

    public Response getAllTeachers(String token) {
        Response validationResponse = validateToken(token);
        if (validationResponse != null) {
            return validationResponse;
        }

        MongoCollection<Document> collection = mongoDBService.getTeacherCollection();
        List<Document> teachers = collection.find().into(new ArrayList<>());
        
        if (teachers.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No teachers found in the database.").build();
        }

        return Response.ok(teachers).build();
    }

    public Response changeVerificationStatus(String email, String token) {
        Response validationResponse = validateToken(token);
        if (validationResponse != null) {
            return validationResponse;
        }
        
        MongoCollection<Document> teacherCollection = mongoDBService.getTeacherCollection();
        Document teacherDocument = teacherCollection.find(new Document("email", email)).first();

        if (teacherDocument == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Teacher with email " + email + " not found.").build();
        }
        
        try {
            boolean newStatus = !teacherDocument.getBoolean("accountVerified");
            teacherCollection.updateOne(
                new Document("email", email),
                new Document("$set", new Document("accountVerified", newStatus))
            );

            return Response.ok(email + " verification status successfully changed to " + newStatus + ".").build();
        } catch (MongoException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to change verification status due to: " + e.getMessage()).build();
        }
    }

    public Response deleteTeacherAccount(String email, String token) {
        Response validationResponse = validateToken(token);
        if (validationResponse != null) {
            return validationResponse;
        }

        try {
            DeleteResult result = mongoDBService.getTeacherCollection().deleteOne(new Document("email", email));
            
            if (result.getDeletedCount() > 0) {
                return Response.ok("Account connected to " + email + " has been successfully deleted.").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("No account found for email " + email + ".").build();
            }
        } catch (MongoException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete account connected to " + email + " due to: " + e.getMessage()).build();
        }
    }

    private Response validateToken(String token) {
        Response response = securityService.checkJwt(token);
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            return response; 
        }
        return null; 
    }
}
