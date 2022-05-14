package com.fran.mymovies.services;

import com.fran.mymovies.entity.Movie;

import java.util.List;


public interface IMovieService {

    public List<Movie> findAll();

    public Movie findById(Long id);
}
