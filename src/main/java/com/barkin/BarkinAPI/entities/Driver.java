package com.barkin.BarkinAPI.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.hateoas.RepresentationModel;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
public class Driver extends RepresentationModel<Driver> {
    private UUID id;
    private String nameDriver;
    private String rg;
    private String phone;
    private Timestamp lastUpdate;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    private List<Vehicle> vehicles;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "driver")
    private List<Gateway> gatewayList;

    public Driver(){}

    public Driver(UUID id, String nameDriver, String rg, String phone) {
        this.id = id;
        this.nameDriver = nameDriver;
        this.rg = rg;
        this.phone = phone;
    }

    public Driver(String nameDriver, String rg, String phone) {
        this.nameDriver = nameDriver;
        this.rg = rg;
        this.phone = phone;
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
    @Column(name = "nameDriver")
    public String getNameDriver() {
        return nameDriver;
    }

    public void setNameDriver(String nameDriver) {
        this.nameDriver = nameDriver;
    }

    @Basic
    @Column(name = "rg", unique = true)
    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    @Basic
    @Column(name = "phone", unique = true)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Gateway> getGatewayList() {
        return gatewayList;
    }

    public void setGatewayList(List<Gateway> gatewayList) {
        this.gatewayList = gatewayList;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "id=" + id +
                ", nameDriver='" + nameDriver + '\'' +
                ", rg='" + rg + '\'' +
                ", phone='" + phone + '\'' +
                ", lastUpdate=" + lastUpdate +
                ", vehicles=" + vehicles +
                ", gatewayList=" + gatewayList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Driver driver)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getNameDriver(), driver.getNameDriver()) && Objects.equals(getVehicles(), driver.getVehicles()) && Objects.equals(getGatewayList(), driver.getGatewayList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getNameDriver(), getVehicles(), getGatewayList());
    }
}
