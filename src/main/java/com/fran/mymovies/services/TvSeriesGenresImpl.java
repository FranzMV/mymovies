package com.fran.mymovies.services;

import com.fran.mymovies.dao.ITvSeriesGenresDAO;
import com.fran.mymovies.entity.TvSerieGenre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Service
public class TvSeriesGenresImpl implements ITvSeriesGenresService{


    @Autowired
    private ITvSeriesGenresDAO tvSeriesGenresDAO;

    @Override
    @Transactional(readOnly = true)
    public List<TvSerieGenre> findAll() {
        return tvSeriesGenresDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TvSerieGenre> findById(Long id) {
        return tvSeriesGenresDAO.findById(id);
    }
}
