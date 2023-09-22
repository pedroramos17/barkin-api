package com.barkin.BarkinAPI.controllers;

import com.barkin.BarkinAPI.entities.Gateway;
import com.barkin.BarkinAPI.services.GatewayService;
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
@ExposesResourceFor(Gateway.class)
@RequestMapping("/api/gateways")
public class GatewayController {

    @Autowired
    private EntityLinks entityLinks;

    @Autowired
    private GatewayService gatewayService;

    // GET /api/gateways (get a list of gateways)
    @Operation(summary = "Get a list of gateways (Ordened by id in ascending order), you must specify page number and page size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful Operation", content = { @Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    @GetMapping(produces = { "application/json" })
    public ResponseEntity<CollectionModel<Gateway>> getGateways(@Parameter(description = "Page index (start from 0)") @RequestParam(value = "page") Integer page, @Parameter(description = "Number of records per page") @RequestParam(value = "pageSize") Integer pageSize) {
        List<Gateway> gatewayList = gatewayService.listGateways(page, pageSize);

        for (Gateway gateway : gatewayList) {
            Link recordSelfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GatewayController.class)
                    .gatewayService.getGatewayById(gateway.getId())).withSelfRel();

            gateway.add(recordSelfLink);
        }

        CollectionModel<Gateway> resources = CollectionModel.of(gatewayList);

        Link selfLink = entityLinks.linkToCollectionResource(Gateway.class);
        resources.add(selfLink);

        return new ResponseEntity(EntityModel.of(resources), HttpStatus.OK);
    }

    // GET /api/gateways/1 (get one gateway from the list)
    @Operation(summary = "Get a gateway by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the gateway",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Gateway.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "gateway not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content) })
    @GetMapping(value = "/{gatewayId}", produces = { "application/json" })
    public ResponseEntity<EntityModel<Gateway>> getGatewayById(@Parameter(description = "id of gateway to be retrieved")  @PathVariable UUID gatewayId){

        // selfLink to api according to HATEOS
        Link selfLink = entityLinks.linkToItemResource(Gateway.class, gatewayId);
        // Retrieve requested gateway from database
        Gateway gateway = gatewayService.getGatewayById(gatewayId);
        //Check whether the gateway exist or not
        if(gateway!=null) {
            EntityModel<Gateway> resource = EntityModel.of(gateway);
            resource.add(selfLink);
            // Send data to client as a response
            return new ResponseEntity(EntityModel.of(resource),HttpStatus.OK);
        }

        // If no gateway is found send 404 status code as response
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }


    // POST /api/gateways (create a gateway)
    @Operation(summary = "Create a new gateway")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "gateway created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Gateway.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "gateway not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate gateway name",
                    content = @Content) })
    @PostMapping
    public ResponseEntity<EntityModel<Gateway>> createGateway(@RequestBody Gateway gateway){

        // Create and Save gateway in database
        Gateway storedgateway = gatewayService.insertGateway(gateway);

        // Check whether the gateway is saved or not
        if(gateway!=null) {
            // selfLink to api that retrieves the gateway according to HATEOS
            Link recordSelfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GatewayController.class)
                    .getGatewayById(gateway.getId())).withSelfRel();
            gateway.add(recordSelfLink);
            // Send created gateway with 201 status code to client
            return new ResponseEntity(EntityModel.of(storedgateway), HttpStatus.CREATED);
        }
        // If no gateway is saved send 304 status code
        return new ResponseEntity(null, HttpStatus.NOT_MODIFIED);
    }


    //PATCH /api/gateways/1 (update the price of a single gateway)
    @Operation(summary = "Update a gateway by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "gateway updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Gateway.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "gateway not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Duplicate gateway name",
                    content = @Content) })
    @PatchMapping(value = "/{gatewayId}", produces = { "application/json" })
    public ResponseEntity<EntityModel<Gateway>> updateGateway(@Parameter(description = "id of gateway to be updated")  @PathVariable UUID gatewayId, @Parameter(description = "gateway updated information")  @RequestBody Gateway gateway){

        // Update gateway in the database
        Gateway storedgateway = gatewayService.updateGateway(gatewayId ,gateway);

        // Check whether the gateway exist and is updated or not
        if(storedgateway!=null) {
            // selfLink to api that retrieves the gateway according to HATEOS
            Link recordSelfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(GatewayController.class)
                    .getGatewayById(gateway.getId())).withSelfRel();
            gateway.add(recordSelfLink);
            // Send updated gateway with 202 status code to client
            return new ResponseEntity(EntityModel.of(storedgateway), HttpStatus.ACCEPTED);
        }

        // If no gateway is found send 404 status code as response
        return new ResponseEntity(null, HttpStatus.NOT_FOUND);
    }

    // DELETE/api/gateways/1 (delete a single gateway)
    @Operation(summary = "Delete a gateway by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "gateway deleted",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "gateway not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error",
                    content = @Content) })
    @DeleteMapping(value = "/{gatewayId}")
    public ResponseEntity<EntityModel<Gateway>> deleteGateway(@Parameter(description = "id of gateway to be deleted")  @PathVariable UUID gatewayId){

        // Delete the gateway by its id and check for the result
        if(gatewayService.deleteGateway(gatewayId))
            // Sent empty response with 202 status code
            return new ResponseEntity(null, HttpStatus.ACCEPTED);
        else
            // If no gateway is found send 404 status code as response
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
