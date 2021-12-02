package co.com.cidenet.cinema.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A FunctionFilm.
 */
@Entity
@Table(name = "function_film")
public class FunctionFilm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "date_function", nullable = false)
    private LocalDate dateFunction;

    @ManyToOne(optional = false)
    @NotNull
    private Room room;

    @OneToOne(optional = false)
    @NotNull
    @MapsId
    @JoinColumn(name = "id")
    private Film film;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FunctionFilm id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFunction() {
        return this.dateFunction;
    }

    public FunctionFilm dateFunction(LocalDate dateFunction) {
        this.setDateFunction(dateFunction);
        return this;
    }

    public void setDateFunction(LocalDate dateFunction) {
        this.dateFunction = dateFunction;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public FunctionFilm room(Room room) {
        this.setRoom(room);
        return this;
    }

    public Film getFilm() {
        return this.film;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public FunctionFilm film(Film film) {
        this.setFilm(film);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FunctionFilm)) {
            return false;
        }
        return id != null && id.equals(((FunctionFilm) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FunctionFilm{" +
            "id=" + getId() +
            ", dateFunction='" + getDateFunction() + "'" +
            "}";
    }
}
