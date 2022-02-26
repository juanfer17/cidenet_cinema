package co.com.cidenet.cinema.service.dto;

import co.com.cidenet.cinema.domain.Booking;
import co.com.cidenet.cinema.domain.FunctionFilm;
import java.util.List;

public class FunctionFilmWithChairDTO {

    FunctionFilm functionFilmData;
    List<Booking> chairList;

    public FunctionFilm getFunctionFilmData() {
        return functionFilmData;
    }

    public void setFunctionFilmData(FunctionFilm functionFilmData) {
        this.functionFilmData = functionFilmData;
    }

    public List<Booking> getChairList() {
        return chairList;
    }

    public void setChairList(List<Booking> chairList) {
        this.chairList = chairList;
    }
}
