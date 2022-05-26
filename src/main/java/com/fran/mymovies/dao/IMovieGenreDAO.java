package com.fran.mymovies.dao;

import com.fran.mymovies.entity.MovieGenre;
import org.springframework.data.jpa.repository.JpaRepository;


public interface IMovieGenreDAO extends JpaRepository<MovieGenre, Long> {

}
