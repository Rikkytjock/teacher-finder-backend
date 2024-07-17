package services;

import org.bson.Document;

import com.mongodb.client.MongoClient;

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

    public Response findTeacher() {
        
        Document teacherDocument = mongoDBService.getTeacherCollection().find(new Document("name", "Rikard Mohlin")).first();
        return teacherDocument != null ? Response.ok(teacherDocument).build() : Response.status(Response.Status.NOT_FOUND).entity("No account found.").build();
    }
    
}

