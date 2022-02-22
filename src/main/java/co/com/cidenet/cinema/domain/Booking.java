package co.com.cidenet.cinema.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Booking.
 */
@Entity
@Table(name = "booking")
public class Booking implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "chair_location", length = 4)
    private String chairLocation;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "user")
    private Long user;

    @ManyToOne(optional = false)
    @NotNull
    // @JsonIgnoreProperties(value = { "room", "film" }, allowSetters = true, allowGetters = true)
    private FunctionFilm functionFilm;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Booking id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChairLocation() {
        return this.chairLocation;
    }

    public Booking chairLocation(String chairLocation) {
        this.setChairLocation(chairLocation);
        return this;
    }

    public void setChairLocation(String chairLocation) {
        this.chairLocation = chairLocation;
    }

    public String getStatus() {
        return this.status;
    }

    public Booking status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getUser() {
        return this.user;
    }

    public Booking user(Long user) {
        this.setUser(user);
        return this;
    }

    public void setUser(Long user) {
        this.user = user;
    }

    public FunctionFilm getFunctionFilm() {
        return this.functionFilm;
    }

    public void setFunctionFilm(FunctionFilm functionFilm) {
        this.functionFilm = functionFilm;
    }

    public Booking functionFilm(FunctionFilm functionFilm) {
        this.setFunctionFilm(functionFilm);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Booking)) {
            return false;
        }
        return id != null && id.equals(((Booking) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Booking{" +
            "id=" + getId() +
            ", chairLocation='" + getChairLocation() + "'" +
            ", status='" + getStatus() + "'" +
            ", user=" + getUser() +
            "}";
    }
}
