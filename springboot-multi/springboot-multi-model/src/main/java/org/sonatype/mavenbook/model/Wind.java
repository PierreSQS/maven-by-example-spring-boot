package org.sonatype.mavenbook.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Wind {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String chill;
    private String direction;
    private String speed;

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="weather_id", nullable=false)
    private Weather weather;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getChill() { return chill; }
    public void setChill(String newChill) {
	this.chill = newChill;
    }

    public String getDirection() { return direction; }
    public void setDirection(final String newDirection) {
	this.direction = newDirection;
    }

    public String getSpeed() { return speed; }
    public void setSpeed(final String newSpeed) {
	this.speed = newSpeed;
    }
    
    public Weather getWeather() { return weather; }
    public void setWeather(Weather weather) { this.weather = weather; }

}