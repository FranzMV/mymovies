package com.fran.mymovies.dao;

import com.fran.mymovies.entity.TvSerie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Francisco David Manzanedo.
 */
public interface ITvSeriesDAO extends JpaRepository<TvSerie, Long> {

}
