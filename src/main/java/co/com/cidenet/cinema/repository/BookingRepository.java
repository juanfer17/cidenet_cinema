package co.com.cidenet.cinema.repository;

import co.com.cidenet.cinema.domain.Booking;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Booking entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByFunctionFilmId(Long id);
    List<Booking> findByUser(Long user);
    List<Booking> findByFunctionFilmIdAndUser(Long id, Long user);
}
