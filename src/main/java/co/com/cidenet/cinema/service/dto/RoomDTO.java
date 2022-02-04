package co.com.cidenet.cinema.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link co.com.cidenet.cinema.domain.Room} entity.
 */
public class RoomDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(min = 4, max = 8)
    private String roomName;

    @NotNull
    @Size(min = 2, max = 8)
    private String roomType;

    @NotNull
    @Min(value = 1)
    private Integer row;

    @NotNull
    @Min(value = 1)
    private Integer column;

    @NotNull
    private Boolean statusRoom;

    @NotNull
    private Double bookingPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public Integer getColumn() {
        return column;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public Boolean getStatusRoom() {
        return statusRoom;
    }

    public void setStatusRoom(Boolean statusRoom) {
        this.statusRoom = statusRoom;
    }

    public Double getBookingPrice() {
        return bookingPrice;
    }

    public void setBookingPrice(Double bookingPrice) {
        this.bookingPrice = bookingPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RoomDTO)) {
            return false;
        }

        RoomDTO roomDTO = (RoomDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, roomDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RoomDTO{" +
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
