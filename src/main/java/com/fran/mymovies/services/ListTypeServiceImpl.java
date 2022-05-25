package com.fran.mymovies.services;

import com.fran.mymovies.entity.ListType;
import com.fran.mymovies.entity.Role;
import com.fran.mymovies.entity.enums.ListTypeName;
import com.fran.mymovies.entity.enums.RoleName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Service
@Transactional
public class ListTypeServiceImpl {

    @Autowired
    IListTypeService listTypeService;

    public void save(ListType listType){
        listTypeService.save(listType);
    }

    public Optional<ListType> getListTypeByName(ListTypeName listTypeName){
        return listTypeService.findByListTypeName(listTypeName);
    }

    public boolean existByListTypeName(ListTypeName listTypeName){
        return listTypeService.existsByListTypeName(listTypeName);
    }
}
