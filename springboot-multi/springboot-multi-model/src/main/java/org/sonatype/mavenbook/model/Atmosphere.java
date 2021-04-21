package org.sonatype.mavenbook.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Atmosphere {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    private String humidity;
    private String visibility;
    private String pressure;
    private String rising;
    
    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="weather_id", nullable=false)
    private Weather weather;

    protected Atmosphere() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getHumidity() {	return humidity; }
    public void setHumidity(final String newHumidity) {
	this.humidity = newHumidity;
    }

    public String getVisibility() { return visibility; }
    public void setVisibility(final String newVisibility) {
	this.visibility = newVisibility;
    }

    public String getPressure() { return pressure; }
    public void setPressure(final String newPressure) {
	this.pressure = newPressure;
    }

    public String getRising() { return rising; }
    public void setRising(final String newRising) {
	this.rising = newRising;
    }
    
    public Weather getWeather() { return weather; }
    public void setWeather(Weather weather) { this.weather = weather; }
    
}