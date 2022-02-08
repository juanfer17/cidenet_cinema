package co.com.cidenet.cinema.service.dto;

import java.time.LocalDate;

public class DataFilmDTO {

    private Long idFilm;

    private LocalDate dateFunction;

    public LocalDate getDateFunction() {
        return dateFunction;
    }

    public void setDateFunction(LocalDate dateFunction) {
        this.dateFunction = dateFunction;
    }

    public Long getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(Long idFilm) {
        this.idFilm = idFilm;
    }
}
