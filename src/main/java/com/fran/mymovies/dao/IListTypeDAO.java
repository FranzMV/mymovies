package com.fran.mymovies.dao;

import com.fran.mymovies.entity.ListType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IListTypeDAO extends JpaRepository<ListType, Long> {
}
