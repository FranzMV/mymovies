package com.fran.mymovies.security.config;

import com.fran.mymovies.entity.Role;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.entity.enums.RoleName;
import com.fran.mymovies.services.IRoleService;
import com.fran.mymovies.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class CreateAdmin implements CommandLineRunner {

    @Autowired
    IUserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    IRoleService roleService;

    @Override
    public void run(String... args) throws Exception {
//        User user = new User();
//        String passwordEncoded = passwordEncoder.encode("1234");
//        user.setUserName("admin");
//        user.setName("admin");
//        user.setPassword(passwordEncoded);
//        Role rolAdmin = roleService.findByRoleName(RoleName.ROLE_ADMIN).get();
//        Role rolUser = roleService.findByRoleName(RoleName.ROLE_USER).get();
//        Set<Role> roles = new HashSet<>();
//        roles.add(rolAdmin);
//        roles.add(rolUser);
//        user.setRoles(roles);
//
//        userService.save(user);



    }
}
