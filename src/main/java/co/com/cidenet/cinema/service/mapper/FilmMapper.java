package co.com.cidenet.cinema.service.mapper;

import co.com.cidenet.cinema.domain.Film;
import co.com.cidenet.cinema.service.dto.FilmDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Film} and its DTO {@link FilmDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface FilmMapper extends EntityMapper<FilmDTO, Film> {}
