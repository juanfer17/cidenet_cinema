package co.com.cidenet.cinema.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import co.com.cidenet.cinema.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FunctionFilmDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FunctionFilmDTO.class);
        FunctionFilmDTO functionFilmDTO1 = new FunctionFilmDTO();
        functionFilmDTO1.setId(1L);
        FunctionFilmDTO functionFilmDTO2 = new FunctionFilmDTO();
        assertThat(functionFilmDTO1).isNotEqualTo(functionFilmDTO2);
        functionFilmDTO2.setId(functionFilmDTO1.getId());
        assertThat(functionFilmDTO1).isEqualTo(functionFilmDTO2);
        functionFilmDTO2.setId(2L);
        assertThat(functionFilmDTO1).isNotEqualTo(functionFilmDTO2);
        functionFilmDTO1.setId(null);
        assertThat(functionFilmDTO1).isNotEqualTo(functionFilmDTO2);
    }
}
