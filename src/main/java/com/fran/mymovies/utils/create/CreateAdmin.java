package com.fran.mymovies.utils.create;

import com.fran.mymovies.entity.ListType;
import com.fran.mymovies.entity.Role;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.entity.enums.ListTypeName;
import com.fran.mymovies.entity.enums.RoleName;
import com.fran.mymovies.services.*;
import com.fran.mymovies.utils.Utils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Clase encargada de crear el usuario administrador.
 * @author Francisco David Manzanedo Valle.
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Service
public class CreateAdmin implements CommandLineRunner {

    @Autowired
    private IUserService userService;

    @Autowired
    private IRoleService roleService;

    @Autowired
    private IListTypeService listTypeService;

    @Override
    public void run(String... args) throws Exception {
//        User user = new User();
//        user.setUserName("admin");
//        user.setName("admin");
//        String passwordEncoded = Utils.getMd5("1234");
//        user.setPassword(passwordEncoded);
//
//        ListType favorita = listTypeService.findByListTypeName(ListTypeName.FAVORITA).get();
//        ListType pendiente = listTypeService.findByListTypeName(ListTypeName.PENDIENTE).get();
//        ListType vista = listTypeService.findByListTypeName(ListTypeName.VISTA).get();
//        Set<ListType> listTypes = new HashSet<>();
//        listTypes.add(favorita);
//        listTypes.add(pendiente);
//        listTypes.add(vista);
//        user.setListTypes(listTypes);
//
//        Role rolAdmin = roleService.findByRoleName(RoleName.ROLE_ADMIN).get();
//        Role rolUser = roleService.findByRoleName(RoleName.ROLE_USER).get();
//        Set<Role> roles = new HashSet<>();
//        roles.add(rolAdmin);
//        roles.add(rolUser);
//        user.setRoles(roles);

//        userService.save(user);

    }
}
