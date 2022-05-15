package com.fran.mymovies.dao;

import com.fran.mymovies.entity.Movie;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Francisco David Manzanedo.
 */
public interface IMovieDAO extends CrudRepository<Movie, Long> {
}
