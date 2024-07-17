package models;

import org.bson.codecs.pojo.annotations.BsonId;

import jakarta.validation.constraints.Email;

public class Teacher {
    
    @BsonId
    private String id;
    @Email
    private String email;
    private String password;
    private String name;
    private String teacherId;

    public Teacher(String id, @Email String email, String password, String name, String teacherId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.teacherId = teacherId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    

    
}
