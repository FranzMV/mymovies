package com.fran.mymovies.services;

import com.fran.mymovies.entity.Movie;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Francisco David Manzanedo.
 */
public interface IMovieService {

    List<Movie> findAll();

    Movie findById(Long id);

    Page<Movie> findPage(int pageNumber);
}
