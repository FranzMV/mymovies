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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    /**
     * Debe contener al menos un dígito [0-9].
     * Debe contener una letra minúscula [a-z].
     * Debe contener una letra mayúscula. [A-Z].
     * Debe contener al menos un tamaño mínimo de 4 caracteres y máximo de 20.
     */
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile( "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{4,20}$");

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);


    @GetMapping("/login")
    public String login(Model model){
        actualUser = new User();
        model.addAttribute("user", actualUser);
        return "user/login";
    }

    @PostMapping("/signin")
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

        mv.addObject("user", actualUser);
        return mv;

    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new User());
        return "user/registration";
    }

    @PostMapping("/registration")
    public ModelAndView registration(@Valid User user){
        ModelAndView mv = new ModelAndView();
        User newUser = new User();
        newUser.setListTypes(setUserListType());
        newUser.setRoles(setUserRole());
        newUser.setName(user.getName());

        //PASSWORD VALIDATION
        if(passwordIsValid(user.getPassword())){
            newUser.setPassword(Utils.getMd5(user.getPassword()));
        }else{
            mv.setViewName("user/registration");
            mv.addObject(Constants.ERROR_LABEL,
                    "La contraseña debe de contener al menos un dígito, una letra " +
                    "minúscula una letra mayúscula y un tamaño mínimo de 4 caracteres y máximo de 20.");
            return mv;
        }
        //EMAIL VALIDATION
        if(validateEmail(user.getEmail())){
            newUser.setEmail(user.getEmail());
        }else{
            mv.setViewName("user/registration");
            mv.addObject(Constants.ERROR_LABEL, "La dirección de email no parece correcta.");
            return mv;
        }

        Optional<User> optionalUser = userService.getUserByName(user.getUserName());
        if(optionalUser.isPresent()){
            mv.setViewName("user/registration");
            mv.addObject(Constants.ERROR_LABEL, Constants.ERROR_USERNAME_ALREADY_EXISTS);
            return mv;
        }else{
            newUser.setUserName(user.getUserName());
            userService.save(newUser);
        }

        mv.setViewName("user/login");
        mv.addObject("user",  newUser);
        mv.addObject(Constants.OK_REGISTER_LABEL, Constants.PLEASE_SIGNIN);
        return mv;
    }

    @GetMapping("/profile")
    public ModelAndView getUserProfile(){
        ModelAndView mv = new ModelAndView("user/profile");
        mv.addObject("user", actualUser);
        mv.addObject("totalFavoritesMovies", (long) actualUser.getFavorite_movies().size());
        mv.addObject("totalWatchedMovies", (long) actualUser.getWatched_movies().size());
        mv.addObject("totalPendingMovies", (long) actualUser.getPending_movies().size());
        mv.addObject("totalFavoritesSeries", (long) actualUser.getFavorite_tvSeries().size());
        mv.addObject("totalWatchedSeries", (long) actualUser.getWatched_tvSeries().size());
        mv.addObject("totalPendingSeries", (long) actualUser.getPending_tvSeries().size());
        return mv;
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model){
        User updatedUser = userService.getById(id).get();
        model.addAttribute("user", updatedUser);
        return "user/update-user";
    }


    @RequestMapping("/update")
    public ModelAndView updateUser(@Valid User user, BindingResult result, Model model){
        ModelAndView mv = new ModelAndView("user/update-user");
        User updatedUser = userService.getById(user.getId()).get();
        if(!result.hasErrors()){
            log.info("Editado: "+updatedUser.getUserName());

            if(passwordIsValid(user.getPassword())){
                updatedUser.setPassword(Utils.getMd5(user.getPassword()));
            }else{
                mv.setViewName("user/update-user");
                mv.addObject(Constants.ERROR_LABEL,
                        "La contraseña debe de contener al menos un dígito, una letra " +
                                "minúscula una letra mayúscula y un tamaño mínimo de 4 caracteres y máximo de 20.");
                return mv;
            }

            if(validateEmail(user.getEmail())){
                updatedUser.setEmail(user.getEmail());
            }else{
                mv.setViewName("user/update-user");
                mv.addObject(Constants.ERROR_LABEL, "La dirección de email no parece correcta.");
                return mv;
            }

            updatedUser.setUserName(user.getUserName());
            updatedUser.setName(user.getName());
            mv.setViewName("user/profile");
            mv.addObject("okRegister", "Los datos de su perfil se han actualizado");
            mv.addObject("user", updatedUser);
            mv.addObject("totalFavoritesMovies", (long) actualUser.getFavorite_movies().size());
            mv.addObject("totalWatchedMovies", (long) actualUser.getWatched_movies().size());
            mv.addObject("totalPendingMovies", (long) actualUser.getPending_movies().size());
            mv.addObject("totalFavoritesSeries", (long) actualUser.getFavorite_tvSeries().size());
            mv.addObject("totalWatchedSeries", (long) actualUser.getWatched_tvSeries().size());
            mv.addObject("totalPendingSeries", (long) actualUser.getPending_tvSeries().size());
            userService.save(updatedUser);
        }
        return mv;
    }


    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id, Model model){
        userService.deleteById(id);
        model.addAttribute("okRegister", "Su cuenta ha sido eliminada.");
        model.addAttribute("user", actualUser);
        return "user/login";
    }

    private Set<Role> setUserRole(){
        Role rolUser = roleService.getRoleByName(RoleName.ROLE_USER).get();
        Set<Role> roles = new HashSet<>();
        roles.add(rolUser);
        return roles;
    }

    private Set<ListType> setUserListType(){
        Set<ListType> listTypes = new HashSet<>();
        listTypes.add(listTypeService.findByListTypeName(ListTypeName.FAVORITA).get());
        listTypes.add(listTypeService.findByListTypeName(ListTypeName.PENDIENTE).get());
        listTypes.add(listTypeService.findByListTypeName(ListTypeName.VISTA).get());
        return listTypes;
    }

    private static boolean passwordIsValid(final String password) {
        Matcher matcher = PASSWORD_PATTERN.matcher(password);
        return matcher.find();
    }

    private static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
