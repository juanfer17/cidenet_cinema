package co.com.cidenet.cinema.service;

import co.com.cidenet.cinema.domain.Booking;
import co.com.cidenet.cinema.service.dto.BookingDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.cidenet.cinema.domain.Booking}.
 */
public interface BookingService {
    /**
     * Save a booking.
     *
     * @param bookingDTO the entity to save.
     * @return the persisted entity.
     */
    BookingDTO save(BookingDTO bookingDTO);

    /**
     * Partially updates a booking.
     *
     * @param bookingDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<BookingDTO> partialUpdate(BookingDTO bookingDTO);

    /**
     * Get all the bookings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BookingDTO> findAll(Pageable pageable);

    /**
     * Get the "id" booking.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BookingDTO> findOne(Long id);

    /**
     * Delete the "id" booking.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<Booking> bookingByFunction(Long id);

    List<Booking> bookingChairConfirm(List<BookingDTO> chairSelected);

    Page<Booking> bookingByUserPage(String user, Pageable pageable);

    void deleteAll(Long id);
}
