package co.com.cidenet.cinema.service;

import co.com.cidenet.cinema.domain.FunctionFilm;
import co.com.cidenet.cinema.service.dto.FunctionFilmDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.cidenet.cinema.domain.FunctionFilm}.
 */
public interface FunctionFilmService {
    /**
     * Save a functionFilm.
     *
     * @param functionFilmDTO the entity to save.
     * @return the persisted entity.
     */
    FunctionFilmDTO save(FunctionFilmDTO functionFilmDTO);

    /**
     * Partially updates a functionFilm.
     *
     * @param functionFilmDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FunctionFilmDTO> partialUpdate(FunctionFilmDTO functionFilmDTO);

    /**
     * Get all the functionFilms.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FunctionFilmDTO> findAll(Pageable pageable);

    /**
     * Get the "id" functionFilm.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FunctionFilmDTO> findOne(Long id);

    /**
     * Delete the "id" functionFilm.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    List<FunctionFilm> functionByFilm(Long id);
}
