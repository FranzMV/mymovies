package com.fran.mymovies.services;

import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.entity.TvSerie;

import java.util.List;

public interface ITvSerieService {

    public List<TvSerie> findAll();

    public TvSerie findById(Long id);
}
