package com.fran.mymovies.controllers;
import com.fran.mymovies.entity.ListType;
import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.services.IListTypeService;
import com.fran.mymovies.services.MovieGenreImpl;
import com.fran.mymovies.services.MovieServiceImpl;
import com.fran.mymovies.services.UserServiceImpl;
import com.fran.mymovies.utils.Constants;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Francisco David Manzanedo.
 */
@Getter
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
    MovieGenreImpl movieGenresService;

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
        model.addAttribute("moviesGenres", movieGenresService.findAll());
        Page<Movie> page = movieService.findPage(1);
        return getOnePage(model, 1);
    }



    @GetMapping("/page/{pageNumber}")
    public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage){
        log.info("Current Page:"+currentPage );
        Page<Movie> page = movieService.findPage(currentPage);
            model.addAttribute("title", "Movies");
            model.addAttribute("urlImage", URL_IMAGE);
            model.addAttribute("currentPage", currentPage);
            model.addAttribute("nextPage", currentPage + 1);
            model.addAttribute("prevPage", currentPage - 1);
            model.addAttribute("totalPages", page.getTotalPages());
            model.addAttribute("totalItems", page.getTotalElements());
            model.addAttribute("movies", page.getContent());
            model.addAttribute("user", actualUser.getUserName());
            return "movies/movies-list";
    }

    @GetMapping("filter/{id}")
    public String filterByGenre(Model model, @PathVariable("id") Long id){
        List<Movie> filterMoviesByGenre = movieService.findAll()
                .stream()
                .filter(movies-> movies.getGenres()
                        .stream()
                        .anyMatch(genre-> genre.getId().equals(id)))
                .collect(Collectors.toList());
        Page<Movie> filterGenrePage = new PageImpl<>(filterMoviesByGenre);

        model.addAttribute("moviesGenres", movieGenresService.findAll());
        model.addAttribute("title", "Movies");
        model.addAttribute("urlImage", URL_IMAGE);
        model.addAttribute("totalPages", filterGenrePage.getTotalPages());
        model.addAttribute("totalItems", filterGenrePage.getTotalElements());
        model.addAttribute("user", actualUser.getUserName());
        model.addAttribute("movies",filterGenrePage.getContent());
        return "movies/movies-list";
    }



    @GetMapping("/detail/{id}")
    public ModelAndView movieDetail(@PathVariable("id") Long id, Model model){
        Movie selectedMovie = movieService.findById(id);
        selectedMovie.getListType().addAll((listTypeService.findAll()));
        id_selectedMovie = id;
        ModelAndView mv = new ModelAndView("movies/movie-detail");
        mv.addObject("selectedMovie", selectedMovie);
        mv.addObject("title", selectedMovie.getTitle());
        mv.addObject("urlImage", URL_ORIGINAL_IMAGE);
        mv.addObject("user", actualUser);
        return mv;
    }


    @PostMapping("/addMovie")
    public ModelAndView addMovieToList(@Valid Movie selectedMovie, Model model){
        final Movie selectedMovieAux = movieService.findById(id_selectedMovie);
        final User userAux = userService.getById(actualUser.getId()).get();

        for (ListType l: selectedMovie.getListType()) {
            switch (l.getListTypeName().name()){
                //FAVORITA
                case Constants.TYPE_LIST_FAVORITE:
                    if (!movieExistsFavoriteList(selectedMovieAux, userAux)) {
                        userAux.getFavorite_movies().add(selectedMovieAux);
                        model.addAttribute(Constants.RESULT_LABEL, "Película "
                                .concat(selectedMovieAux.getTitle()).concat(" añadida a la lista de Favoritos"));
                        //SI la marca como favorita y no existe en vista, se añade a vista.
                        if(movieExistsWatchedList(selectedMovieAux, userAux)) {
                            userAux.getWatched_movies().add(selectedMovieAux);
                        }

                    }else
                        model.addAttribute(Constants.ERROR_LABEL, Constants.ERROR_MSG_MOVIE_EXISTS_FAVORITE);
                    break;

                //PENDIENTE
                case Constants.TYPE_LIST_PENDING:
                    if(!movieExistsPendingList(selectedMovieAux, userAux)){
                        //Si la quiere añadir a PENDIENTE, no debe existir en VISTA
                        if(movieExistsWatchedList(selectedMovieAux, userAux)) {
                            userAux.getPending_movies().add(selectedMovieAux);
                            model.addAttribute(Constants.RESULT_LABEL, "Película "
                                    .concat(selectedMovieAux.getTitle()).concat(" añadida a la lista de Favoritos"));
                        }
                        else
                            model.addAttribute(Constants.ERROR_LABEL,  Constants.ERROR_MSG_MOVIE_EXISTS_WATCHED);
                    }else
                        model.addAttribute(Constants.ERROR_LABEL,  Constants.ERROR_MSG_MOVIE_EXISTS_PENDING);
                    break;

                //VISTA
                case Constants.TYPE_LIST_WATCHED:
                    if(movieExistsWatchedList(selectedMovieAux, userAux)){
                        //Si la marca como vista y existe en pendiente, la elimina de pendiente
                        userAux.getWatched_movies().add(selectedMovieAux);
                        model.addAttribute(Constants.RESULT_LABEL, "Película ".concat(selectedMovieAux.getTitle())
                                .concat(" añadida a la lista de Vistas"));

                        if(movieExistsPendingList(selectedMovieAux, userAux)) {
                            userAux.getPending_movies().remove(selectedMovieAux);
                            model.addAttribute(Constants.RESULT_LABEL, "Película ".concat(selectedMovieAux.getTitle())
                                    .concat(" añadida a la lista de Vistas y eliminada de Pendientes"));
                        }
                    }else
                        model.addAttribute(Constants.ERROR_LABEL, Constants.ERROR_MSG_MOVIE_EXISTS_WATCHED);
                    break;

                default:
                    model.addAttribute(Constants.ERROR_LABEL, Constants.NO_LIST_SELECTED_ERROR);
                    break;
            }
        }
        //id_selectedMovie = 0L;
        userService.save(userAux);
        return movieDetail(selectedMovieAux.getId(), model);
    }

    private static boolean movieExistsPendingList(Movie selectedMovie, User actualUser){
        boolean result = false;
        for(Movie m : actualUser.getPending_movies()){
            if (m.getId().equals(selectedMovie.getId())) {
                result = true;
                break;
            }
        }
        return result;
    }

    private static boolean movieExistsFavoriteList(Movie selectedMovie, User actualUser){
        boolean result = false;
        for(Movie m : actualUser.getFavorite_movies()){
            if (m.getId().equals(selectedMovie.getId())) {
                result = true;
                break;
            }
        }
        return result;
    }

    private static boolean movieExistsWatchedList(Movie selectedMovie, User actualUser){
        boolean result = false;
        for(Movie m : actualUser.getWatched_movies()){
            if (m.getId().equals(selectedMovie.getId())) {
                result = true;
                break;
            }
        }
        return !result;
    }
}
