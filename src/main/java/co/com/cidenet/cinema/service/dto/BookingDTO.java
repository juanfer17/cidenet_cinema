package co.com.cidenet.cinema.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.cidenet.cinema.domain.Booking} entity.
 */
public class BookingDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 1, max = 4)
    private String chairLocation;

    @NotNull
    private String status;

    private Long user;

    private FunctionFilmDTO functionFilm;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChairLocation() {
        return chairLocation;
    }

    public void setChairLocation(String chairLocation) {
        this.chairLocation = chairLocation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public FunctionFilmDTO getFunctionFilm() {
        return functionFilm;
    }

    public void setFunctionFilm(FunctionFilmDTO functionFilm) {
        this.functionFilm = functionFilm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookingDTO)) {
            return false;
        }

        BookingDTO bookingDTO = (BookingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, bookingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookingDTO{" +
            "id=" + getId() +
            ", chairLocation='" + getChairLocation() + "'" +
            ", status='" + getStatus() + "'" +
            ", user=" + getUser() +
            ", functionFilm=" + getFunctionFilm() +
            "}";
    }
}
