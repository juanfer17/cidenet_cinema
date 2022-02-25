package co.com.cidenet.cinema.service.impl;

import co.com.cidenet.cinema.domain.Booking;
import co.com.cidenet.cinema.domain.FunctionFilm;
import co.com.cidenet.cinema.repository.BookingRepository;
import co.com.cidenet.cinema.repository.FilmRepository;
import co.com.cidenet.cinema.repository.FunctionFilmRepository;
import co.com.cidenet.cinema.service.FunctionFilmService;
import co.com.cidenet.cinema.service.dto.DataFilmDTO;
import co.com.cidenet.cinema.service.dto.FunctionFilmDTO;
import co.com.cidenet.cinema.service.mapper.FunctionFilmMapper;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final BookingRepository bookingRepository;

    public FunctionFilmServiceImpl(
        FunctionFilmRepository functionFilmRepository,
        FunctionFilmMapper functionFilmMapper,
        FilmRepository filmRepository,
        BookingRepository bookingRepository
    ) {
        this.functionFilmRepository = functionFilmRepository;
        this.functionFilmMapper = functionFilmMapper;
        this.filmRepository = filmRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public FunctionFilmDTO save(FunctionFilmDTO functionFilmDTO) {
        log.debug("Request to save FunctionFilm : {}", functionFilmDTO);
        FunctionFilm functionFilm = functionFilmMapper.toEntity(functionFilmDTO);
        Long filmId = functionFilmDTO.getFilm().getId();
        filmRepository.findById(filmId).ifPresent(functionFilm::film);
        functionFilm = functionFilmRepository.save(functionFilm);
        createChairAvailable(functionFilm);
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
    public Page<FunctionFilm> findAll(Pageable pageable) {
        log.debug("Request to get all FunctionFilms");
        return functionFilmRepository.findAll(pageable);
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

    @Override
    public List<FunctionFilm> functionByFilm(Long id) {
        return functionFilmRepository.findByFilmId(id);
    }

    private void createChairAvailable(FunctionFilm function) {
        String chairName = "abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i <= function.getRoom().getColumn(); i++) {
            for (int j = 1; j <= function.getRoom().getRow(); j++) {
                Booking booking = new Booking();
                booking.setFunctionFilm(function);
                booking.setStatus("Available");
                booking.setChairLocation(chairName.charAt(i) + "-" + j);
                bookingRepository.save(booking);
            }
        }
    }

    @Override
    public List<FunctionFilm> functionByDateFunction(DataFilmDTO dateFunction) {
        LocalDate fecha = LocalDate.parse(dateFunction.getDateFunction());
        return functionFilmRepository.findByDateFunctionAndFilmId(fecha, dateFunction.getIdFilm());
    }
}
