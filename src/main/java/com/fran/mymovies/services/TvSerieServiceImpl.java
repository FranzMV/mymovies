package com.fran.mymovies.services;

import com.fran.mymovies.dao.ITvSeriesDAO;
import com.fran.mymovies.entity.TvSerie;
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
public class TvSerieServiceImpl implements ITvSerieService{

    @Autowired
    private ITvSeriesDAO tvSeriesDAO;

    @Override
    @Transactional(readOnly = true)
    public List<TvSerie> findAll() {
        return  tvSeriesDAO.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TvSerie findById(Long id) {
        return tvSeriesDAO.findById(id).get();
    }

    @Override
    public Page<TvSerie> findPage(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber -1, 12);
        return tvSeriesDAO.findAll(pageable);
    }
}
