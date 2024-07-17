package services;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class TeacherService {

    @Inject
    MongoClient mongoClient;

    @Inject
    MongoDBService mongoDBService;

    public Response findteacher() {
        
        Document query = new Document("name", "Rikard Mohlin");
        Document teacherDocument = mongoDBService.getTeacherCollection().find(query).first();
        return Response.ok(teacherDocument).build();
    }
    
}

