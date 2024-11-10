package com.outsera.movie.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProducerV1Dto {

    private String name;

    @Setter(AccessLevel.NONE)
    private List<Integer> years;

    @Setter(AccessLevel.NONE)
    private Integer maxInterval;

    @Setter(AccessLevel.NONE)
    private Integer minInterval;

    @Setter(AccessLevel.NONE)
    private Integer[] minIntervalYear;

    @Setter(AccessLevel.NONE)
    private Integer[] maxIntervalYear;

    public void addYear(Integer yearLaunch) {
        if (this.years == null) {
            years = new ArrayList<>();
            years.add(yearLaunch);
            maxInterval = 0;
            minInterval = Integer.MAX_VALUE;
        } else {
            int lastYear = years.get(years.size() - 1);

            Integer interval = yearLaunch - lastYear;
            if (maxInterval < interval) {
                maxInterval = interval;
                maxIntervalYear = new Integer[] {lastYear, yearLaunch};
            }

            if (minInterval > interval) {
                minInterval = interval;
                minIntervalYear = new Integer[] {lastYear, yearLaunch};
            }

            years.add(yearLaunch);
        }
    }

}
