package com.outsera.movie.controller;

import com.outsera.movie.dto.IntervalAwardV1Dto;
import com.outsera.movie.entity.Movie;
import com.outsera.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/movies")
public class MovieV1Controller {

    @Autowired
    private MovieService service;

    @GetMapping
    public ResponseEntity<List<Movie>> getAll() {
        return ResponseEntity.ok(service.getMovies());
    }

    @GetMapping(path = "/winners")
    public ResponseEntity<List<Movie>> getAllWinners() {
        return ResponseEntity.ok(service.getAllWinners());
    }

    @GetMapping(path = "/award-interval")
    public ResponseEntity<IntervalAwardV1Dto> getAwardInterval() {
        return ResponseEntity.ok(service.getAwardInterval());
    }
}
