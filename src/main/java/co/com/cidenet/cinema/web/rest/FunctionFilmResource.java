package co.com.cidenet.cinema.web.rest;

import co.com.cidenet.cinema.domain.FunctionFilm;
import co.com.cidenet.cinema.repository.FunctionFilmRepository;
import co.com.cidenet.cinema.service.FunctionFilmService;
import co.com.cidenet.cinema.service.dto.DataFilmDTO;
import co.com.cidenet.cinema.service.dto.FunctionFilmDTO;
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
 * REST controller for managing {@link co.com.cidenet.cinema.domain.FunctionFilm}.
 */
@RestController
@RequestMapping("/api")
public class FunctionFilmResource {

    private final Logger log = LoggerFactory.getLogger(FunctionFilmResource.class);

    private static final String ENTITY_NAME = "functionFilm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FunctionFilmService functionFilmService;

    private final FunctionFilmRepository functionFilmRepository;

    public FunctionFilmResource(FunctionFilmService functionFilmService, FunctionFilmRepository functionFilmRepository) {
        this.functionFilmService = functionFilmService;
        this.functionFilmRepository = functionFilmRepository;
    }

    /**
     * {@code POST  /function-films} : Create a new functionFilm.
     *
     * @param functionFilmDTO the functionFilmDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new functionFilmDTO, or with status {@code 400 (Bad Request)} if the functionFilm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/function-films")
    public ResponseEntity<FunctionFilmDTO> createFunctionFilm(@Valid @RequestBody FunctionFilmDTO functionFilmDTO)
        throws URISyntaxException {
        log.debug("REST request to save FunctionFilm : {}", functionFilmDTO);
        if (functionFilmDTO.getId() != null) {
            throw new BadRequestAlertException("A new functionFilm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(functionFilmDTO.getFilm())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        FunctionFilmDTO result = functionFilmService.save(functionFilmDTO);
        return ResponseEntity
            .created(new URI("/api/function-films/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /function-films/:id} : Updates an existing functionFilm.
     *
     * @param id the id of the functionFilmDTO to save.
     * @param functionFilmDTO the functionFilmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated functionFilmDTO,
     * or with status {@code 400 (Bad Request)} if the functionFilmDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the functionFilmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/function-films/{id}")
    public ResponseEntity<FunctionFilmDTO> updateFunctionFilm(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FunctionFilmDTO functionFilmDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FunctionFilm : {}, {}", id, functionFilmDTO);
        if (functionFilmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, functionFilmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!functionFilmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FunctionFilmDTO result = functionFilmService.save(functionFilmDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, functionFilmDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /function-films/:id} : Partial updates given fields of an existing functionFilm, field will ignore if it is null
     *
     * @param id the id of the functionFilmDTO to save.
     * @param functionFilmDTO the functionFilmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated functionFilmDTO,
     * or with status {@code 400 (Bad Request)} if the functionFilmDTO is not valid,
     * or with status {@code 404 (Not Found)} if the functionFilmDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the functionFilmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/function-films/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FunctionFilmDTO> partialUpdateFunctionFilm(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FunctionFilmDTO functionFilmDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FunctionFilm partially : {}, {}", id, functionFilmDTO);
        if (functionFilmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, functionFilmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!functionFilmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FunctionFilmDTO> result = functionFilmService.partialUpdate(functionFilmDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, functionFilmDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /function-films} : get all the functionFilms.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of functionFilms in body.
     */
    @GetMapping("/function-films")
    public ResponseEntity<List<FunctionFilm>> getAllFunctionFilms(Pageable pageable) {
        log.debug("REST request to get a page of FunctionFilms");
        Page<FunctionFilm> page = functionFilmService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /function-films/:id} : get the "id" functionFilm.
     *
     * @param id the id of the functionFilmDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the functionFilmDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/function-films/{id}")
    public ResponseEntity<FunctionFilmDTO> getFunctionFilm(@PathVariable Long id) {
        log.debug("REST request to get FunctionFilm : {}", id);
        Optional<FunctionFilmDTO> functionFilmDTO = functionFilmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(functionFilmDTO);
    }

    /**
     * {@code DELETE  /function-films/:id} : delete the "id" functionFilm.
     *
     * @param id the id of the functionFilmDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/function-films/{id}")
    public ResponseEntity<Void> deleteFunctionFilm(@PathVariable Long id) {
        log.debug("REST request to delete FunctionFilm : {}", id);
        functionFilmService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @GetMapping("/function-films/view/{id}")
    public ResponseEntity<List<FunctionFilm>> functionByFilm(@PathVariable Long id) {
        log.debug("REST request to find Film by Function : {}", id);
        return ResponseEntity.ok().body(functionFilmService.functionByFilm(id));
    }

    @PostMapping("/function-films/date")
    public ResponseEntity<List<FunctionFilm>> getFunctionByDateFunction(@RequestBody DataFilmDTO dataFilmDTO) {
        log.debug("REST request to get a page of FunctionByDateFunctions");
        System.out.println("Fecha:  " + dataFilmDTO.getDateFunction());
        List<FunctionFilm> page = functionFilmService.functionByDateFunction(dataFilmDTO);
        return ResponseEntity.ok(page);
    }
}
