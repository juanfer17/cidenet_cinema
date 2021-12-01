package co.com.cidenet.cinema.repository;

import co.com.cidenet.cinema.domain.Film;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Film entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    List<Film> findByActive(Boolean active);
}
