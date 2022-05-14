package com.fran.mymovies.services;

import com.fran.mymovies.dao.IMovieDAO;
import com.fran.mymovies.entity.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@Service
public class MovieServiceImpl implements IMovieService{

    @Autowired
    private IMovieDAO movieDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Movie> findAll() {
        return (List<Movie>) movieDAO.findAll();
    }

    @Override
    public Movie findById(Long id) {
        return movieDAO.findById(id).orElse(null);
    }
}
