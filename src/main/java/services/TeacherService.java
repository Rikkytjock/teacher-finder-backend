package services;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import models.JwtResponse;
import models.Teacher;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class TeacherService {

    @Inject
    MongoClient mongoClient;

    @Inject
    MongoDBService mongoDBService;

    @Inject
    SecurityService securityService;

    public Response findTeacher(String token) {

        Response validateJwt = securityService.checkJwt(token);
        if (validateJwt.getStatus() != Response.Status.OK.getStatusCode()) {
            return validateJwt;
        }

        JwtResponse jwtResponse = (JwtResponse) validateJwt.getEntity();
        String email = jwtResponse.getSubject();

        Document teacherDocument = mongoDBService.getTeacherCollection().find(new Document("email", email)).first();
        if (teacherDocument != null) {
            teacherDocument.remove("password");
            teacherDocument.put("_id", teacherDocument.get("_id").toString());
            return Response.ok(teacherDocument).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found.").build();
        }
    }

    public Response createAccount(Teacher teacher) {

        String hashedPassword = BCrypt.hashpw(teacher.getPassword(), BCrypt.gensalt());
        teacher.setPassword(hashedPassword);

        try {
            Document teacherDocument = toDocument(teacher);
            MongoCollection<Document> teacherCollection = mongoDBService.getTeacherCollection();
            teacherCollection.insertOne(teacherDocument);
            return Response.status(Response.Status.CREATED).entity("Teacher account created!").build();
        } catch (MongoWriteException e) {
        if (e.getCode() == 11000) {
            return Response.status(Response.Status.CONFLICT).entity("Email already exists.").build();
        }
        throw e;
        }
    }

    private Document toDocument(Teacher teacher) {
        return new Document()
            .append("email", teacher.getEmail())
            .append("password", teacher.getPassword())
            .append("firstName", teacher.getFirstName())
            .append("lastName", teacher.getLastName())
            .append("teachingLanguages", teacher.getTeachingLanguages())
            .append("mobileNumber", teacher.getMobileNumber())
            .append("whatsAppNumber", teacher.getWhatsAppNumber())
            .append("country", teacher.getCountry())
            .append("city", teacher.getCity())
            .append("teachingLocationAddress", teacher.getTeachingLocationAddress())
            .append("websiteUrl", teacher.getWebsiteUrl())
            .append("profilePictureUrl", teacher.getProfilePictureUrl())
            .append("socialMediaUrls", teacher.getSocialMediaUrls())
            .append("programs", teacher.getPrograms())
            .append("role", "teacher")
            .append("accountVerified", false);
    }

    public Response editAccount(String token, Teacher teacher) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'editAccount'");
    }

    
}

