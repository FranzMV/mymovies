package com.fran.mymovies.services;

import com.fran.mymovies.dao.ITvSeriesDAO;
import com.fran.mymovies.entity.TvSerie;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TvSerieServiceImpl implements ITvSerieService{

    @Autowired
    private ITvSeriesDAO tvSeriesDAO;

    @Override
    @Transactional(readOnly = true)
    public List<TvSerie> findAll() {
        return (List<TvSerie>) tvSeriesDAO.findAll();
    }

    @Override
    public TvSerie findById(Long id) {
        return tvSeriesDAO.findById(id).orElse(null);
    }
}
