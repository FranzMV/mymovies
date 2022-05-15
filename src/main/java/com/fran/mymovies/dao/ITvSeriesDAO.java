package com.fran.mymovies.dao;

import com.fran.mymovies.entity.TvSerie;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Francisco David Manzanedo.
 */
public interface ITvSeriesDAO extends CrudRepository<TvSerie, Long> {

}
