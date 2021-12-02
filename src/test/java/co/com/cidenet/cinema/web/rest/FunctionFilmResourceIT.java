package co.com.cidenet.cinema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.cidenet.cinema.IntegrationTest;
import co.com.cidenet.cinema.domain.Film;
import co.com.cidenet.cinema.domain.FunctionFilm;
import co.com.cidenet.cinema.domain.Room;
import co.com.cidenet.cinema.repository.FunctionFilmRepository;
import co.com.cidenet.cinema.service.dto.FunctionFilmDTO;
import co.com.cidenet.cinema.service.mapper.FunctionFilmMapper;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FunctionFilmResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FunctionFilmResourceIT {

    private static final LocalDate DEFAULT_DATE_FUNCTION = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FUNCTION = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/function-films";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FunctionFilmRepository functionFilmRepository;

    @Autowired
    private FunctionFilmMapper functionFilmMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFunctionFilmMockMvc;

    private FunctionFilm functionFilm;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FunctionFilm createEntity(EntityManager em) {
        FunctionFilm functionFilm = new FunctionFilm().dateFunction(DEFAULT_DATE_FUNCTION);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        functionFilm.setRoom(room);
        // Add required entity
        Film film;
        if (TestUtil.findAll(em, Film.class).isEmpty()) {
            film = FilmResourceIT.createEntity(em);
            em.persist(film);
            em.flush();
        } else {
            film = TestUtil.findAll(em, Film.class).get(0);
        }
        functionFilm.setFilm(film);
        return functionFilm;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FunctionFilm createUpdatedEntity(EntityManager em) {
        FunctionFilm functionFilm = new FunctionFilm().dateFunction(UPDATED_DATE_FUNCTION);
        // Add required entity
        Room room;
        if (TestUtil.findAll(em, Room.class).isEmpty()) {
            room = RoomResourceIT.createUpdatedEntity(em);
            em.persist(room);
            em.flush();
        } else {
            room = TestUtil.findAll(em, Room.class).get(0);
        }
        functionFilm.setRoom(room);
        // Add required entity
        Film film;
        if (TestUtil.findAll(em, Film.class).isEmpty()) {
            film = FilmResourceIT.createUpdatedEntity(em);
            em.persist(film);
            em.flush();
        } else {
            film = TestUtil.findAll(em, Film.class).get(0);
        }
        functionFilm.setFilm(film);
        return functionFilm;
    }

    @BeforeEach
    public void initTest() {
        functionFilm = createEntity(em);
    }

    @Test
    @Transactional
    void createFunctionFilm() throws Exception {
        int databaseSizeBeforeCreate = functionFilmRepository.findAll().size();
        // Create the FunctionFilm
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(functionFilm);
        restFunctionFilmMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isCreated());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeCreate + 1);
        FunctionFilm testFunctionFilm = functionFilmList.get(functionFilmList.size() - 1);
        assertThat(testFunctionFilm.getDateFunction()).isEqualTo(DEFAULT_DATE_FUNCTION);

        // Validate the id for MapsId, the ids must be same
        assertThat(testFunctionFilm.getId()).isEqualTo(testFunctionFilm.getFilm().getId());
    }

    @Test
    @Transactional
    void createFunctionFilmWithExistingId() throws Exception {
        // Create the FunctionFilm with an existing ID
        functionFilm.setId(1L);
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(functionFilm);

        int databaseSizeBeforeCreate = functionFilmRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFunctionFilmMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDateFunctionIsRequired() throws Exception {
        int databaseSizeBeforeTest = functionFilmRepository.findAll().size();
        // set the field null
        functionFilm.setDateFunction(null);

        // Create the FunctionFilm, which fails.
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(functionFilm);

        restFunctionFilmMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isBadRequest());

        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFunctionFilms() throws Exception {
        // Initialize the database
        functionFilmRepository.saveAndFlush(functionFilm);

        // Get all the functionFilmList
        restFunctionFilmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(functionFilm.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFunction").value(hasItem(DEFAULT_DATE_FUNCTION.toString())));
    }

    @Test
    @Transactional
    void getFunctionFilm() throws Exception {
        // Initialize the database
        functionFilmRepository.saveAndFlush(functionFilm);

        // Get the functionFilm
        restFunctionFilmMockMvc
            .perform(get(ENTITY_API_URL_ID, functionFilm.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(functionFilm.getId().intValue()))
            .andExpect(jsonPath("$.dateFunction").value(DEFAULT_DATE_FUNCTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFunctionFilm() throws Exception {
        // Get the functionFilm
        restFunctionFilmMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFunctionFilm() throws Exception {
        // Initialize the database
        functionFilmRepository.saveAndFlush(functionFilm);

        int databaseSizeBeforeUpdate = functionFilmRepository.findAll().size();

        // Update the functionFilm
        FunctionFilm updatedFunctionFilm = functionFilmRepository.findById(functionFilm.getId()).get();
        // Disconnect from session so that the updates on updatedFunctionFilm are not directly saved in db
        em.detach(updatedFunctionFilm);
        updatedFunctionFilm.dateFunction(UPDATED_DATE_FUNCTION);
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(updatedFunctionFilm);

        restFunctionFilmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, functionFilmDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isOk());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeUpdate);
        FunctionFilm testFunctionFilm = functionFilmList.get(functionFilmList.size() - 1);
        assertThat(testFunctionFilm.getDateFunction()).isEqualTo(UPDATED_DATE_FUNCTION);
    }

    @Test
    @Transactional
    void putNonExistingFunctionFilm() throws Exception {
        int databaseSizeBeforeUpdate = functionFilmRepository.findAll().size();
        functionFilm.setId(count.incrementAndGet());

        // Create the FunctionFilm
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(functionFilm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunctionFilmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, functionFilmDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFunctionFilm() throws Exception {
        int databaseSizeBeforeUpdate = functionFilmRepository.findAll().size();
        functionFilm.setId(count.incrementAndGet());

        // Create the FunctionFilm
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(functionFilm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionFilmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFunctionFilm() throws Exception {
        int databaseSizeBeforeUpdate = functionFilmRepository.findAll().size();
        functionFilm.setId(count.incrementAndGet());

        // Create the FunctionFilm
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(functionFilm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionFilmMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFunctionFilmWithPatch() throws Exception {
        // Initialize the database
        functionFilmRepository.saveAndFlush(functionFilm);

        int databaseSizeBeforeUpdate = functionFilmRepository.findAll().size();

        // Update the functionFilm using partial update
        FunctionFilm partialUpdatedFunctionFilm = new FunctionFilm();
        partialUpdatedFunctionFilm.setId(functionFilm.getId());

        partialUpdatedFunctionFilm.dateFunction(UPDATED_DATE_FUNCTION);

        restFunctionFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunctionFilm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunctionFilm))
            )
            .andExpect(status().isOk());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeUpdate);
        FunctionFilm testFunctionFilm = functionFilmList.get(functionFilmList.size() - 1);
        assertThat(testFunctionFilm.getDateFunction()).isEqualTo(UPDATED_DATE_FUNCTION);
    }

    @Test
    @Transactional
    void fullUpdateFunctionFilmWithPatch() throws Exception {
        // Initialize the database
        functionFilmRepository.saveAndFlush(functionFilm);

        int databaseSizeBeforeUpdate = functionFilmRepository.findAll().size();

        // Update the functionFilm using partial update
        FunctionFilm partialUpdatedFunctionFilm = new FunctionFilm();
        partialUpdatedFunctionFilm.setId(functionFilm.getId());

        partialUpdatedFunctionFilm.dateFunction(UPDATED_DATE_FUNCTION);

        restFunctionFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFunctionFilm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFunctionFilm))
            )
            .andExpect(status().isOk());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeUpdate);
        FunctionFilm testFunctionFilm = functionFilmList.get(functionFilmList.size() - 1);
        assertThat(testFunctionFilm.getDateFunction()).isEqualTo(UPDATED_DATE_FUNCTION);
    }

    @Test
    @Transactional
    void patchNonExistingFunctionFilm() throws Exception {
        int databaseSizeBeforeUpdate = functionFilmRepository.findAll().size();
        functionFilm.setId(count.incrementAndGet());

        // Create the FunctionFilm
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(functionFilm);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFunctionFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, functionFilmDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFunctionFilm() throws Exception {
        int databaseSizeBeforeUpdate = functionFilmRepository.findAll().size();
        functionFilm.setId(count.incrementAndGet());

        // Create the FunctionFilm
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(functionFilm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFunctionFilm() throws Exception {
        int databaseSizeBeforeUpdate = functionFilmRepository.findAll().size();
        functionFilm.setId(count.incrementAndGet());

        // Create the FunctionFilm
        FunctionFilmDTO functionFilmDTO = functionFilmMapper.toDto(functionFilm);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFunctionFilmMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(functionFilmDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the FunctionFilm in the database
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFunctionFilm() throws Exception {
        // Initialize the database
        functionFilmRepository.saveAndFlush(functionFilm);

        int databaseSizeBeforeDelete = functionFilmRepository.findAll().size();

        // Delete the functionFilm
        restFunctionFilmMockMvc
            .perform(delete(ENTITY_API_URL_ID, functionFilm.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<FunctionFilm> functionFilmList = functionFilmRepository.findAll();
        assertThat(functionFilmList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
