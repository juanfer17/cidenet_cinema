package co.com.cidenet.cinema.domain;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.cidenet.cinema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FunctionFilmTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(FunctionFilm.class);
        FunctionFilm functionFilm1 = new FunctionFilm();
        functionFilm1.setId(1L);
        FunctionFilm functionFilm2 = new FunctionFilm();
        functionFilm2.setId(functionFilm1.getId());
        assertThat(functionFilm1).isEqualTo(functionFilm2);
        functionFilm2.setId(2L);
        assertThat(functionFilm1).isNotEqualTo(functionFilm2);
        functionFilm1.setId(null);
        assertThat(functionFilm1).isNotEqualTo(functionFilm2);
    }
}
