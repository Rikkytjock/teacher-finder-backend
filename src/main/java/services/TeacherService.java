package services;

import org.bson.Document;
import org.mindrot.jbcrypt.BCrypt;

import com.mongodb.MongoWriteException;

import config.AppConfig;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import models.JwtValidationResult;
import models.Teacher;
import utilities.EmailUtility;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class TeacherService {


    @Inject
    private MongoDBService mongoDBService;

    @Inject
    private SecurityService securityService;

    @Inject
    private AppConfig appConfig;

    public Response getTeacher(String token) {
        JwtValidationResult result = securityService.validateJwtAndGetTeacher(token);

        if (result.hasError()) {
            return result.getErrorResponse(); 
        }

        Document teacherDocument = result.getTeacherDocument();
        if (teacherDocument == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found.").build();
        }

        teacherDocument.remove("password"); 
        teacherDocument.put("_id", teacherDocument.get("_id").toString()); 
        return Response.ok(teacherDocument).build();
    }

    public Response createAccount(Teacher teacher) {
        try {
            String hashedPassword = BCrypt.hashpw(teacher.getPassword(), BCrypt.gensalt());
            teacher.setPassword(hashedPassword);

            Document teacherDocument = toDocument(teacher);
            mongoDBService.getTeacherCollection().insertOne(teacherDocument);

            sendVerificationRequestEmail(teacher);

            return Response.status(Response.Status.CREATED)
                           .entity("Teacher account created! Please allow up to 48 hours for your teacher ID to be validated.")
                           .build();
        } catch (MongoWriteException e) {
            if (e.getCode() == 11000) { 
                return Response.status(Response.Status.CONFLICT).entity("Email already in use.").build();
            }
            throw e;
        }
    }

    private void sendVerificationRequestEmail(Teacher teacher) {
        EmailUtility emailUtility = new EmailUtility(
            appConfig.emailUsername(),
            appConfig.emailPassword(),
            appConfig.emailHost(),
            appConfig.emailPort()
            );

        try {
            String recipient = "d_hankin@outlook.com";
            String subject = "Teacher account verification requested!";
            String body = "Teacher account created using email: " + teacher.getEmail() + ".\n\nTeacher ID: " + teacher.getTeacherId() + "\n\nVerify teacher ID and login as admin to verify the teacher account.";
            emailUtility.sendEmail(recipient, subject, body);
        } catch (Exception e) {
            System.err.println("Failed to send verification email: " + e.getMessage());
        }
    }

    public Response editAccount(String token, Teacher teacher) {
        JwtValidationResult result = securityService.validateJwtAndGetTeacher(token);

        if (result.hasError()) {
            return result.getErrorResponse(); 
        }

        Document existingTeacherDocument = result.getTeacherDocument();
        if (existingTeacherDocument == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found.").build();
        }

        Document updatedTeacherDocument = toDocument(teacher)
                                            .append("accountVerified", existingTeacherDocument.get("accountVerified"));
        mongoDBService.getTeacherCollection()
                      .updateOne(new Document("email", existingTeacherDocument.getString("email")),
                                 new Document("$set", updatedTeacherDocument));

        return Response.ok("Teacher account updated successfully").build();
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
        

        teacherDocument.append("role", "teacher");
        teacherDocument.append("accountVerified", teacher.isAccountVerified());
        
        return teacherDocument;
    }
}
