package com.outsera.movie.dto;

import lombok.Data;

@Data
public class ProducersAwardV1Dto {

    private String producer;

    private Integer interval;

    private Integer previousWin;

    private Integer followingWin;
}
