package com.infy.cabbooking.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.infy.cabbooking.dto.BookingDTO;
import com.infy.cabbooking.exception.CabBookingException;
import com.infy.cabbooking.service.CabBookingService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "api")
public class CabBookingAPI
{
    @Autowired
    private CabBookingService cabBookingService;

    @Autowired
    private Environment environment;

    @GetMapping(value = "cab/{bookingType}")
    public ResponseEntity<List<BookingDTO>> getDetailsByBookingType(@PathVariable String bookingType) throws CabBookingException
    {
        List<BookingDTO> detailsByBookingType = cabBookingService.getDetailsByBookingType(bookingType);
        return new ResponseEntity(detailsByBookingType, HttpStatus.OK);
    }

    @PostMapping(value = "cab")
    public ResponseEntity<String> bookCab(@Valid @RequestBody  BookingDTO bookingDTO) throws CabBookingException
    {
        Integer bookingId = cabBookingService.bookCab(bookingDTO);
        String successMessage = environment.getProperty("API.BOOKING_SUCCESS") + bookingId;
	    return new ResponseEntity<>(successMessage , HttpStatus.CREATED);
    }

}
