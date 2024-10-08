package services;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.bson.Document;

@ApplicationScoped
public class MongoDBService {
    @Inject
    MongoClient mongoClient;

    public MongoCollection<Document> getTeacherCollection() {
        MongoDatabase database = mongoClient.getDatabase("teacherFinder");
        return database.getCollection("teachers");
    }

    public MongoCollection<Document> getAdminCollection() {
        MongoDatabase database = mongoClient.getDatabase("teacherFinder");
        return database.getCollection("admin");
    }
}

