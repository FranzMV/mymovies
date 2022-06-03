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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Pageable pageable = PageRequest.of(1 , 12);
        Page<Movie> filterGenrePage = new PageImpl<>(filterMoviesByGenre, pageable, filterMoviesByGenre.size());

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
                    addSelectedMovieToFavorite(selectedMovieAux, userAux, model);
                    break;
                //PENDIENTE
                case Constants.TYPE_LIST_PENDING:
                    addSelectedMovieToPending(selectedMovieAux, userAux, model);
                    break;
                //VISTA
                case Constants.TYPE_LIST_WATCHED:
                   addSelectedMovieToWatched(selectedMovieAux, userAux, model);
                   break;
                //DEFAULT
                default:
                    model.addAttribute(Constants.ERROR_LABEL, Constants.NO_LIST_SELECTED_ERROR);
                    break;
            }
        }
        userService.save(userAux);
        return movieDetail(selectedMovieAux.getId(), model);
    }

    @GetMapping("/user")
    public ModelAndView getUserProfile(){
        final User userAux = userService.getById(actualUser.getId()).get();
        ModelAndView mv =  new ModelAndView("user/profile");
        mv.addObject("user", userAux);
        mv.addObject("movies", movieService.findAll());
        mv.addObject("moviesGenres", movieGenresService.findAll());
        mv.addObject("totalFavoritesMovies", (long) userAux.getFavorite_movies().size());
        mv.addObject("totalWatchedMovies", (long) userAux.getWatched_movies().size());
        mv.addObject("totalPendingMovies", (long) userAux.getPending_movies().size());
        mv.addObject("totalFavoritesSeries", (long) userAux.getFavorite_tvSeries().size());
        mv.addObject("totalWatchedSeries", (long) userAux.getWatched_tvSeries().size());
        mv.addObject("totalPendingSeries", (long) userAux.getPending_tvSeries().size());
        return mv;
    }


    @GetMapping("/user-lists")
    public String getMoviesList(){
        return "user/lits-movies";
    }

    /**
     * Añade una película a la lista de Favoritas del usuario actual.
     * @param selectedMovie Película seleccionada por el usuario.
     * @param userAux Usuario actual.
     * @param model Model.
     */
    private void addSelectedMovieToFavorite(Movie selectedMovie, User userAux, Model model){
        if (!movieExistsFavoriteList(selectedMovie, userAux)) {
            log.info("Se añade a favorita");
            userAux.getFavorite_movies().add(selectedMovie);
            model.addAttribute(Constants.RESULT_LABEL, "Película "
                    .concat(selectedMovie.getTitle()).concat(" añadida a la lista de Favoritos"));
            //SI la marca como favorita y no existe en vista, se añade a vista.
            if(movieExistsWatchedList(selectedMovie, userAux)) {
                log.info("Al añadir en favorita y no estar en vista, se añade a vista");
                userAux.getWatched_movies().add(selectedMovie);
            }

        }else model.addAttribute(Constants.ERROR_LABEL, Constants.ERROR_MSG_MOVIE_EXISTS_FAVORITE);
    }

    /**
     * Añade una película a la lista de Pendientes del usuario actual.
     * @param selectedMovie Película seleccionada por el usuario.
     * @param userAux Usuario actual.
     * @param model Model.
     */
    private void addSelectedMovieToPending(Movie selectedMovie, User userAux, Model model){
        if(!movieExistsPendingList(selectedMovie, userAux)){
            //Si la quiere añadir a PENDIENTE, no debe existir en VISTA
            log.info("Entra en pendiente");
            if(movieExistsWatchedList(selectedMovie, userAux)) {
                log.info("La película marcada como pendiente no existe en vista y se añade en pendiente");
                userAux.getPending_movies().add(selectedMovie);
                model.addAttribute(Constants.RESULT_LABEL, "Película "
                        .concat(selectedMovie.getTitle()).concat(" añadida a la lista de Pendientes"));
            }
            else model.addAttribute(Constants.ERROR_LABEL,  Constants.ERROR_MSG_MOVIE_EXISTS_WATCHED);

        }else model.addAttribute(Constants.ERROR_LABEL,  Constants.ERROR_MSG_MOVIE_EXISTS_PENDING);
    }


    /**
     * Añade una película a la lista de Vistas del usuario actual.
     * @param selectedMovie Película seleccionada por el usuario.
     * @param userAux Usuario actual.
     * @param model Model.
     */
    private void addSelectedMovieToWatched(Movie selectedMovie, User userAux, Model model){
        if(movieExistsWatchedList(selectedMovie, userAux)){
            //Si la marca como vista y existe en pendiente, la elimina de pendiente
            log.info("Entra en vista y se añade");
            userAux.getWatched_movies().add(selectedMovie);
            model.addAttribute(Constants.RESULT_LABEL, "Película ".concat(selectedMovie.getTitle())
                    .concat(" añadida a la lista de Vistas"));

            if(movieExistsPendingList(selectedMovie, userAux)) {
                log.info("Si existe en pendiente, se elimina");
                userAux.getPending_movies().remove(selectedMovie);
                model.addAttribute(Constants.RESULT_LABEL, "Película ".concat(selectedMovie.getTitle())
                        .concat(" añadida a la lista de Vistas y eliminada de Pendientes"));
            }
        }else
            model.addAttribute(Constants.ERROR_LABEL, Constants.ERROR_MSG_MOVIE_EXISTS_WATCHED);

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
