package com.fran.mymovies.services;

import com.fran.mymovies.dao.IMovieDAO;
import com.fran.mymovies.entity.Movie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Francisco David Manzanedo.
 */
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
        return movieDAO.findAll();
    }


    @Override
    public Movie findById(Long id) {
        return movieDAO.findById(id).orElse(null);
    }


    @Override
    public Page<Movie> findPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1, 12);
        return movieDAO.findAll(pageable);
    }
}
