package services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bson.Document;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import models.JwtValidationResult;
import models.Program;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class ProgramService {

    @Inject
    SecurityService securityService;

    @Inject 
    MongoDBService mongoDBService;

    public Response submitProgram(String token, Program program) {

        JwtValidationResult result = securityService.validateJwtAndGetTeacher(token);

        if (result.hasError()) {
            return result.getErrorResponse(); 
        }

        Document teacherDocument = result.getTeacherDocument();
        if (teacherDocument != null) {
            @SuppressWarnings("unchecked")
            List<Document> programsList = (List<Document>) teacherDocument.get("programs");

            Document programDocument = toDocument(program);

            if (programsList == null) {
                programsList = new ArrayList<>();
            }

            programsList.add(programDocument);

            mongoDBService.getTeacherCollection().updateOne(
                new Document("email", teacherDocument.get("email")),
                new Document("$set", new Document("programs", programsList))
            );

            return Response.ok().entity("Program submitted succesfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found.").build();
        }
    }

    private Document toDocument(Program program) {
        return new Document("name", program.getProgramName())
            .append("programId", UUID.randomUUID().toString())
            .append("address", program.getAddress())
            .append("city", program.getCity())
            .append("postalCode", program.getPostalCode())
            .append("country", program.getCountry())
            .append("startDate", program.getStartDate()) 
            .append("sessions", program.getSessions()) 
            .append("description", program.getDescription())
            .append("registrationUrl", program.getRegistrationUrl())
            .append("imageUrl", program.getImageUrl());
    }

    public Response editProgram(String token, Program updatedProgram) {

        JwtValidationResult result = securityService.validateJwtAndGetTeacher(token);

        if (result.hasError()) {
            return result.getErrorResponse(); 
        }

        Document teacherDocument = result.getTeacherDocument();
        if (teacherDocument != null) {
            @SuppressWarnings("unchecked")
            List<Document> programs = (List<Document>) teacherDocument.get("programs");
            boolean programFound = false;
            for (int i = 0; i < programs.size(); i++) {
                Document program = programs.get(i);
                if (updatedProgram.getProgramId().equals(program.getString("programId"))) {
                    programs.set(i, toDocument(updatedProgram));
                    programFound = true;
                    break;
                }
            }

            if (!programFound) {
                return Response.status(Response.Status.NOT_FOUND).entity("Program not found").build();
            }

            mongoDBService.getTeacherCollection().updateOne(
                new Document("email", teacherDocument.getString("email")),
                new Document("$set", new Document("programs", programs))
            );
            return Response.ok().entity("Program edited succesfully").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No account found.").build();
        }
    }
    
}
