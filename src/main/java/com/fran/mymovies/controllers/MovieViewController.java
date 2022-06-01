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
    private MovieGenreImpl movieGenresService;

    //Para obtener el usuario que ha iniciado sesion
    @Autowired
    UserController userController;

    private Long id_selectedMovie;

    private int currentPageAux;

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
        actualUser = userController.getActualUser();
        model.addAttribute("user", actualUser);
        model.addAttribute("movies", movieService.findAll());
        model.addAttribute("moviesGenres", movieGenresService.findAll());
        return getOnePage(model, 1);
    }

    @GetMapping("/page/{pageNumber}")
    public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage){
        currentPageAux = currentPage;
        actualUser = userController.getActualUser();
        Page<Movie> page = movieService.findPage(currentPageAux);
        model.addAttribute("title", "Movies");
        model.addAttribute("urlImage", URL_IMAGE);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", currentPage + 1);
        model.addAttribute("prevPage", currentPage - 1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("movies", page.getContent());
        model.addAttribute("user", actualUser);
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
        mv.addObject("pageNumber", currentPageAux);
        mv.addObject("user", actualUser);
        return mv;
    }



    @GetMapping("filter/{id}")
    public String filterByGenre(Model model, @PathVariable("id") Long id){
        //List<Movie> allMoviesSortedByGenre = movieService.findAll(Sort.by());
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
        model.addAttribute("user", actualUser);
        model.addAttribute("movies",filterGenrePage.getContent());
        return "movies/movies-list";
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
                        log.info("Se añade a favorita");
                        userAux.getFavorite_movies().add(selectedMovieAux);
                        model.addAttribute(Constants.RESULT_LABEL, "Película "
                                .concat(selectedMovieAux.getTitle()).concat(" añadida a la lista de Favoritos"));
                        //SI la marca como favorita y no existe en vista, se añade a vista.
                        if(movieExistsWatchedList(selectedMovieAux, userAux)) {
                            log.info("Al añadir en favorita y no estar en vista, se añade a vista");
                            userAux.getWatched_movies().add(selectedMovieAux);
                        }

                    }else model.addAttribute(Constants.ERROR_LABEL, Constants.ERROR_MSG_MOVIE_EXISTS_FAVORITE);
                    break;

                //PENDIENTE
                case Constants.TYPE_LIST_PENDING:
                    if(!movieExistsPendingList(selectedMovieAux, userAux)){
                        //Si la quiere añadir a PENDIENTE, no debe existir en VISTA
                        log.info("Entra en pendiente");
                        if(movieExistsWatchedList(selectedMovieAux, userAux)) {
                            log.info("La película marcada como pendiente no existe en vista y se añade en pendiente");
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
                        log.info("Entra en vista y se añade");
                        userAux.getWatched_movies().add(selectedMovieAux);
                        model.addAttribute(Constants.RESULT_LABEL, "Película ".concat(selectedMovieAux.getTitle())
                                .concat(" añadida a la lista de Vistas"));

                        if(movieExistsPendingList(selectedMovieAux, userAux)) {
                            log.info("Si existe en pendiente, se elimina");
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
        userService.save(userAux);
        return movieDetail(selectedMovieAux.getId(), model);
    }

    /**
     * Comprueba si una película ya existe en la lista de Pendientes del usuario.
     * @param selectedMovie La Película seleccionada por el usuario.
     * @param actualUser El usuario autenticado.
     * @return Boolean, true si ya existe, false si no.
     */
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



    /**
     * Comprueba si una película ya existe en la lista de Favoritas del usuario.
     * @param selectedMovie La Película seleccionada por el usuario.
     * @param actualUser El usuario autenticado.
     * @return Boolean, true si ya existe, false si no.
     */
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


    /**
     * Comprueba si una película ya existe en la lista de Vista del usuario.
     * @param selectedMovie La Película seleccionada por el usuario.
     * @param actualUser El usuario autenticado.
     * @return Boolean, true si ya existe, false si no.
     */
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
