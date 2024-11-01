package com.outsera.movie.service;

import com.outsera.movie.dto.IntervalAwardV1Dto;
import com.outsera.movie.dto.ProducersAwardV1Dto;
import com.outsera.movie.entity.Movie;
import com.outsera.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository repository;

    public List<Movie> getMovies() {
        return repository.findAll();
    }

    public List<Movie> getAllWinners() {
        return repository.findByWinnerOrderByYearLaunchAsc(true);
    }

    public IntervalAwardV1Dto getAwardInterval() {
        List<Movie> movies = getAllWinners();
        List<Movie> moviesFinal = new ArrayList<>();
        movies.forEach(m -> {
            if (m.getProducers().contains(",") || m.getProducers().contains(" and ")) {
                List<String> producers = Arrays.stream(m.getProducers().split(",")).toList();

                List<String> producersFinal = new ArrayList<>();
                for (int i = 0; i < producers.size()-1; i++) {
                    producersFinal.add(producers.get(i));
                }

                producersFinal.addAll(Arrays.stream(producers.get(producers.size()-1).split(" and ")).toList());

                moviesFinal.addAll(producersFinal.stream().map(p -> {
                    Movie movie = new Movie();
                    movie.setProducers(p);
                    movie.setStudios(m.getStudios());
                    movie.setTitle(m.getTitle());
                    movie.setYearLaunch(m.getYearLaunch());
                    movie.setWinner(m.isWinner());
                    movie.setId(m.getId());
                    return movie;
                }).collect(Collectors.toList()));
            } else {
                moviesFinal.add(m);
            }
        });

        Map<String, ProducersAwardV1Dto> minAwards = new HashMap<>();
        int minInterval = Integer.MAX_VALUE;

        for (Movie movie : moviesFinal) {
            if (minAwards.get(movie.getProducers()) == null) {
                ProducersAwardV1Dto producersAwardV1Dto = new ProducersAwardV1Dto();
                producersAwardV1Dto.setPreviousWin(movie.getYearLaunch());
                producersAwardV1Dto.setProducer(movie.getProducers());
                minAwards.put(movie.getProducers(), producersAwardV1Dto);
            } else {
                var producer = minAwards.get(movie.getProducers());
                if (producer.getFollowingWin() == null) {
                    producer.setFollowingWin(movie.getYearLaunch());
                    producer.setInterval(producer.getFollowingWin() - producer.getPreviousWin());
                } else {
                    if ((movie.getYearLaunch() - producer.getFollowingWin()) < minInterval) {
                        producer.setPreviousWin(producer.getFollowingWin());
                        producer.setFollowingWin(movie.getYearLaunch());
                        producer.setInterval(producer.getFollowingWin() - producer.getPreviousWin());
                        minInterval = producer.getInterval();
                    }
                }

                if (minInterval > producer.getInterval()) {
                    minInterval = producer.getInterval();
                }
            }
        }

        int maxInterval = 0;
        Map<String, ProducersAwardV1Dto> maxAwards = new HashMap<>();
        for (Movie movie : moviesFinal) {
            if (maxAwards.get(movie.getProducers()) == null) {
                ProducersAwardV1Dto producersAwardV1Dto = new ProducersAwardV1Dto();
                producersAwardV1Dto.setPreviousWin(movie.getYearLaunch());
                producersAwardV1Dto.setProducer(movie.getProducers());
                maxAwards.put(movie.getProducers(), producersAwardV1Dto);
            } else {
                var producer = maxAwards.get(movie.getProducers());
                if (producer.getFollowingWin() == null) {
                    producer.setFollowingWin(movie.getYearLaunch());
                    producer.setInterval(producer.getFollowingWin() - producer.getPreviousWin());
                } else {
                    if ((movie.getYearLaunch() - producer.getFollowingWin()) < maxInterval) {
                        producer.setPreviousWin(producer.getFollowingWin());
                        producer.setFollowingWin(movie.getYearLaunch());
                        producer.setInterval(producer.getFollowingWin() - producer.getPreviousWin());
                        maxInterval = producer.getInterval();
                    }
                }

                if (maxInterval < producer.getInterval()) {
                    maxInterval = producer.getInterval();
                }
            }
        }

        int finalMaxInterval = maxInterval;
        int finalMinInterval = minInterval;

        IntervalAwardV1Dto intervalAwardV1Dto = new IntervalAwardV1Dto();
        intervalAwardV1Dto.setMax(maxAwards.values().stream().filter(x -> x.getInterval() != null && x.getInterval() == finalMaxInterval)
                .collect(Collectors.toList()));
        intervalAwardV1Dto.setMin(minAwards.values().stream().filter(x -> x.getInterval() != null && x.getInterval() == finalMinInterval)
                .collect(Collectors.toList()));

        return intervalAwardV1Dto;
    }
}
