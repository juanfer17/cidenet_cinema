package co.com.cidenet.cinema.service.impl;

import co.com.cidenet.cinema.domain.Booking;
import co.com.cidenet.cinema.domain.User;
import co.com.cidenet.cinema.repository.BookingRepository;
import co.com.cidenet.cinema.repository.UserRepository;
import co.com.cidenet.cinema.service.BookingService;
import co.com.cidenet.cinema.service.dto.BookingDTO;
import co.com.cidenet.cinema.service.mapper.BookingMapper;
import co.com.cidenet.cinema.service.mapper.UtilMapperBooking;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Booking}.
 */
@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;

    private final BookingMapper bookingMapper;

    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.userRepository = userRepository;
    }

    @Override
    public BookingDTO save(BookingDTO bookingDTO) {
        log.debug("Request to save Booking : {}", bookingDTO);
        Booking booking = bookingMapper.toEntity(bookingDTO);
        booking = bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public Optional<BookingDTO> partialUpdate(BookingDTO bookingDTO) {
        log.debug("Request to partially update Booking : {}", bookingDTO);

        return bookingRepository
            .findById(bookingDTO.getId())
            .map(existingBooking -> {
                bookingMapper.partialUpdate(existingBooking, bookingDTO);

                return existingBooking;
            })
            .map(bookingRepository::save)
            .map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookingDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bookings");
        return bookingRepository.findAll(pageable).map(bookingMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookingDTO> findOne(Long id) {
        log.debug("Request to get Booking : {}", id);
        return bookingRepository.findById(id).map(bookingMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Booking : {}", id);
        Booking booking = bookingRepository.getById(id);
        booking.setUser(null);
        booking.setStatus("Available");
        bookingRepository.save(booking);
    }

    @Override
    public List<Booking> bookingByFunction(Long id) {
        return bookingRepository.findByFunctionFilmId(id);
    }

    @Override
    public List<Booking> bookingChairConfirm(List<BookingDTO> chairSelected) {
        User user = userRepository.findOneByLogin(chairSelected.get(0).getLogin()).get();

        chairSelected.forEach(data -> {
            data.setStatus("Reserved");
            data.setUser(user.getId());
        });

        UtilMapperBooking bookingMapper = new UtilMapperBooking();
        List<Booking> booking = bookingMapper.convertListToEntity(chairSelected);
        return bookingRepository.saveAll(booking);
    }

    @Override
    public Page<Booking> bookingByUserPage(String user, Pageable pageable) {
        User userData = userRepository.findOneByLogin(user).get();
        // return bookingRepository.findByUser(userData.getId(), pageable).map(bookingMapper::toDto);
        return bookingRepository.findByUser(userData.getId(), pageable);
    }

    @Override
    public void deleteAll(Long id) {
        log.debug("Request to delete All Booking : {}", id);
        Booking booking = bookingRepository.getById(id);
        List<Booking> bookingList = bookingRepository.findByFunctionFilmIdAndUser(booking.getFunctionFilm().getId(), booking.getUser());
        bookingList.forEach(data -> {
            data.setUser(null);
            data.setStatus("Available");
        });
        bookingRepository.saveAll(bookingList);
    }
}
