package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.ListType;
import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.entity.MovieGenre;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.entity.enums.ListTypeName;
import com.fran.mymovies.services.IListTypeService;
import com.fran.mymovies.services.MovieGenreImpl;
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

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private IListTypeService listTypeService;

    @Autowired
    MovieGenreImpl movieGenreImpl;

    //Para obtener el usuario que ha iniciado sesion
    @Autowired
    UserController userController;

    private Long id_selectedMovie;

    private User actualUser;

    private final String URL_IMAGE ="https://image.tmdb.org/t/p/w500";
    private final String URL_ORIGINAL_IMAGE ="https://image.tmdb.org/t/p/w500";


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
        model.addAttribute("movies",movieService.findAll());
        model.addAttribute("moviesGenres", movieGenreImpl.findAll());

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
        selectedMovie.getListType().addAll((listTypeService.findAll()));
        id_selectedMovie = id;
        if(selectedMovie == null){
            return new ModelAndView("redirect: /movies/all");
        }
        ModelAndView mv = new ModelAndView("movies/movie-detail");
        mv.addObject("selectedMovie", selectedMovie);
        mv.addObject("title", selectedMovie.getTitle());
        mv.addObject("urlImage", URL_ORIGINAL_IMAGE);
        mv.addObject("user", actualUser);


        return mv;
    }

    @RequestMapping("/addMovie")
    public ModelAndView addMovieToList(@Valid Movie selectedMovie){
        final Movie selectedMovieAux = movieService.findById(id_selectedMovie);
        final User userAux = userService.getById(actualUser.getId()).get();
        log.info("Usuario al que se a;ade: "+userAux);
        log.info(selectedMovieAux.getTitle());
        selectedMovie.getListType().forEach(l-> {
            if(l.getId().equals(1L)){
                log.info("Seleccionado favorita");
                userAux.getFavorite_movies().add(selectedMovieAux);
            }else if(l.getId().equals(2L)){
                log.info("Seleccionado pendiente");
                userAux.getPending_movies().add(selectedMovieAux);
            }else {
                log.info("Seleccionado vista");
                userAux.getWatched_movies().add(selectedMovieAux);
            }
            userAux.getFavorite_movies().forEach(m-> log.info("Añadida "+m.getTitle()));
            userService.save(userAux);
        });

        if(id_selectedMovie == null){
            return new ModelAndView("redirect:/movies/all");
        }
        ModelAndView mv = new ModelAndView("redirect:/movies/all");
        id_selectedMovie = 0L;
        return mv;
    }
}
