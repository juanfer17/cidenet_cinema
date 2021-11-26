package co.com.cidenet.cinema.service;

import co.com.cidenet.cinema.service.dto.FilmDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link co.com.cidenet.cinema.domain.Film}.
 */
public interface FilmService {
    /**
     * Save a film.
     *
     * @param filmDTO the entity to save.
     * @return the persisted entity.
     */
    FilmDTO save(FilmDTO filmDTO);

    /**
     * Partially updates a film.
     *
     * @param filmDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FilmDTO> partialUpdate(FilmDTO filmDTO);

    /**
     * Get all the films.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<FilmDTO> findAll(Pageable pageable);

    /**
     * Get the "id" film.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FilmDTO> findOne(Long id);

    /**
     * Delete the "id" film.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
