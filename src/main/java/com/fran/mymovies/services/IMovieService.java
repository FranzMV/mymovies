package com.fran.mymovies.services;

import com.fran.mymovies.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * @author Francisco David Manzanedo.
 */
public interface IMovieService {

    List<Movie> findAll();
    Movie findById(Long id);

    Page<Movie> findPage(int pageNumber);
}
