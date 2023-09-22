package com.barkin.BarkinAPI.controllers;

import com.barkin.BarkinAPI.entities.Vehicle;
import com.barkin.BarkinAPI.services.VehicleService;
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
@ExposesResourceFor(Vehicle.class)
@RequestMapping("/api/vehicles")
public class VehicleController {

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    private VehicleService vehicleService;

    // GET /api/vehicles (get a list of vehicles)
    @Operation(summary = "Get a list of vehicles (Ordened by id in ascending order), you must specify page number and page size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    @GetMapping(produces = { "application/json" })
    public ResponseEntity<CollectionModel<Vehicle>> getVehicles(@Parameter(description = "Page index (start from 0)") @RequestParam(value = "page") Integer page, @Parameter(description = "Number of records per page") @RequestParam(value = "pageSize") Integer pageSize) {
        List<Vehicle> vehicleList = vehicleService.listVehicles(page, pageSize);

        for (Vehicle vehicle : vehicleList) {
            Link recordSelfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VehicleController.class)
                    .vehicleService.getVehicleById(vehicle.getId())).withSelfRel();

            vehicle.add(recordSelfLink);
        }

        CollectionModel<Vehicle> resources = CollectionModel.of(vehicleList);

        Link selfLink = entityLinks.linkToCollectionResource(Vehicle.class);
        resources.add(selfLink);

        return new ResponseEntity(EntityModel.of(resources), HttpStatus.OK);
    }

    // GET /api/vehicles/1 (get one vehicle from the list)
    @Operation(summary = "Get a vehicle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the vehicle",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vehicle.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "vehicle not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content) })
    @GetMapping(value = "/{vehicleId}", produces = { "application/json" })
    public ResponseEntity<EntityModel<Vehicle>> getVehicleById(@Parameter(description = "id of vehicle to be retrieved")  @PathVariable UUID vehicleId){

        // selfLink to api according to HATEOS
        Link selfLink = entityLinks.linkToItemResource(Vehicle.class, vehicleId);
        // Retrieve requested vehicle from database
        Vehicle vehicle = vehicleService.getVehicleById(vehicleId);
        //Check whether the vehicle exist or not
        if(vehicle!=null) {
            EntityModel<Vehicle> resource = EntityModel.of(vehicle);
            resource.add(selfLink);
            // Send data to client as a response
            return new ResponseEntity(EntityModel.of(resource),HttpStatus.OK);
        }

        // If no vehicle is found send 404 status code as response
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }


    // POST /api/vehicles (create a vehicle)
    @Operation(summary = "Create a new vehicle")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "vehicle created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vehicle.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "vehicle not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate vehicle name",
                    content = @Content) })
    @PostMapping
    public ResponseEntity<EntityModel<Vehicle>> createVehicle(@RequestBody Vehicle vehicle){

        // Create and Save vehicle in database
        Vehicle storedvehicle = vehicleService.insertVehicle(vehicle);

        // Check whether the vehicle is saved or not
        if(vehicle!=null) {
            // selfLink to api that retrieves the vehicle according to HATEOS
            Link recordSelfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VehicleController.class)
                    .getVehicleById(vehicle.getId())).withSelfRel();
            vehicle.add(recordSelfLink);
            // Send created vehicle with 201 status code to client
            return new ResponseEntity(EntityModel.of(storedvehicle), HttpStatus.CREATED);
        }
        // If no vehicle is saved send 304 status code
        return new ResponseEntity(null, HttpStatus.NOT_MODIFIED);
    }


    //PATCH /api/vehicles/1 (update the price of a single vehicle)
    @Operation(summary = "Update a vehicle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "vehicle updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Vehicle.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "vehicle not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate vehicle name",
                    content = @Content) })
    @PatchMapping(value = "/{vehicleId}", produces = { "application/json" })
    public ResponseEntity<EntityModel<Vehicle>> updateVehicle(@Parameter(description = "id of vehicle to be updated")  @PathVariable UUID vehicleId, @Parameter(description = "vehicle updated information")  @RequestBody Vehicle vehicle){

        // Update vehicle in the database
        Vehicle storedvehicle = vehicleService.updateVehicle(vehicleId ,vehicle);

        // Check whether the vehicle exist and is updated or not
        if(storedvehicle!=null) {
            // selfLink to api that retrieves the vehicle according to HATEOS
            Link recordSelfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(VehicleController.class)
                    .getVehicleById(vehicle.getId())).withSelfRel();
            vehicle.add(recordSelfLink);
            // Send updated vehicle with 202 status code to client
            return new ResponseEntity(EntityModel.of(storedvehicle), HttpStatus.ACCEPTED);
        }

        // If no vehicle is found send 404 status code as response
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    // DELETE/api/vehicles/1 (delete a single vehicle)
    @Operation(summary = "Delete a vehicle by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "vehicle deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "vehicle not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content) })
    @DeleteMapping(value = "/{vehicleId}")
    public ResponseEntity<EntityModel<Vehicle>> deleteVehicle(@Parameter(description = "id of vehicle to be deleted")  @PathVariable UUID vehicleId){

        // Delete the vehicle by its id and check for the result
        if(vehicleService.deleteVehicle(vehicleId))
            // Sent empty response with 202 status code
            return new ResponseEntity(null, HttpStatus.ACCEPTED);
        else
            // If no vehicle is found send 404 status code as response
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
