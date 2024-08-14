package models;

import java.time.LocalDateTime;
import java.util.UUID;  

public class Session {
    
    private String sessionId;
    private LocalDateTime start;
    private LocalDateTime finish;

    public Session() {
        this.sessionId = UUID.randomUUID().toString();
    }

    public Session(String sessionId, LocalDateTime start, LocalDateTime finish) {
        this.sessionId = sessionId != null ? sessionId : UUID.randomUUID().toString(); 
        this.start = start;
        this.finish = finish;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getFinish() {
        return finish;
    }

    public void setFinish(LocalDateTime finish) {
        this.finish = finish;
    }
}
