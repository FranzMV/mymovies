package com.fran.mymovies.services;

import com.fran.mymovies.dao.IListTypeDAO;
import com.fran.mymovies.entity.ListType;
import com.fran.mymovies.entity.enums.ListTypeName;

import java.util.Optional;

public interface IListTypeService extends IListTypeDAO {


    Optional<ListType> findByListTypeName(ListTypeName listTypeName);
    boolean existsByListTypeName(ListTypeName listTypeName);

}
