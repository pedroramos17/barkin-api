package com.barkin.BarkinAPI.services;

import com.barkin.BarkinAPI.entities.Vehicle;
import com.barkin.BarkinAPI.repositories.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class VehicleService {

    @Autowired
    VehicleRepository vehicleRepository;

    public Vehicle insertVehicle(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }
    public Vehicle updateVehicle(UUID vehicleId, Vehicle updatedVehicle) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);

        if (vehicle.isPresent()) {
            Vehicle tempVehicle = vehicle.get();
            tempVehicle.setBrand(updatedVehicle.getBrand());
            tempVehicle.setColor(updatedVehicle.getColor());
            tempVehicle.setPlate(updatedVehicle.getPlate());
            tempVehicle.setObservation(updatedVehicle.getObservation());
            tempVehicle.setDriver(updatedVehicle.getDriver());

            return vehicleRepository.save(tempVehicle);
        }

        return null;
    }
    public  boolean deleteVehicle(UUID vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);

        if(vehicle.isPresent()) {
            vehicleRepository.delete(vehicle.get());
            return true;
        }

        return false;
    }

    public Vehicle getVehicleById(UUID vehicleId) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(vehicleId);

        return vehicle.orElse(null);

    }

    public List<Vehicle> listVehicles(int page, int size) {
        return  vehicleRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending())).toList();
    }
}
