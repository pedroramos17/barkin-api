package com.barkin.BarkinAPI.controllers;

import com.barkin.BarkinAPI.entities.Driver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import javax.sql.DataSource;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@SpringBootTest
//@ActiveProfiles("test")
//@AutoConfigureMockMvc
//@AutoConfigureWebClient
//@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:beforeTestRun.sql")
//@Sql(executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD, scripts = "classpath:afterTestRun.sql")
////@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
//public class DriverControllerTest {

//    @Autowired
//    MockMvc mockMvc;
//
//    @Autowired
//    DataSource dataSource;
//
//    Faker faker = new Faker();
//
//    @Test
//    void getListOfdriversWithConfigurablePageSize() throws Exception {
//        // First page with page size of 2
//        mockMvc.perform(get("/api/drivers?page=0&pageSize=2")).andExpect(status().isOk())
//                .andExpect(content().contentType("application/json")).andExpect(jsonPath("$._embedded.driverList[0].id").value("1"))
//                .andExpect(jsonPath("$._embedded.driverList[1].id").value("2"));
//
//        // Second page with page size of 3
//        mockMvc.perform(get("/api/drivers?page=1&pageSize=3")).andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$._embedded.driverList[0].id").value("4"))
//                .andExpect(jsonPath("$._embedded.driverList[1].id").value("5"));
//
//    }
//
//    @Test
//    void getSingleExistingdriverById() throws Exception {
//
//        // Get driver with id=2
//        mockMvc.perform(get("/api/drivers/2")).andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$.id").value("2"))
//                .andExpect(jsonPath("$.name").value("HP Inspiron"));
//
//    }
//
//
//    @Test
//    void getSingleNotExistingdriverById() throws Exception {
//
//        // Get driver with id=22
//        mockMvc.perform(get("/api/drivers/22"))
//                .andExpect(status().isNotFound());
//
//    }
//
//    @Test
//    void getSingledriverByInvaliddriverId() throws Exception {
//
//        // Get driver with id=i
//        mockMvc.perform(get("/api/drivers/i"))
//                .andExpect(status().isBadRequest());
//
//        // Get driver with id=*
//        mockMvc.perform(get("/api/drivers/*"))
//                .andExpect(status().isBadRequest());
//
//    }
//
//    @Test
//    void updatedriverWithExistingdriverId() throws Exception{
//        Driver driver = new Driver(UUID.randomUUID(), faker.name().fullName(), faker.idNumber().ssnValid(), faker.phoneNumber().cellPhone());
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(driver);
//        // Update driver with id = 2
//        mockMvc.perform(patch("/api/drivers/2").content(json).characterEncoding("utf-8")
//                        .contentType("application/json"))
//                .andExpect(status().isAccepted())
//                .andExpect(jsonPath("$.name").value("HP FitBook"))
//                .andExpect(jsonPath("$.currentPrice").value(305.99));
//    }
//
//
//    @Test
//    void updatedriverWithNotExistingdriverId() throws Exception{
//
//        Driver driver = new Driver(77,"HP FitBook",305.99);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(driver);
//        // Update driver with id = 77 which is not existed
//        mockMvc.perform(patch("/api/drivers/77").content(json).characterEncoding("utf-8")
//                        .contentType("application/json"))
//                .andExpect(status().isNotFound());
//
//    }
//
//
//    @Test
//    void createdriver() throws Exception{
//
//        Driver driver = new Driver("HP FitBook",305.99);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(driver);
//
//        // Create new driver
//        mockMvc.perform(post("/api/drivers").content(json).characterEncoding("utf-8")
//                        .contentType("application/json"))
//                .andExpect(status().isCreated());
//
//    }
//
//    @Test
//    void createDriverWithDuplicateName() throws Exception{
//
//        Driver driver = new Driver("Dell B3",305.99);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(driver);
//
//        // Create new driver with duplicate name
//        mockMvc.perform(post("/api/drivers").content(json).characterEncoding("utf-8")
//                        .contentType("application/json"))
//                .andExpect(status().isConflict());
//
//    }
//
//    @Test
//    void deleteDriverWithExistingDriverId() throws Exception{
//
//        // Delete driver with id = 2
//        mockMvc.perform(delete("/api/drivers/2"))
//                .andExpect(status().isAccepted());
//    }
//}
