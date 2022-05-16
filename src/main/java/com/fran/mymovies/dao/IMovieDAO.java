package com.fran.mymovies.dao;

import com.fran.mymovies.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Francisco David Manzanedo.
 */
public interface IMovieDAO extends JpaRepository<Movie, Long> {
}
