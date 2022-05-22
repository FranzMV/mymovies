package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.Role;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.entity.enums.ListTypeName;
import com.fran.mymovies.entity.enums.RoleName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.fran.mymovies.services.RoleServiceImpl;
import com.fran.mymovies.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;


    @GetMapping("/register")
    public String register(Model model){
        log.info("Entra register");
        model.addAttribute("user", new User());
        return "user/registration";
    }

    @RequestMapping("/registration")
    public ModelAndView registration(@Valid User user){
        log.info("Entra en registration");
        ModelAndView mv = new ModelAndView();
        user.setListType(ListTypeName.FAVORITA);
        user.setListType(ListTypeName.PENDIENTE);
        user.setListType(ListTypeName.VISTA);
        Set<Role> roles = new HashSet<>();
        roles.add(new Role(roleService.getRoleByName(RoleName.ROLE_USER).get().getRoleName()));
        mv.addObject("user", user);
        log.info("Recibido: "+user);

//        if(StringUtils.isBlank(name)){
//            mv.setViewName("user/registration");
//            mv.addObject("error", "El campo nombre no puede quedar vacío.");
//            return mv;
//        }
//        if(StringUtils.isBlank(password)){
//            mv.setViewName("user/registration");
//            mv.addObject("error", "la contraseña no puede estar vacía");
//            return mv;
//        }
//        if(userService.existsByName(userName)){
//            mv.setViewName("user/registration");
//            mv.addObject("error", "ese nombre de usuario ya existe");
//            return mv;
//        }
//        if(!password.equals(password2)){
//            mv.setViewName("user/registration");
//            mv.addObject("error", "Las contraseñas no son iguales");
//            return mv;
//        }
//        if(StringUtils.isBlank(email)){
//            mv.setViewName("user/registration");
//            mv.addObject("error", "El campo email no puede quedar vacío.");
//            return mv;
//        }

           //userService.save(user);
            mv.setViewName("user/login");
//        mv.addObject("registroOK", "Cuenta creada, " + newUser.getUserName() + ", ya puedes iniciar sesión");
//        log.info("Insertado usuario: ".concat(newUser.getUserName()));
            mv.setViewName("user/login");
        return mv;
    }
}
