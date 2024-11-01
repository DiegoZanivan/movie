package com.outsera.movie.dto;

import lombok.Data;

import java.util.List;

@Data
public class IntervalAwardV1Dto {

    private List<ProducersAwardV1Dto> min;

    private List<ProducersAwardV1Dto> max;

}
