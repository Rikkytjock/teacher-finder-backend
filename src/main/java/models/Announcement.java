package models;

import java.time.LocalDate;
import java.time.LocalTime;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public class Announcement {
    
    @BsonId
    private String id;
    private String city;
    private LocalDate date;
    private LocalTime time;
    private String description;
    private ObjectId teacherId;

    public Announcement(String id, String city, LocalDate date, LocalTime time, String description,
            ObjectId teacherId) {
        this.id = id;
        this.city = city;
        this.date = date;
        this.time = time;
        this.description = description;
        this.teacherId = teacherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectId getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(ObjectId teacherId) {
        this.teacherId = teacherId;
    }

    

    
    

}
