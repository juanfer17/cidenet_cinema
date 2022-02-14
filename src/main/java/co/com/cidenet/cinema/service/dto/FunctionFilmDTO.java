package co.com.cidenet.cinema.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.cidenet.cinema.domain.FunctionFilm} entity.
 */
public class FunctionFilmDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate dateFunction;

    @NotNull
    private String timeFunction;

    private RoomDTO room;

    private FilmDTO film;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFunction() {
        return dateFunction;
    }

    public void setDateFunction(LocalDate dateFunction) {
        this.dateFunction = dateFunction;
    }

    public String getTimeFunction() {
        return timeFunction;
    }

    public void setTimeFunction(String timeFunction) {
        this.timeFunction = timeFunction;
    }

    public RoomDTO getRoom() {
        return room;
    }

    public void setRoom(RoomDTO room) {
        this.room = room;
    }

    public FilmDTO getFilm() {
        return film;
    }

    public void setFilm(FilmDTO film) {
        this.film = film;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionFilmDTO)) {
            return false;
        }

        FunctionFilmDTO functionFilmDTO = (FunctionFilmDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, functionFilmDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FunctionFilmDTO{" +
            "id=" + getId() +
            ", dateFunction='" + getDateFunction() + "'" +
            ", timeFunction='" + getTimeFunction() + "'" +
            ", room=" + getRoom() +
            ", film=" + getFilm() +
            "}";
    }
}
