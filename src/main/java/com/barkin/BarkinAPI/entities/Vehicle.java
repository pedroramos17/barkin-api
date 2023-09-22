package com.barkin.BarkinAPI.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Vehicle extends RepresentationModel<Vehicle> {
    private UUID id;
    private String brand;
    private String color;
    private String plate;
    private String observation;
    private Timestamp lastUpdate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private Driver driver;

    public Vehicle() {}
    public Vehicle(UUID id, String brand, String color, String plate, String observation) {
        this.id = id;
        this.brand = brand;
        this.color = color;
        this.plate = plate;
        this.observation = observation;
    }

    public Vehicle(String brand, String color, String plate, String observation) {
        this.brand = brand;
        this.color = color;
        this.plate = plate;
        this.observation = observation;
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Basic
    @Column(name = "brand")
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Basic
    @Column(name = "color")
    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Basic
    @Column(name = "plate", unique = true)
    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    @Basic
    @Column(name = "observation")
    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }


    @Basic
    @Column(name = "lastUpdate")
    @UpdateTimestamp
    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", color='" + color + '\'' +
                ", plate='" + plate + '\'' +
                ", observation='" + observation + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", driver=" + driver +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle vehicle)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getBrand(), vehicle.getBrand()) && Objects.equals(getColor(), vehicle.getColor()) && Objects.equals(getDriver(), vehicle.getDriver());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getBrand(), getColor(), getDriver());
    }
}
