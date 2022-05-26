package com.fran.mymovies.services;

import com.fran.mymovies.entity.MovieGenre;

import java.util.List;

public interface IMovieGenreService {

    List<MovieGenre> findAll();

    MovieGenre findById(Long id);


}
