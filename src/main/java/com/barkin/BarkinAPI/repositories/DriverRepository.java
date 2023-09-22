package com.barkin.BarkinAPI.repositories;

import com.barkin.BarkinAPI.entities.Driver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DriverRepository extends JpaRepository<Driver, UUID> {
    public Page<Driver> findAll(Pageable page);
}
