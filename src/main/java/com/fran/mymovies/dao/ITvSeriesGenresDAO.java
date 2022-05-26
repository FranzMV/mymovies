package com.fran.mymovies.dao;

import com.fran.mymovies.entity.TvSerieGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITvSeriesGenresDAO extends JpaRepository<TvSerieGenre, Long> {
}
