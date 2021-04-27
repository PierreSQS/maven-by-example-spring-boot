package org.sonatype.mavenbook.model;

import javax.persistence.*;

@Entity
public class Location {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private String id;

    private String city;
    private String region;
    private String country;
    private String woeid;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getRegion() {	return region; }
    public void setRegion(String region) { this.region = region; }
    
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getWoeid() {
        return woeid;
    }

    public void setWoeid(String woeid) {
        this.woeid = woeid;
    }
}