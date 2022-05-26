package com.fran.mymovies.services;

import com.fran.mymovies.entity.TvSerieGenre;
import java.util.List;
import java.util.Optional;

public interface ITvSeriesGenresService {

    List<TvSerieGenre> findAll();

    Optional<TvSerieGenre> findById(Long id);

}
