package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Program {
    
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private LocalDate date;
    private LocalTime start;
    private LocalTime finish;
    private String description;
    private String registrationUrl;
    private String imageUrl;

    public Program() {}

    public Program(String city, String address, String postalCode, String country, LocalDate date,
            LocalTime start, LocalTime finish, String description, String registrationUrl, String imageUrl) {
        
        this.city = city;
        this.address = address;
        this.postalCode = postalCode;
        this.country = country;
        this.date = date;
        this.start = start;
        this.finish = finish;
        this.description = description;
        this.registrationUrl = registrationUrl;
        this.imageUrl = imageUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getRegistrationUrl() {
        return registrationUrl;
    }

    public void setRegistrationUrl(String registrationUrl) {
        this.registrationUrl = registrationUrl;
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

    public LocalTime getStart() {
        return start;
    }

    public void setStart(LocalTime start) {
        this.start = start;
    }

    public LocalTime getFinish() {
        return finish;
    }

    public void setFinish(LocalTime finish) {
        this.finish = finish;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
