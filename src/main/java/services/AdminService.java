package services;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

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

    public Response getAllTeachers() {

        MongoCollection<Document> collection = mongoDBService.getTeacherCollection();
        return Response.ok(collection.find().into(new ArrayList<>())).build();

    }

    public Response changeVerificationStatus(String email) {
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

    public Response deleteTeacherAccount(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteTeacherAccount'");
    }
    


    
}
