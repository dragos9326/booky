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
import org.springframework.http.HttpStatus;
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
public class BookingController {

    private final IBookingService bookingService;

    @Operation(summary = "Gets bookings by ID",
            description = "bookings must exist or it will return an error")
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
    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(bookingService.findBookingById(id));
    }

    @Operation(summary = "Gets all bookings",
            description = "Available only to ADMIN")
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
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok().body(bookingService.getAllBookings());
    }

    @Operation(summary = "Creates a booking",
            description = "Creates a booking")
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
    //create a booking
    @PostMapping("/bookings")
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingRq req, @AuthenticationPrincipal UserDetails userDetails) {
        return new ResponseEntity<>(bookingService.saveBooking(req, userDetails.getUsername()), HttpStatus.CREATED);
    }

    @Operation(summary = "Updates a booking",
            description = "Booking must exist or it will return an error")
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
    @PutMapping("/bookings/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable("id") UUID id, @RequestBody BookingRq req, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(bookingService.updateBooking(id, req, userDetails.getUsername()));
    }


    @Operation(summary = "Deletes a booking",
            description = "Deletes a booking")
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
    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<GeneralResponse> deleteBooking(@PathVariable("id") UUID id, @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok().body(bookingService.deleteBooking(id, userDetails.getUsername(), true));
    }
}
