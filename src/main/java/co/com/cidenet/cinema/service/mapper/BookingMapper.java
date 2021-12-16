package co.com.cidenet.cinema.service.mapper;

import co.com.cidenet.cinema.domain.Booking;
import co.com.cidenet.cinema.service.dto.BookingDTO;
import java.util.List;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Booking} and its DTO {@link BookingDTO}.
 */
@Mapper(componentModel = "spring", uses = { FunctionFilmMapper.class })
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {
    @Mapping(target = "functionFilm", source = "functionFilm", qualifiedByName = "id")
    BookingDTO toDto(Booking s);

    List<Booking> toModel(List<BookingDTO> s);
}
