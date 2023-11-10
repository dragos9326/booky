package com.booky.controller;

import com.booky.model.dto.BookingDTO;
import com.booky.model.dto.GeneralResponse;
import com.booky.service.interfaces.IPropertiesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dragos
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class PropertiesController {

    private final IPropertiesService propertiesService;

    @Operation(summary = "Gets all bookings",
            description = "Available to ADMIN for every porperty or to OWNERs for theirs")
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
    @GetMapping("/properties/{id}/bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookingsAndBlocks(@PathVariable Long id) {
        return ResponseEntity.ok().body(propertiesService.getAllBookingsAndBlocks(id));
    }

}
