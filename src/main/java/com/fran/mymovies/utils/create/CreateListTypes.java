package com.fran.mymovies.utils.create;

import com.fran.mymovies.entity.ListType;
import com.fran.mymovies.entity.enums.ListTypeName;
import com.fran.mymovies.services.IListTypeService;
import com.fran.mymovies.services.ListTypeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class CreateListTypes implements CommandLineRunner {

    @Autowired
    ListTypeServiceImpl listTypeService;

    @Override
    public void run(String... args) throws Exception {
//        ListType favorita =  new ListType(ListTypeName.FAVORITA);
//        ListType pendiente =  new ListType(ListTypeName.PENDIENTE);
//        ListType vista = new ListType(ListTypeName.VISTA);
//
//        listTypeService.save(favorita);
//        listTypeService.save(pendiente);
//        listTypeService.save(vista);


    }
}
