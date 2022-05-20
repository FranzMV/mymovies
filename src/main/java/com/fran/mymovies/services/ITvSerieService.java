package com.fran.mymovies.services;

import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.entity.TvSerie;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @author Francisco David Manzanedo.
 */
public interface ITvSerieService {

    List<TvSerie> findAll();

    TvSerie findById(Long id);

    Page<TvSerie> findPage(int pageNumber);
}
