package com.fran.mymovies.services;

import com.fran.mymovies.dao.IMovieGenreDAO;
import com.fran.mymovies.entity.MovieGenre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Service
public class MovieGenreImpl implements IMovieGenreService{

    @Autowired
    private IMovieGenreDAO movieGenreDAO;

    @Override
    public List<MovieGenre> findAll() {
        return movieGenreDAO.findAll();
    }

    @Override
    public MovieGenre findById(Long id) {
        return  movieGenreDAO.findById(id).get();
    }
}
