package services;

import java.util.ArrayList;

import org.bson.Document;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@Transactional(Transactional.TxType.SUPPORTS)
@ApplicationScoped
public class AdminService {

    @Inject
    MongoClient mongoClient;

    @Inject
    MongoDBService mongoDBService;

    public Response getAllTeachers() {

        MongoCollection<Document> collection = mongoDBService.getTeacherCollection();
        return Response.ok(collection.find().into(new ArrayList<>())).build();

    }


    
}
