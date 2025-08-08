package cz.uhk.grainweight.model;

import jakarta.persistence.*;

@Entity
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String driverName;
    private int tareWeight;
    private String licencePlate;
    private String contact;

    public Driver(long l, String janNovak, double v, Object o) {
    }

    public Driver() {

    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getTareWeight() {
        return tareWeight;
    }

    public void setTareWeight(int tareWeight) {
        this.tareWeight = tareWeight;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

}
