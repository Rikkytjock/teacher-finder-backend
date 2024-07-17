package services;

import java.util.List;
import org.bson.Document;
import org.bson.types.ObjectId;

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
        if (teacherDocument != null) {
            teacherDocument.put("_id", teacherDocument.get("_id").toString());
            @SuppressWarnings("unchecked")
            List<Document> announcements = (List<Document>) teacherDocument.get("announcements");
            if (announcements != null) {
                for (Document announcement : announcements) {
                    ObjectId announcementId = announcement.getObjectId("_id");
                    announcement.put("_id", announcementId != null ? announcementId.toHexString() : null);
                    ObjectId teacherDocumentId = announcement.getObjectId("teacherDocumentId");
                    announcement.put("teacherDocumentId", teacherDocumentId != null ? teacherDocumentId.toHexString() : null);
                }
            }
            return Response.ok(teacherDocument).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found.").build();
        }
    }
    
}

