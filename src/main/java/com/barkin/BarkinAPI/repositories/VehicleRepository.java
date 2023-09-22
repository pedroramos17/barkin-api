package com.barkin.BarkinAPI.repositories;

import com.barkin.BarkinAPI.entities.Vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    public Page<Vehicle> findAll(Pageable page);
}
