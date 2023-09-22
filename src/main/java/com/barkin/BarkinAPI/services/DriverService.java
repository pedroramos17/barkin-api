package com.barkin.BarkinAPI.services;

import com.barkin.BarkinAPI.entities.Driver;
import com.barkin.BarkinAPI.repositories.DriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DriverService {

    @Autowired
    DriverRepository driverRepository;

    public Driver insertDriver(Driver driver) {
        return driverRepository.save(driver);
    }
    public Driver updateDriver(UUID driverId, Driver updatedDriver) {
        Optional<Driver> driver = driverRepository.findById(driverId);

        if (driver.isPresent()) {
            Driver tempDriver = driver.get();
            tempDriver.setNameDriver(updatedDriver.getNameDriver());
            tempDriver.setRg(updatedDriver.getRg());
            tempDriver.setPhone(updatedDriver.getPhone());
            tempDriver.setVehicles(updatedDriver.getVehicles());
            tempDriver.setGatewayList(updatedDriver.getGatewayList());

            return driverRepository.save(tempDriver);
        }

        return null;
    }
    public  boolean deleteDriver(UUID driverId) {
        Optional<Driver> driver = driverRepository.findById(driverId);

        if(driver.isPresent()) {
            driverRepository.delete(driver.get());
            return true;
        }

        return false;
    }

    public Driver getDriverById(UUID driverId) {
        Optional<Driver> driver = driverRepository.findById(driverId);

        return driver.orElse(null);

    }

    public List<Driver> listDrivers(int page, int size) {
        return  driverRepository.findAll(PageRequest.of(page, size, Sort.by("id").ascending())).toList();
    }
}
