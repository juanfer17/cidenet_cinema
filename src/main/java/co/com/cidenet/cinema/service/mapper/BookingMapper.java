package co.com.cidenet.cinema.service.mapper;

import co.com.cidenet.cinema.domain.Booking;
import co.com.cidenet.cinema.service.dto.BookingDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, FunctionFilmMapper.class })
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "user", source = "user", qualifiedByName = "login")
    @Mapping(target = "functionFilm", source = "functionFilm", qualifiedByName = "id")
    BookingDTO toDto(Booking s);
}
