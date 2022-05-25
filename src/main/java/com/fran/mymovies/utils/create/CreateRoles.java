package com.fran.mymovies.utils.create;

import com.fran.mymovies.entity.Role;
import com.fran.mymovies.entity.enums.RoleName;
import com.fran.mymovies.services.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class CreateRoles implements CommandLineRunner{

    @Autowired
    IRoleService roleService;

    @Override
    public void run(String... args) throws Exception {
//        Role rolAdmin = new Role(RoleName.ROLE_ADMIN);
//        Role rolUser = new Role(RoleName.ROLE_USER);
//        roleService.save(rolAdmin);
//        roleService.save(rolUser);

    }
}
