package com.booky.controller;

import com.booky.model.dto.BookingDTO;
import com.booky.model.dto.BookingRq;
import com.booky.model.dto.GeneralResponse;
import com.booky.service.interfaces.IBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * @author dragos
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class BlocksController {

    private final IBookingService bookingService;

    @Operation(summary = "Gets blocks by ID",
            description = "Block must exist or it will return an error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookingDTO.class))
                    }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class))
                    })
    })
    @GetMapping("/blocks/{id}")
    public ResponseEntity<BookingDTO> getBlocksById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(bookingService.findBlockById(id));
    }

    @Operation(summary = "Gets all blocks of a property by ID",
            description = "Property must exist or it will return an error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(
                                            schema = @Schema(implementation = BookingDTO.class))
                            )
                    }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class))
                    })
    })
    @GetMapping("/blocks/{propertyId}/all")
    public ResponseEntity<List<BookingDTO>> getAllBlocksForProperty(@PathVariable Long propertyId) {
        return ResponseEntity.ok().body(bookingService.getAllBlocksByProperty(propertyId));
    }

    @Operation(summary = "Creates a block",
            description = "Creates a block")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookingDTO.class))
                    }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class))
                    })
    })
    //create a block
    @PostMapping("/blocks")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingRq req, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(bookingService.saveBlock(req, userDetails.getUsername()));
    }

    @Operation(summary = "Updates a block",
            description = "Block must exist or it will return an error")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok", content =
                    {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = BookingDTO.class))
                    }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class))
                    })
    })
    //update a booking
    @PutMapping("/blocks/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable("id") UUID id, @RequestBody BookingRq req, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(bookingService.updateBlock(id, req, userDetails.getUsername()));
    }

    @Operation(summary = "Deletes a block",
            description = "Deletes a block")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok. errorCode field will be empty", content =
                    {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class))
                    }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content =
                    {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = GeneralResponse.class))
                    })
    })
    //delete a booking
    @DeleteMapping("/blocks/{id}")
    public ResponseEntity<GeneralResponse> deleteBooking(@PathVariable("id") UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(bookingService.deleteBooking(id, userDetails.getUsername(), false));
    }
}
