package com.outsera.movie.repository;

import com.outsera.movie.entity.Movie;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface MovieRepository extends Repository<Movie, Integer> {

    List<Movie> findAll();

    List<Movie> findByWinnerOrderByYearLaunchAsc(boolean winner);
}
