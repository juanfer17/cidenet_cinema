package co.com.cidenet.cinema.service.dto;

import java.util.List;

public class ResponseBookingByUserDTO {

    List<FunctionFilmWithChairDTO> functionFilmList;

    public List<FunctionFilmWithChairDTO> getFunctionFilmList() {
        return functionFilmList;
    }

    public void setFunctionFilmList(List<FunctionFilmWithChairDTO> functionFilmList) {
        this.functionFilmList = functionFilmList;
    }
}
