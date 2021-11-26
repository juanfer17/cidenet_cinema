package co.com.cidenet.cinema.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import co.com.cidenet.cinema.IntegrationTest;
import co.com.cidenet.cinema.domain.Film;
import co.com.cidenet.cinema.repository.FilmRepository;
import co.com.cidenet.cinema.service.dto.FilmDTO;
import co.com.cidenet.cinema.service.mapper.FilmMapper;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link FilmResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FilmResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_GENRE = "AAAAAAAAAA";
    private static final String UPDATED_GENRE = "BBBBBBBBBB";

    private static final byte[] DEFAULT_URL_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_URL_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_URL_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_URL_IMAGE_CONTENT_TYPE = "image/png";

    private static final Integer DEFAULT_DURATION = 1;
    private static final Integer UPDATED_DURATION = 2;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/films";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmMapper filmMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilmMockMvc;

    private Film film;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Film createEntity(EntityManager em) {
        Film film = new Film()
            .name(DEFAULT_NAME)
            .genre(DEFAULT_GENRE)
            .urlImage(DEFAULT_URL_IMAGE)
            .urlImageContentType(DEFAULT_URL_IMAGE_CONTENT_TYPE)
            .duration(DEFAULT_DURATION)
            .active(DEFAULT_ACTIVE)
            .date(DEFAULT_DATE);
        return film;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Film createUpdatedEntity(EntityManager em) {
        Film film = new Film()
            .name(UPDATED_NAME)
            .genre(UPDATED_GENRE)
            .urlImage(UPDATED_URL_IMAGE)
            .urlImageContentType(UPDATED_URL_IMAGE_CONTENT_TYPE)
            .duration(UPDATED_DURATION)
            .active(UPDATED_ACTIVE)
            .date(UPDATED_DATE);
        return film;
    }

    @BeforeEach
    public void initTest() {
        film = createEntity(em);
    }

    @Test
    @Transactional
    void createFilm() throws Exception {
        int databaseSizeBeforeCreate = filmRepository.findAll().size();
        // Create the Film
        FilmDTO filmDTO = filmMapper.toDto(film);
        restFilmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isCreated());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeCreate + 1);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFilm.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testFilm.getUrlImage()).isEqualTo(DEFAULT_URL_IMAGE);
        assertThat(testFilm.getUrlImageContentType()).isEqualTo(DEFAULT_URL_IMAGE_CONTENT_TYPE);
        assertThat(testFilm.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testFilm.getActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testFilm.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    void createFilmWithExistingId() throws Exception {
        // Create the Film with an existing ID
        film.setId(1L);
        FilmDTO filmDTO = filmMapper.toDto(film);

        int databaseSizeBeforeCreate = filmRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmRepository.findAll().size();
        // set the field null
        film.setName(null);

        // Create the Film, which fails.
        FilmDTO filmDTO = filmMapper.toDto(film);

        restFilmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isBadRequest());

        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkGenreIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmRepository.findAll().size();
        // set the field null
        film.setGenre(null);

        // Create the Film, which fails.
        FilmDTO filmDTO = filmMapper.toDto(film);

        restFilmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isBadRequest());

        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDurationIsRequired() throws Exception {
        int databaseSizeBeforeTest = filmRepository.findAll().size();
        // set the field null
        film.setDuration(null);

        // Create the Film, which fails.
        FilmDTO filmDTO = filmMapper.toDto(film);

        restFilmMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isBadRequest());

        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFilms() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get all the filmList
        restFilmMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(film.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].genre").value(hasItem(DEFAULT_GENRE)))
            .andExpect(jsonPath("$.[*].urlImageContentType").value(hasItem(DEFAULT_URL_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].urlImage").value(hasItem(Base64Utils.encodeToString(DEFAULT_URL_IMAGE))))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION)))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }

    @Test
    @Transactional
    void getFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        // Get the film
        restFilmMockMvc
            .perform(get(ENTITY_API_URL_ID, film.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(film.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.genre").value(DEFAULT_GENRE))
            .andExpect(jsonPath("$.urlImageContentType").value(DEFAULT_URL_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.urlImage").value(Base64Utils.encodeToString(DEFAULT_URL_IMAGE)))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFilm() throws Exception {
        // Get the film
        restFilmMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Update the film
        Film updatedFilm = filmRepository.findById(film.getId()).get();
        // Disconnect from session so that the updates on updatedFilm are not directly saved in db
        em.detach(updatedFilm);
        updatedFilm
            .name(UPDATED_NAME)
            .genre(UPDATED_GENRE)
            .urlImage(UPDATED_URL_IMAGE)
            .urlImageContentType(UPDATED_URL_IMAGE_CONTENT_TYPE)
            .duration(UPDATED_DURATION)
            .active(UPDATED_ACTIVE)
            .date(UPDATED_DATE);
        FilmDTO filmDTO = filmMapper.toDto(updatedFilm);

        restFilmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filmDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filmDTO))
            )
            .andExpect(status().isOk());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFilm.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testFilm.getUrlImage()).isEqualTo(UPDATED_URL_IMAGE);
        assertThat(testFilm.getUrlImageContentType()).isEqualTo(UPDATED_URL_IMAGE_CONTENT_TYPE);
        assertThat(testFilm.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testFilm.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testFilm.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void putNonExistingFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // Create the Film
        FilmDTO filmDTO = filmMapper.toDto(film);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filmDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // Create the Film
        FilmDTO filmDTO = filmMapper.toDto(film);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // Create the Film
        FilmDTO filmDTO = filmMapper.toDto(film);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilmWithPatch() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Update the film using partial update
        Film partialUpdatedFilm = new Film();
        partialUpdatedFilm.setId(film.getId());

        partialUpdatedFilm
            .urlImage(UPDATED_URL_IMAGE)
            .urlImageContentType(UPDATED_URL_IMAGE_CONTENT_TYPE)
            .active(UPDATED_ACTIVE)
            .date(UPDATED_DATE);

        restFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilm))
            )
            .andExpect(status().isOk());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testFilm.getGenre()).isEqualTo(DEFAULT_GENRE);
        assertThat(testFilm.getUrlImage()).isEqualTo(UPDATED_URL_IMAGE);
        assertThat(testFilm.getUrlImageContentType()).isEqualTo(UPDATED_URL_IMAGE_CONTENT_TYPE);
        assertThat(testFilm.getDuration()).isEqualTo(DEFAULT_DURATION);
        assertThat(testFilm.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testFilm.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void fullUpdateFilmWithPatch() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeUpdate = filmRepository.findAll().size();

        // Update the film using partial update
        Film partialUpdatedFilm = new Film();
        partialUpdatedFilm.setId(film.getId());

        partialUpdatedFilm
            .name(UPDATED_NAME)
            .genre(UPDATED_GENRE)
            .urlImage(UPDATED_URL_IMAGE)
            .urlImageContentType(UPDATED_URL_IMAGE_CONTENT_TYPE)
            .duration(UPDATED_DURATION)
            .active(UPDATED_ACTIVE)
            .date(UPDATED_DATE);

        restFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilm.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilm))
            )
            .andExpect(status().isOk());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
        Film testFilm = filmList.get(filmList.size() - 1);
        assertThat(testFilm.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testFilm.getGenre()).isEqualTo(UPDATED_GENRE);
        assertThat(testFilm.getUrlImage()).isEqualTo(UPDATED_URL_IMAGE);
        assertThat(testFilm.getUrlImageContentType()).isEqualTo(UPDATED_URL_IMAGE_CONTENT_TYPE);
        assertThat(testFilm.getDuration()).isEqualTo(UPDATED_DURATION);
        assertThat(testFilm.getActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testFilm.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // Create the Film
        FilmDTO filmDTO = filmMapper.toDto(film);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filmDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // Create the Film
        FilmDTO filmDTO = filmMapper.toDto(film);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filmDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFilm() throws Exception {
        int databaseSizeBeforeUpdate = filmRepository.findAll().size();
        film.setId(count.incrementAndGet());

        // Create the Film
        FilmDTO filmDTO = filmMapper.toDto(film);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilmMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(filmDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Film in the database
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFilm() throws Exception {
        // Initialize the database
        filmRepository.saveAndFlush(film);

        int databaseSizeBeforeDelete = filmRepository.findAll().size();

        // Delete the film
        restFilmMockMvc
            .perform(delete(ENTITY_API_URL_ID, film.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Film> filmList = filmRepository.findAll();
        assertThat(filmList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
