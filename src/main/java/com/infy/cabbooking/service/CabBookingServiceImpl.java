package com.infy.cabbooking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.infy.cabbooking.dto.BookingDTO;
import com.infy.cabbooking.dto.CabDTO;
import com.infy.cabbooking.entity.Booking;
import com.infy.cabbooking.entity.Cab;
import com.infy.cabbooking.exception.CabBookingException;
import com.infy.cabbooking.repository.BookingRepository;
import com.infy.cabbooking.repository.CabRepository;
import com.infy.cabbooking.validator.BookingValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("cabBookingService")
@Transactional
public class CabBookingServiceImpl implements CabBookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private CabRepository cabRepository;

	@Override
	public List<BookingDTO> getDetailsByBookingType(String bookingType) throws CabBookingException {
		List<Booking> bookingList = bookingRepository.findByBookingType(bookingType);
		if(bookingList.isEmpty()){
			throw new CabBookingException("Service.NO_DETAILS_FOUND");
		}
		else{
			//TODO
			List<BookingDTO> result = new ArrayList<>();
			for (Booking booking: bookingList) {
				BookingDTO temp = new BookingDTO();
				temp.setBookingId(booking.getBookingId());
				temp.setCustomerName(booking.getCustomerName());
				temp.setPhoneNo(booking.getPhoneNo());
				temp.setBookingType(booking.getBookingType());

				Cab cab = booking.getCab();
				CabDTO tempCab = new CabDTO();

				tempCab.setCabNo(cab.getCabNo());
				tempCab.setModelName(cab.getModelName());
				tempCab.setDriverPhoneNo(cab.getDriverPhoneNo());
				tempCab.setAvailability(cab.getAvailability());

				temp.setCabDTO(tempCab);

				result.add(temp);
			}
			return result;
		}
	}

	
	@Override
	public Integer bookCab(BookingDTO bookingDTO) throws CabBookingException {
		BookingValidator validator  = new BookingValidator();

		validator.validate(bookingDTO);
		Optional<Cab> optionalCab = cabRepository.findById(bookingDTO.getCabDTO().getCabNo());

		if (optionalCab.isEmpty()) {
			throw new CabBookingException("Service.CAB_NOT_FOUND");
		} else {
			Cab cab = optionalCab.get();
			if(cab.getAvailability().equalsIgnoreCase("no")) {
				throw new CabBookingException("Service.CAB_NOT_AVAILABLE");
			} else {
				Booking booking = new Booking();
				booking.setBookingId(bookingDTO.getBookingId());
				booking.setBookingType(bookingDTO.getBookingType());
				booking.setCustomerName(bookingDTO.getCustomerName());
				booking.setPhoneNo(bookingDTO.getPhoneNo());

				cab.setAvailability("No");
				booking.setCab(cab);

				cabRepository.save(cab);

				Booking savedBooking = bookingRepository.save(booking);

				return savedBooking.getBookingId();
			}
		}
	}
	
	

}



