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
import models.JwtValidationResult;

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

        Response validateJwt = securityService.checkJwt(token);
        if (validateJwt.getStatus() != Response.Status.OK.getStatusCode()) {
            return validateJwt; 
        }

        MongoCollection<Document> collection = mongoDBService.getTeacherCollection();
        List<Document> teachers = collection.find().into(new ArrayList<>());
        if (teachers.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No teachers found in the database.").build();
        }

    return Response.ok(teachers).build();

    }

    public Response changeVerificationStatus(String email, String token) {

        Response validateJwt = securityService.checkJwt(token);
        if (validateJwt.getStatus() != Response.Status.OK.getStatusCode()) {
            return validateJwt; 
        }

        try {
            MongoCollection<Document> teacherCollection = mongoDBService.getTeacherCollection();
            Document teacherDocument = teacherCollection.find(new Document("email", email)).first(); 
            if (teacherDocument == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Teacher with email " + email + " not found.").build();
            }
            
            boolean newStatus = !teacherDocument.getBoolean("accountVerified");
            teacherCollection.updateOne(
                new Document("email", email),
                new Document("$set", new Document("accountVerified", newStatus))
            );
            
            return Response.ok().entity(email + " verification status successfully changed to " + newStatus + ".").build();
        } catch (MongoException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to change verification status due to: " + e.getMessage()).build();
        }
    }

    public Response deleteTeacherAccount(String email, String token) {

        Response validateJwt = securityService.checkJwt(token);
        if (validateJwt.getStatus() != Response.Status.OK.getStatusCode()) {
            return validateJwt; 
        }

        try {
            DeleteResult result = mongoDBService.getTeacherCollection().deleteOne(new Document("email", email));
            
            if (result.getDeletedCount() > 0) {
                return Response.ok("Account connected to " + email + " has been successfully deleted.").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("The database did not find and account using " + email + ".").build();
            }
        } catch (MongoException e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to delete account connected to " + email + " due to: " + e.getMessage()).build();
        }
    }
    


    
}
