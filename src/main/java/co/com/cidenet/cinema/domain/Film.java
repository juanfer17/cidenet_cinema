package co.com.cidenet.cinema.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Film.
 */
@Entity
@Table(name = "film")
public class Film implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "name", length = 30, nullable = false, unique = true)
    private String name;

    @NotNull
    @Size(min = 5, max = 30)
    @Column(name = "genre", length = 30, nullable = false)
    private String genre;

    @Lob
    @Column(name = "url_image", nullable = false)
    private byte[] urlImage;

    @NotNull
    @Column(name = "url_image_content_type", nullable = false)
    private String urlImageContentType;

    @NotNull
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "date")
    private LocalDate date;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Film id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Film name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return this.genre;
    }

    public Film genre(String genre) {
        this.setGenre(genre);
        return this;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public byte[] getUrlImage() {
        return this.urlImage;
    }

    public Film urlImage(byte[] urlImage) {
        this.setUrlImage(urlImage);
        return this;
    }

    public void setUrlImage(byte[] urlImage) {
        this.urlImage = urlImage;
    }

    public String getUrlImageContentType() {
        return this.urlImageContentType;
    }

    public Film urlImageContentType(String urlImageContentType) {
        this.urlImageContentType = urlImageContentType;
        return this;
    }

    public void setUrlImageContentType(String urlImageContentType) {
        this.urlImageContentType = urlImageContentType;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Film duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Film active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public Film date(LocalDate date) {
        this.setDate(date);
        return this;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Film)) {
            return false;
        }
        return id != null && id.equals(((Film) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Film{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", genre='" + getGenre() + "'" +
            ", urlImage='" + getUrlImage() + "'" +
            ", urlImageContentType='" + getUrlImageContentType() + "'" +
            ", duration=" + getDuration() +
            ", active='" + getActive() + "'" +
            ", date='" + getDate() + "'" +
            "}";
    }
}
