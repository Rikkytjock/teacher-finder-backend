package models;

import org.bson.Document;

import jakarta.ws.rs.core.Response;

public class JwtValidationResult {
    private Response errorResponse;
    private Document teacherDocument;

    public JwtValidationResult(Response errorResponse, Document teacherDocument) {
        this.errorResponse = errorResponse;
        this.teacherDocument = teacherDocument;
    }

    public boolean hasError() {
        return errorResponse != null;
    }

    public Response getErrorResponse() {
        return errorResponse;
    }

    public Document getTeacherDocument() {
        return teacherDocument;
    }
}

