package org.sonatype.mavenbook.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@NamedQuery(name = "Weather.findPlacesWithID", query = "from Weather w where w.id = :id")
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Location location;

    @OneToOne(mappedBy = "weather", cascade = CascadeType.ALL)
    private Condition condition;

    @OneToOne(mappedBy = "weather", cascade = CascadeType.ALL)
    private Wind wind;

    @OneToOne(mappedBy = "weather", cascade = CascadeType.ALL)
    private Atmosphere atmosphere;

    private Date date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition newCondition) {
        this.condition = newCondition;
    }

    public Wind getWind() {
        return wind;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public Atmosphere getAtmosphere() {
        return atmosphere;
    }

    public void setAtmosphere(Atmosphere atmosphere) {
        this.atmosphere = atmosphere;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
