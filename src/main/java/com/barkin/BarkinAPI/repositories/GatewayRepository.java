package com.barkin.BarkinAPI.repositories;

import com.barkin.BarkinAPI.entities.Gateway;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GatewayRepository extends JpaRepository<Gateway, UUID> {
    public Page<Gateway> findAll(Pageable page);
}
