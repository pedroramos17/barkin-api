package com.barkin.BarkinAPI.configs;

import com.barkin.BarkinAPI.entities.Driver;
import com.barkin.BarkinAPI.entities.Vehicle;
import com.barkin.BarkinAPI.repositories.DriverRepository;
import com.barkin.BarkinAPI.repositories.VehicleRepository;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.UUID;

//@Configuration
//@Profile("!test")
//public class InitDBRecords {
//
//        private static final Logger log = LoggerFactory.getLogger(InitDBRecords.class);
//
//        Faker faker = new Faker();
//
//        @Bean
//        CommandLineRunner initDatabase(DriverRepository driverRepository, VehicleRepository vehicleRepository) {
//            if(driverRepository.findAll().isEmpty() && vehicleRepository.findAll().isEmpty()) {
//                return args -> {
//                    try {
//                        // insert initial records into DB
//                        int x = 0;
//                        while (x < 6) {
//                            driverRepository.save(new Driver(UUID.randomUUID(), faker.name().fullName(), faker.idNumber().ssnValid(), faker.phoneNumber().cellPhone()));
//                            x++;
//                        }
//
//                        vehicleRepository.save(new Vehicle(UUID.randomUUID(), "Ford", faker.color().name(), "BRA8FH12", "Any observation"));
//                        vehicleRepository.save(new Vehicle(UUID.randomUUID(), "Honda", faker.color().name(), "BST8PS12", "Any observation"));
//                        vehicleRepository.save(new Vehicle(UUID.randomUUID(), "VW", faker.color().name(), "PRD7FG35", "Any observation"));
//                        vehicleRepository.save(new Vehicle(UUID.randomUUID(), "Fiat", faker.color().name(), "YTA3JW11", "Any observation"));
//                        vehicleRepository.save(new Vehicle(UUID.randomUUID(), "Mercedes-benz", faker.color().name(), "FNP8BV34", "Any observation"));
//                        vehicleRepository.save(new Vehicle(UUID.randomUUID(), "Lexus", faker.color().name(), "GAR6GH22", "Any observation"));
//
//
//                        log.info("Initial records inserted into database." );
//                    } catch (Exception e) {
//                        log.info("Error in initiating records into database." );
//                    }
//                };
//            }
//            else
//                return args -> {
//                    log.info("Records already initiated ");
//                };
//        }
//}
