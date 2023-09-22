package com.barkin.BarkinAPI.controllers;

import com.barkin.BarkinAPI.entities.Driver;
import com.barkin.BarkinAPI.services.DriverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.UUID;

@RestController
@ExposesResourceFor(Driver.class)
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    private DriverService driverService;

    // GET /api/drivers (get a list of drivers)
    @Operation(summary = "Get a list of drivers (Ordened by id in ascending order), you must specify page number and page size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    @GetMapping(produces = { "application/json" })
    public ResponseEntity<CollectionModel<Driver>> getDrivers(@Parameter(description = "Page index (start from 0)") @RequestParam(value = "page") Integer page, @Parameter(description = "Number of records per page") @RequestParam(value = "pageSize") Integer pageSize) {
        List<Driver> driverList = driverService.listDrivers(page, pageSize);

        for (Driver driver : driverList) {
            Link recordSelfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DriverController.class)
                    .driverService.getDriverById(driver.getId())).withSelfRel();

            driver.add(recordSelfLink);
        }

        CollectionModel<Driver> resources = CollectionModel.of(driverList);

        Link selfLink = entityLinks.linkToCollectionResource(Driver.class);
        resources.add(selfLink);

        return new ResponseEntity(EntityModel.of(resources), HttpStatus.OK);
    }
    
    // GET /api/drivers/1 (get one driver from the list)
    @Operation(summary = "Get a driver by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the driver",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Driver.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "driver not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content) })
    @GetMapping(value = "/{driverId}", produces = { "application/json" })
    public ResponseEntity<EntityModel<Driver>> getDriverById(@Parameter(description = "id of driver to be retrieved")  @PathVariable UUID driverId){

        // selfLink to api according to HATEOS
        Link selfLink = entityLinks.linkToItemResource(Driver.class, driverId);
        // Retrieve requested driver from database
        Driver driver = driverService.getDriverById(driverId);
        //Check whether the driver exist or not
        if(driver!=null) {
            EntityModel<Driver> resource = EntityModel.of(driver);
            resource.add(selfLink);
            // Send data to client as a response
            return new ResponseEntity(EntityModel.of(resource),HttpStatus.OK);
        }

        // If no driver is found send 404 status code as response
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }


    // POST /api/drivers (create a driver)
    @Operation(summary = "Create a new driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "driver created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Driver.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "driver not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate driver name",
                    content = @Content) })
    @PostMapping
    public ResponseEntity<EntityModel<Driver>> createDriver(@RequestBody Driver driver){

        // Create and Save driver in database
        Driver storeddriver = driverService.insertDriver(driver);

        // Check whether the driver is saved or not
        if(driver!=null) {
            // selfLink to api that retrieves the driver according to HATEOS
            Link recordSelfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DriverController.class)
                    .getDriverById(driver.getId())).withSelfRel();
            driver.add(recordSelfLink);
            // Send created driver with 201 status code to client
            return new ResponseEntity(EntityModel.of(storeddriver), HttpStatus.CREATED);
        }
        // If no driver is saved send 304 status code
        return new ResponseEntity(null, HttpStatus.NOT_MODIFIED);
    }


    //PATCH /api/drivers/1 (update the price of a single driver)
    @Operation(summary = "Update a driver by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "driver updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Driver.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "driver not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate driver name",
                    content = @Content) })
    @PatchMapping(value = "/{driverId}", produces = { "application/json" })
    public ResponseEntity<EntityModel<Driver>> updateDriver(@Parameter(description = "id of driver to be updated")  @PathVariable UUID driverId, @Parameter(description = "driver updated information")  @RequestBody Driver driver){

        // Update driver in the database
        Driver storeddriver = driverService.updateDriver(driverId ,driver);

        // Check whether the driver exist and is updated or not
        if(storeddriver!=null) {
            // selfLink to api that retrieves the driver according to HATEOS
            Link recordSelfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(DriverController.class)
                    .getDriverById(driver.getId())).withSelfRel();
            driver.add(recordSelfLink);
            // Send updated driver with 202 status code to client
            return new ResponseEntity(EntityModel.of(storeddriver), HttpStatus.ACCEPTED);
        }

        // If no driver is found send 404 status code as response
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    // DELETE/api/drivers/1 (delete a single driver)
    @Operation(summary = "Delete a driver by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "driver deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "driver not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content) })
    @DeleteMapping(value = "/{driverId}")
    public ResponseEntity<EntityModel<Driver>> deleteDriver(@Parameter(description = "id of driver to be deleted")  @PathVariable UUID driverId){

        // Delete the driver by its id and check for the result
        if(driverService.deleteDriver(driverId))
            // Sent empty response with 202 status code
            return new ResponseEntity(null, HttpStatus.ACCEPTED);
        else
            // If no driver is found send 404 status code as response
            return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }




    // Handling exceptions related to user input
    @ExceptionHandler({ MissingServletRequestParameterException.class, InvalidParameterException.class, MethodArgumentTypeMismatchException.class })
    public ResponseEntity<String> handleUserInputException() {

        return new ResponseEntity("Bad Request", HttpStatus.BAD_REQUEST);
    }

    // Handling runtime exception
    @ExceptionHandler({ RuntimeException.class })
    public ResponseEntity<String> handleRuntimeException() {

        return new ResponseEntity(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }



    @ExceptionHandler({ DataIntegrityViolationException.class })
    public ResponseEntity<String> handleDuplicateException() {

        return new ResponseEntity(null, HttpStatus.CONFLICT);
    }
}
