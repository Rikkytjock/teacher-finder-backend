package models;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public class Program {
    
    private String programId;
    private String programName;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private LocalDate startDate;
    private List<Session> sessions;
    private String description;
    private String registrationUrl;
    private String imageUrl;

    public Program() {
        this.programId = UUID.randomUUID().toString(); 
    }

    public Program(String programName, String city, String address, String postalCode, String country, LocalDate startDate,
                   List<Session> sessions, String description, String registrationUrl, String imageUrl) {
        this.programId = UUID.randomUUID().toString();  
        this.programName = programName;
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.country = country;
        this.startDate = startDate;
        this.sessions = sessions;
        this.description = description;
        this.registrationUrl = registrationUrl;
        this.imageUrl = imageUrl;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public List<Session> getSessions() {
        return sessions;
    }

    public void setSessions(List<Session> sessions) {
        this.sessions = sessions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRegistrationUrl() {
        return registrationUrl;
    }

    public void setRegistrationUrl(String registrationUrl) {
        this.registrationUrl = registrationUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
