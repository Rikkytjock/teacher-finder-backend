package services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import models.Program;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class ProgramService {

    public Response submitProgram(String token, Program program) {
        return null;
    }
    
}
