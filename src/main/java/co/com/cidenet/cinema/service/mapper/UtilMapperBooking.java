package co.com.cidenet.cinema.service.mapper;

import co.com.cidenet.cinema.domain.Booking;
import co.com.cidenet.cinema.service.dto.BookingDTO;
import java.lang.reflect.Type;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

public class UtilMapperBooking {

    public List<Booking> convertListToEntity(List<BookingDTO> bookingDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Type listType = new TypeToken<List<Booking>>() {}.getType();
        List<Booking> bookingList = modelMapper.map(bookingDTO, listType);
        return bookingList;
    }
}
