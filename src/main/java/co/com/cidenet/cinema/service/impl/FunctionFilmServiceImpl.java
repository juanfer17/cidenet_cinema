package co.com.cidenet.cinema.service.impl;

import co.com.cidenet.cinema.domain.FunctionFilm;
import co.com.cidenet.cinema.repository.FilmRepository;
import co.com.cidenet.cinema.repository.FunctionFilmRepository;
import co.com.cidenet.cinema.service.FunctionFilmService;
import co.com.cidenet.cinema.service.dto.FunctionFilmDTO;
import co.com.cidenet.cinema.service.mapper.FunctionFilmMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link FunctionFilm}.
 */
@Service
@Transactional
public class FunctionFilmServiceImpl implements FunctionFilmService {

    private final Logger log = LoggerFactory.getLogger(FunctionFilmServiceImpl.class);

    private final FunctionFilmRepository functionFilmRepository;

    private final FunctionFilmMapper functionFilmMapper;

    private final FilmRepository filmRepository;

    public FunctionFilmServiceImpl(
        FunctionFilmRepository functionFilmRepository,
        FunctionFilmMapper functionFilmMapper,
        FilmRepository filmRepository
    ) {
        this.functionFilmRepository = functionFilmRepository;
        this.functionFilmMapper = functionFilmMapper;
        this.filmRepository = filmRepository;
    }

    @Override
    public FunctionFilmDTO save(FunctionFilmDTO functionFilmDTO) {
        log.debug("Request to save FunctionFilm : {}", functionFilmDTO);
        FunctionFilm functionFilm = functionFilmMapper.toEntity(functionFilmDTO);
        Long filmId = functionFilmDTO.getFilm().getId();
        filmRepository.findById(filmId).ifPresent(functionFilm::film);
        functionFilm = functionFilmRepository.save(functionFilm);
        return functionFilmMapper.toDto(functionFilm);
    }

    @Override
    public Optional<FunctionFilmDTO> partialUpdate(FunctionFilmDTO functionFilmDTO) {
        log.debug("Request to partially update FunctionFilm : {}", functionFilmDTO);

        return functionFilmRepository
            .findById(functionFilmDTO.getId())
            .map(existingFunctionFilm -> {
                functionFilmMapper.partialUpdate(existingFunctionFilm, functionFilmDTO);

                return existingFunctionFilm;
            })
            .map(functionFilmRepository::save)
            .map(functionFilmMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FunctionFilmDTO> findAll(Pageable pageable) {
        log.debug("Request to get all FunctionFilms");
        return functionFilmRepository.findAll(pageable).map(functionFilmMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<FunctionFilmDTO> findOne(Long id) {
        log.debug("Request to get FunctionFilm : {}", id);
        return functionFilmRepository.findById(id).map(functionFilmMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete FunctionFilm : {}", id);
        functionFilmRepository.deleteById(id);
    }
}
