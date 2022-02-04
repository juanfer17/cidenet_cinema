package co.com.cidenet.cinema.domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Room.
 */
@Entity
@Table(name = "room")
public class Room implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(min = 4, max = 8)
    @Column(name = "room_name", length = 8, nullable = false)
    private String roomName;

    @NotNull
    @Size(min = 2, max = 8)
    @Column(name = "room_type", length = 8, nullable = false)
    private String roomType;

    @NotNull
    @Min(value = 1)
    @Column(name = "row", nullable = false)
    private Integer row;

    @NotNull
    @Min(value = 1)
    @Column(name = "jhi_column", nullable = false)
    private Integer column;

    @NotNull
    @Column(name = "status_room", nullable = false)
    private Boolean statusRoom;

    @NotNull
    @Column(name = "booking_price", nullable = false)
    private Double bookingPrice;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Room id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return this.roomName;
    }

    public Room roomName(String roomName) {
        this.setRoomName(roomName);
        return this;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return this.roomType;
    }

    public Room roomType(String roomType) {
        this.setRoomType(roomType);
        return this;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getRow() {
        return this.row;
    }

    public Room row(Integer row) {
        this.setRow(row);
        return this;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return this.column;
    }

    public Room column(Integer column) {
        this.setColumn(column);
        return this;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Boolean getStatusRoom() {
        return this.statusRoom;
    }

    public Room statusRoom(Boolean statusRoom) {
        this.setStatusRoom(statusRoom);
        return this;
    }

    public void setStatusRoom(Boolean statusRoom) {
        this.statusRoom = statusRoom;
    }

    public Double getBookingPrice() {
        return this.bookingPrice;
    }

    public Room bookingPrice(Double bookingPrice) {
        this.setBookingPrice(bookingPrice);
        return this;
    }

    public void setBookingPrice(Double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Room)) {
            return false;
        }
        return id != null && id.equals(((Room) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Room{" +
            "id=" + getId() +
            ", roomName='" + getRoomName() + "'" +
            ", roomType='" + getRoomType() + "'" +
            ", row=" + getRow() +
            ", column=" + getColumn() +
            ", statusRoom='" + getStatusRoom() + "'" +
            ", bookingPrice=" + getBookingPrice() +
            "}";
    }
}
