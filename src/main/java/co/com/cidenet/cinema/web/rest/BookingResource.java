package co.com.cidenet.cinema.web.rest;

import co.com.cidenet.cinema.domain.Booking;
import co.com.cidenet.cinema.repository.BookingRepository;
import co.com.cidenet.cinema.service.BookingService;
import co.com.cidenet.cinema.service.dto.BookingDTO;
import co.com.cidenet.cinema.service.dto.FunctionFilmWithChairDTO;
import co.com.cidenet.cinema.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link co.com.cidenet.cinema.domain.Booking}.
 */
@RestController
@RequestMapping("/api")
public class BookingResource {

    private final Logger log = LoggerFactory.getLogger(BookingResource.class);

    private static final String ENTITY_NAME = "booking";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookingService bookingService;

    private final BookingRepository bookingRepository;

    public BookingResource(BookingService bookingService, BookingRepository bookingRepository) {
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
    }

    /**
     * {@code POST  /bookings} : Create a new booking.
     *
     * @param bookingDTO the bookingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookingDTO, or with status {@code 400 (Bad Request)} if the booking has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bookings")
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody BookingDTO bookingDTO) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", bookingDTO);
        if (bookingDTO.getId() != null) {
            throw new BadRequestAlertException("A new booking cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookingDTO result = bookingService.save(bookingDTO);
        return ResponseEntity
            .created(new URI("/api/bookings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bookings/:id} : Updates an existing booking.
     *
     * @param id the id of the bookingDTO to save.
     * @param bookingDTO the bookingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingDTO,
     * or with status {@code 400 (Bad Request)} if the bookingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bookings/{id}")
    public ResponseEntity<BookingDTO> updateBooking(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookingDTO bookingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Booking : {}, {}", id, bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookingDTO result = bookingService.save(bookingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bookings/:id} : Partial updates given fields of an existing booking, field will ignore if it is null
     *
     * @param id the id of the bookingDTO to save.
     * @param bookingDTO the bookingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookingDTO,
     * or with status {@code 400 (Bad Request)} if the bookingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bookings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookingDTO> partialUpdateBooking(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookingDTO bookingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Booking partially : {}, {}", id, bookingDTO);
        if (bookingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookingDTO> result = bookingService.partialUpdate(bookingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bookings} : get all the bookings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookings in body.
     */
    @GetMapping("/bookings")
    public ResponseEntity<List<BookingDTO>> getAllBookings(Pageable pageable) {
        log.debug("REST request to get a page of Bookings");
        Page<BookingDTO> page = bookingService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bookings/:id} : get the "id" booking.
     *
     * @param id the id of the bookingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bookings/{id}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable Long id) {
        log.debug("REST request to get Booking : {}", id);
        Optional<BookingDTO> bookingDTO = bookingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookingDTO);
    }

    /**
     * {@code DELETE  /bookings/:id} : delete the "id" booking.
     *
     * @param id the id of the bookingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @PostMapping("/bookings/delete")
    public ResponseEntity<Void> deleteBooking(@RequestBody List<Long> id) {
        log.debug("REST request to delete Booking : {}", id);
        bookingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/bookings/byFunction/{id}")
    public ResponseEntity<List<Booking>> getBookingByFunction(@PathVariable Long id) {
        log.debug("REST request to Booking by Function: {}", id);
        return ResponseEntity.ok().body(bookingService.bookingByFunction(id));
    }

    @PostMapping("/bookings/confirm")
    public ResponseEntity<List<Booking>> updateBookingList(@Valid @RequestBody List<BookingDTO> bookingDTO) throws URISyntaxException {
        log.debug("REST request to save Booking : {}", bookingDTO);

        List<Booking> result = bookingService.bookingChairConfirm(bookingDTO);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/bookings/user/{user}")
    public ResponseEntity<List<FunctionFilmWithChairDTO>> getAllBookingsByUser(@PathVariable String user, Pageable pageable) {
        log.debug("REST request to get a page of Bookings");
        List<FunctionFilmWithChairDTO> page = bookingService.bookingByUserPage(user);
        return ResponseEntity.ok().body(page);
    }

    @DeleteMapping("/bookings/all/{id}")
    public ResponseEntity<Void> deleteAllByFunction(@PathVariable Long id) {
        log.debug("REST request to delete Booking : {}", id);
        bookingService.deleteAll(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
