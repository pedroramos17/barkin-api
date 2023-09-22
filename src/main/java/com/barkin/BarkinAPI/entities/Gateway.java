package com.barkin.BarkinAPI.entities;

import jakarta.persistence.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Gateway extends RepresentationModel<Gateway> {
    private UUID id;
    private LocalDateTime inputDate;
    private LocalDateTime outputDate;
    private boolean permanence;
    @ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY, mappedBy = "gateway")
    @JoinTable(name = "gateway",
            joinColumns = @JoinColumn(name = "driver_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "gateway_id", referencedColumnName = "id"))
    private Driver driver;

    public Gateway(UUID id, LocalDateTime inputDate, LocalDateTime outputDate, boolean permanence, Driver driver) {
        this.id = id;
        this.inputDate = inputDate;
        this.outputDate = outputDate;
        this.permanence = permanence;
        this.driver = driver;
    }

    public Gateway(LocalDateTime inputDate, LocalDateTime outputDate, boolean permanence) {
        this.inputDate = inputDate;
        this.outputDate = outputDate;
        this.permanence = permanence;
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
    @Column(name = "inputDate")
    public LocalDateTime getInputDate() {
        return inputDate;
    }

    public void setInputDate(LocalDateTime inputDate) {
        this.inputDate = inputDate;
    }

    @Basic
    @Column(name = "outputDate")
    public LocalDateTime getOutputDate() {
        return outputDate;
    }

    public void setOutputDate(LocalDateTime outputDate) {
        this.outputDate = outputDate;
    }

    @Basic
    @Column(name = "permanence")
    public boolean isPermanence() {
        return permanence;
    }

    public void setPermanence(boolean permanence) {
        this.permanence = permanence;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
