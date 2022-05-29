package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.ListType;
import com.fran.mymovies.entity.Role;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.entity.enums.ListTypeName;
import com.fran.mymovies.entity.enums.RoleName;
import com.fran.mymovies.services.IListTypeService;
import com.fran.mymovies.services.MovieServiceImpl;
import com.fran.mymovies.utils.Constants;
import com.fran.mymovies.utils.Utils;
import jdk.jshell.execution.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import com.fran.mymovies.services.RoleServiceImpl;
import com.fran.mymovies.services.UserServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Francisco David Manzanedo
 */

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private IListTypeService listTypeService;

    private User actualUser;

    @GetMapping("/login")
    public String login(Model model){
        actualUser = new User();
        log.info("Entra login");
        model.addAttribute("user", actualUser);
        return "user/login";
    }

    @RequestMapping("/signin")
    public ModelAndView signin(@Valid User user){
        actualUser = new User();
        ModelAndView mv = new ModelAndView("redirect:/movies/all/");
        Optional<User> userExists = userService.getUserByName(user.getUserName());
        if(userExists.isPresent()){
            String passwordEncoded = Utils.getMd5(user.getPassword());
            if(userExists.get().getPassword().equals(passwordEncoded)){
                actualUser = userExists.get();
            }else{
                mv.setViewName("user/login");
                mv.addObject(Constants.ERROR_LABEL, Constants.ERROR_INVALID_PASSWORD);
                return mv;
            }
        }else{
            mv.setViewName("user/login");
            mv.addObject(Constants.ERROR_LABEL, Constants.ERROR_PASS_USERNAME_INVALID);
            return mv;
        }
        return mv;

    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "user/registration";
    }

    @RequestMapping("/registration")
    public ModelAndView registration(@Valid User user){
        log.info("Parametro: "+user.getUserName());
        ModelAndView mv = new ModelAndView();
        User newUser = new User();

        Set<ListType> listTypes = new HashSet<>();
        listTypes.add(listTypeService.findByListTypeName(ListTypeName.FAVORITA).get());
        listTypes.add(listTypeService.findByListTypeName(ListTypeName.PENDIENTE).get());
        listTypes.add(listTypeService.findByListTypeName(ListTypeName.VISTA).get());
        newUser.setListTypes(listTypes);

        Role rolUser = roleService.getRoleByName(RoleName.ROLE_USER).get();
        Set<Role> roles = new HashSet<>();
        roles.add(rolUser);
        newUser.setRoles(roles);

        newUser.setPassword(Utils.getMd5(user.getPassword()));
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());

        Optional<User> optionalUser = userService.getUserByName(user.getUserName());
        if(optionalUser.isPresent()){
            mv.setViewName("user/registration");
            mv.addObject(Constants.ERROR_LABEL, Constants.ERROR_USERNAME_ALREADY_EXISTS);
            return mv;
        }else{
            newUser.setUserName(user.getUserName());
            userService.save(newUser);
        }

        log.info(user.getName());
        log.info(user.getUserName());
        log.info(user.getPassword());
        log.info(user.getEmail());
        user.getRoles().forEach(r-> log.info(r.getRoleName().name()));
        user.getListTypes().forEach(l-> log.info(l.getListTypeName().name()));
        user.getFavorite_movies().forEach(f-> log.info(f.getTitle()));

        mv.setViewName("user/login");
        mv.addObject("user",  newUser);
        mv.addObject(Constants.OK_REGISTER_LABEL, Constants.PLEASE_SIGNIN);
        return mv;
    }

}
