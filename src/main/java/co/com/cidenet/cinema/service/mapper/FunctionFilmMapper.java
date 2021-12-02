package co.com.cidenet.cinema.service.mapper;

import co.com.cidenet.cinema.domain.FunctionFilm;
import co.com.cidenet.cinema.service.dto.FunctionFilmDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link FunctionFilm} and its DTO {@link FunctionFilmDTO}.
 */
@Mapper(componentModel = "spring", uses = { RoomMapper.class, FilmMapper.class })
public interface FunctionFilmMapper extends EntityMapper<FunctionFilmDTO, FunctionFilm> {
    @Mapping(target = "room", source = "room", qualifiedByName = "id")
    @Mapping(target = "film", source = "film", qualifiedByName = "id")
    FunctionFilmDTO toDto(FunctionFilm s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "film", source = "film")
    FunctionFilmDTO toDtoId(FunctionFilm functionFilm);
}
