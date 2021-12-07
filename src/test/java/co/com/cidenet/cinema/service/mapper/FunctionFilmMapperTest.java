package co.com.cidenet.cinema.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FunctionFilmMapperTest {

    private FunctionFilmMapper functionFilmMapper;

    @BeforeEach
    public void setUp() {
        functionFilmMapper = new FunctionFilmMapperImpl();
    }
}
