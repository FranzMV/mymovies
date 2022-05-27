package com.fran.mymovies.services;

import com.fran.mymovies.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Francisco David Manzanedo.
 */

public interface IMovieService  {

    @Transactional(readOnly = true)
    List<Movie> findAll();

    @Transactional(readOnly = true)
    Movie findById(Long id);

    @Transactional(readOnly = true)
    Page<Movie> findPage(int pageNumber);


    @Transactional(readOnly = true)
    Page<Movie> findPageFilterByGenre(int pageNumber, Long id);

}
