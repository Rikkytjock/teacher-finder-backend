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
import models.JwtValidationResult;
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

        JwtValidationResult result = securityService.validateJwtAndGetTeacher(token);

        if (result.hasError()) {
            return result.getErrorResponse(); 
        }

        Document teacherDocument = result.getTeacherDocument();
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
            return Response.status(Response.Status.CREATED).entity("Teacher account created! Please allow up to 48 hours for your teacher ID to be validated.").build();
        } catch (MongoWriteException e) {
        if (e.getCode() == 11000) {
            return Response.status(Response.Status.CONFLICT).entity("Email already exists.").build();
        }
        throw e;
        }
    }

    public Response editAccount(String token, Teacher teacher) {

        JwtValidationResult result = securityService.validateJwtAndGetTeacher(token);

        if (result.hasError()) {
            return result.getErrorResponse(); 
        }

        Document teacherDocument = result.getTeacherDocument();
        if (teacherDocument != null) {
            String currentEmail = teacherDocument.get("email").toString();
            Document updatedTeacherDocument = toDocument(teacher);
            updatedTeacherDocument.append("accountVerified", teacherDocument.get("accountVerified"));
            MongoCollection<Document> teacherCollection = mongoDBService.getTeacherCollection();
            teacherCollection.updateOne(
            new Document("email", currentEmail),  
            new Document("$set", updatedTeacherDocument));

            return Response.ok().entity("Teacher account updated successfully").build();

        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found.").build();
        }
    }
    
    private Document toDocument(Teacher teacher) {
        Document teacherDocument = new Document();

        if (teacher.getEmail() != null) {
            teacherDocument.append("email", teacher.getEmail());
        }
        if (teacher.getPassword() != null) {
            teacherDocument.append("password", teacher.getPassword());
        }
        if (teacher.getTeacherId() != null) {
            teacherDocument.append("teacherId", teacher.getTeacherId());
        }
        if (teacher.getFirstName() != null) {
            teacherDocument.append("firstName", teacher.getFirstName());
        }
        if (teacher.getLastName() != null) {
            teacherDocument.append("lastName", teacher.getLastName());
        }
        if (teacher.getTeachingLanguages() != null && !teacher.getTeachingLanguages().isEmpty()) {
            teacherDocument.append("teachingLanguages", teacher.getTeachingLanguages());
        }
        if (teacher.getMobileNumber() != null) {
            teacherDocument.append("mobileNumber", teacher.getMobileNumber());
        }
        if (teacher.getWhatsAppNumber() != null) {
            teacherDocument.append("whatsAppNumber", teacher.getWhatsAppNumber());
        }
        if (teacher.getCountry() != null) {
            teacherDocument.append("country", teacher.getCountry());
        }
        if (teacher.getCity() != null) {
            teacherDocument.append("city", teacher.getCity());
        }
        if (teacher.getTeachingLocationAddress() != null) {
            teacherDocument.append("teachingLocationAddress", teacher.getTeachingLocationAddress());
        }
        if (teacher.getWebsiteUrl() != null) {
            teacherDocument.append("websiteUrl", teacher.getWebsiteUrl());
        }
        if (teacher.getProfilePictureUrl() != null) {
            teacherDocument.append("profilePictureUrl", teacher.getProfilePictureUrl());
        }
        if (teacher.getSocialMediaUrls() != null && !teacher.getSocialMediaUrls().isEmpty()) {
            teacherDocument.append("socialMediaUrls", teacher.getSocialMediaUrls());
        }
        if (teacher.getPrograms() != null && !teacher.getPrograms().isEmpty()) {
            teacherDocument.append("programs", teacher.getPrograms());
        }
        
        return teacherDocument;
    }
    
}

