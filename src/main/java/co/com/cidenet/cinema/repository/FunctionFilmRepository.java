package co.com.cidenet.cinema.repository;

import co.com.cidenet.cinema.domain.FunctionFilm;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the FunctionFilm entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FunctionFilmRepository extends JpaRepository<FunctionFilm, Long> {
    List<FunctionFilm> findByFilmId(Long id);
}
