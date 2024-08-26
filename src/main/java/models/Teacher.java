package models;

import java.util.List;

import org.bson.codecs.pojo.annotations.BsonId;
import jakarta.validation.constraints.Email;

public class Teacher {
    
    @BsonId
    private String id;
    @Email
    private String email;
    private String password;
    private String teacherId;
    private String firstName;
    private String lastName;
    private List<String> teachingLanguages;
    private String mobileNumber;
    private String whatsAppNumber;
    private String country;
    private String city;
    private String teachingLocationAddress;
    private String websiteUrl;
    private String profilePictureUrl;
    private List<String> socialMediaUrls;
    private List<Program> programs;
    private boolean accountVerified;
    private String role;

    public Teacher() {}

    public Teacher(String id, @Email String email, String password, String teacherId, String firstName, String lastName,
            List<String> teachingLanguages, String mobileNumber, String whatsAppNumber, String country, String city,
            String teachingLocationAddress, String websiteUrl, String profilePictureUrl, List<String> socialMediaUrls, List<Program> programs,
            boolean accountVerified, String role) {

        this.id = id;
        this.email = email;
        this.password = password;
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.teachingLanguages = teachingLanguages;
        this.mobileNumber = mobileNumber;
        this.whatsAppNumber = whatsAppNumber;
        this.country = country;
        this.city = city;
        this.teachingLocationAddress = teachingLocationAddress;
        this.websiteUrl = websiteUrl;
        this.profilePictureUrl = profilePictureUrl;
        this.socialMediaUrls = socialMediaUrls;
        this.programs = programs;
        this.accountVerified = accountVerified;
        this.role = role;
    }
    
    public String getFirstName() {
        return firstName;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public List<String> getTeachingLanguages() {
        return teachingLanguages;
    }


    public void setTeachingLanguages(List<String> teachingLanguages) {
        this.teachingLanguages = teachingLanguages;
    }


    public String getMobileNumber() {
        return mobileNumber;
    }


    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }


    public String getWhatsAppNumber() {
        return whatsAppNumber;
    }


    public void setWhatsAppNumber(String whatsAppNumber) {
        this.whatsAppNumber = whatsAppNumber;
    }


    public String getCountry() {
        return country;
    }


    public void setCountry(String country) {
        this.country = country;
    }


    public String getCity() {
        return city;
    }


    public void setCity(String city) {
        this.city = city;
    }


    public String getTeachingLocationAddress() {
        return teachingLocationAddress;
    }


    public void setTeachingLocationAddress(String teachingLocationAddress) {
        this.teachingLocationAddress = teachingLocationAddress;
    }


    public String getWebsiteUrl() {
        return websiteUrl;
    }


    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }


    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }


    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }


    public List<String> getSocialMediaUrls() {
        return socialMediaUrls;
    }


    public void setSocialMediaUrls(List<String> socialMediaUrls) {
        this.socialMediaUrls = socialMediaUrls;
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

    public List<Program> getPrograms() {
        return programs;
    }

    public void setPrograms(List<Program> programs) {
        this.programs = programs;
    }

    public boolean isAccountVerified() {
        return accountVerified;
    }

    public void setAccountVerified(boolean accountVerified) {
        this.accountVerified = accountVerified;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
