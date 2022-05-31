package com.fran.mymovies.dao;

import com.fran.mymovies.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Francisco David Manzanedo.
 */
public interface IMovieDAO extends PagingAndSortingRepository<Movie, Long> {

//    @Query(value = "SELECT m FROM Movie m JOIN m.genres mg WHERE mg.id = :genreId" )
//    Page<Movie> findMoviesByGenre(Long id, Pageable pageable);

}
