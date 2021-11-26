package co.com.cidenet.cinema.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import javax.persistence.Lob;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.cidenet.cinema.domain.Film} entity.
 */
public class FilmDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    private String name;

    @NotNull
    @Size(min = 5, max = 30)
    private String genre;

    @Lob
    private byte[] urlImage;

    private String urlImageContentType;

    @NotNull
    private Integer duration;

    private Boolean active;

    private LocalDate date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public byte[] getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(byte[] urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlImageContentType() {
        return urlImageContentType;
    }

    public void setUrlImageContentType(String urlImageContentType) {
        this.urlImageContentType = urlImageContentType;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FilmDTO)) {
            return false;
        }

        FilmDTO filmDTO = (FilmDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, filmDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FilmDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", genre='" + getGenre() + "'" +
            ", urlImage='" + getUrlImage() + "'" +
            ", duration=" + getDuration() +
            ", active='" + getActive() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
