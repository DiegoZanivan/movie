package com.outsera.movie.service;

import com.outsera.movie.dto.IntervalAwardV1Dto;
import com.outsera.movie.dto.ProducerV1Dto;
import com.outsera.movie.dto.ProducersAwardV1Dto;
import com.outsera.movie.entity.Movie;
import com.outsera.movie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private static final String AND = " and ";
    private static final String COMMA = ",";

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
        Map<String, ProducerV1Dto> producerV1DtoMap = new HashMap<>();
        AtomicInteger bigger = new AtomicInteger(0);
        AtomicInteger smaller = new AtomicInteger(Integer.MAX_VALUE);
        prepareProducers(movies, producerV1DtoMap, bigger, smaller);

        IntervalAwardV1Dto intervalAwardV1Dto = new IntervalAwardV1Dto();
        int finalBigger = bigger.get();
        intervalAwardV1Dto.setMax(producerV1DtoMap.values().stream()
                .filter(x -> x.getMaxInterval() != null && x.getMaxInterval() == finalBigger)
                .map(x -> {
                    ProducersAwardV1Dto producersAwardV1Dto = new ProducersAwardV1Dto();
                    producersAwardV1Dto.setProducer(x.getName());
                    producersAwardV1Dto.setInterval(x.getMaxInterval());
                    producersAwardV1Dto.setPreviousWin(x.getMaxIntervalYear()[0]);
                    producersAwardV1Dto.setFollowingWin(x.getMaxIntervalYear()[1]);
                    return producersAwardV1Dto;
                }).collect(Collectors.toList()));
        int finalSmaller = smaller.get();
        intervalAwardV1Dto.setMin(producerV1DtoMap.values().stream()
                .filter(x -> x.getMinInterval() != null && x.getMinInterval() == finalSmaller)
                .map(x -> {
                    ProducersAwardV1Dto producersAwardV1Dto = new ProducersAwardV1Dto();
                    producersAwardV1Dto.setProducer(x.getName());
                    producersAwardV1Dto.setInterval(x.getMinInterval());
                    producersAwardV1Dto.setPreviousWin(x.getMinIntervalYear()[0]);
                    producersAwardV1Dto.setFollowingWin(x.getMinIntervalYear()[1]);
                    return producersAwardV1Dto;
                }).collect(Collectors.toList()));

        return intervalAwardV1Dto;
    }

    private void prepareProducers(List<Movie> movies, Map<String, ProducerV1Dto> producersDto,
                                  AtomicInteger bigger, AtomicInteger smaller) {
        for (Movie movie : movies) {
            if (movie.getProducers().contains(COMMA) || movie.getProducers().contains(AND)) {
                List<String> producers = Arrays.stream(movie.getProducers().split(COMMA)).toList();

                for (int i = 0; i < producers.size()-1; i++) {
                    Movie m = cloneMovie(producers.get(i), movie);
                    prepareProducers(Collections.singletonList(m), producersDto, bigger, smaller);
                }

                String producerName = producers.get(producers.size() - 1);
                int index = producerName.indexOf(AND);
                if (index > -1) {
                    producerName = producerName.substring(producerName.indexOf(AND) + 5);
                }
                movie.setProducers(producerName);
            }

            ProducerV1Dto producerV1Dto;
            if (!producersDto.containsKey(movie.getProducers())) {
                producerV1Dto = new ProducerV1Dto();
                producerV1Dto.setName(movie.getProducers());
                producersDto.put(movie.getProducers(), producerV1Dto);
            } else {
                producerV1Dto = producersDto.get(movie.getProducers());
            }
            producerV1Dto.addYear(movie.getYearLaunch());
            if (producerV1Dto.getMaxInterval() > bigger.get()) {
                bigger.set(producerV1Dto.getMaxInterval());
            }

            if (producerV1Dto.getMinInterval() < smaller.get()) {
                smaller.set(producerV1Dto.getMinInterval());
            }
        }
    }

    private Movie cloneMovie(String producer, Movie movieOriginal) {
        Movie movie = new Movie();
        movie.setProducers(producer.trim());
        movie.setStudios(movieOriginal.getStudios());
        movie.setTitle(movieOriginal.getTitle());
        movie.setYearLaunch(movieOriginal.getYearLaunch());
        movie.setWinner(movieOriginal.isWinner());
        movie.setId(movieOriginal.getId());
        return movie;
    }
}
