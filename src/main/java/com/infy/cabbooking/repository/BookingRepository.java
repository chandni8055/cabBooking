package com.infy.cabbooking.repository;

import com.infy.cabbooking.entity.Booking;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingRepository extends CrudRepository<Booking , Integer> {

    List<Booking> findByBookingType(String bookingType);

}
