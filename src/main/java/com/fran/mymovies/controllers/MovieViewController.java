package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.services.MovieServiceImpl;
import com.fran.mymovies.services.UserServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Francisco David Manzanedo.
 */
@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Controller
@RequestMapping("/movies")
public class MovieViewController {

    @Autowired
    private MovieServiceImpl movieService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    UserController userController;

    private final String URL_IMAGE ="https://image.tmdb.org/t/p/w500";
    private final String URL_ORIGINAL_IMAGE ="https://image.tmdb.org/t/p/w500";

    private User actualUser;

    /**
     * Vista principal.
     * @param model Model.
     * @return ruta people/index.
     */
    @GetMapping("/all")
    public String getAllMovies(Model model) {
        log.info("Pasando usuario: "+userController.getActualUser().getUserName());
        actualUser = userController.getActualUser();
        model.addAttribute("user", actualUser.getUserName());
        return getOnePage(model, 1);
    }



    @GetMapping("/page/{pageNumber}")
    public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage){
        Page<Movie> page = movieService.findPage(currentPage);
        model.addAttribute("title", "Movies");
        model.addAttribute("urlImage", URL_IMAGE);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", currentPage +1);
        model.addAttribute("prevPage", currentPage -1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("movies", page.getContent());
        model.addAttribute("user", actualUser.getUserName());
        return "movies/movies-list";
    }


    @GetMapping("/detail/{id}")
    public ModelAndView movieDetail(@PathVariable("id") Long id){
        Movie selectedMovie = movieService.findById(id);
        if(selectedMovie == null){
            log.info("Entra null");
            return new ModelAndView("redirect: /movies/all");
        }
        ModelAndView mv = new ModelAndView("movies/movie-detail");
        mv.addObject("selectedMovie", selectedMovie);
        mv.addObject("title", selectedMovie.getTitle());
        mv.addObject("urlImage", URL_ORIGINAL_IMAGE);
        //mv.addObject("user", actualUser.getUserName());
        return mv;
    }
}
