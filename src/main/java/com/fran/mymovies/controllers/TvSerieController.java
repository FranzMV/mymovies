package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.ListType;
import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.entity.TvSerie;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.services.IListTypeService;
import com.fran.mymovies.services.TvSerieServiceImpl;
import com.fran.mymovies.services.TvSeriesGenresImpl;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Francisco David Manzanedo.
 * */

@Getter
@NoArgsConstructor
@Slf4j
@Controller
@RequestMapping("/tvseries")
public class TvSerieController {

    @Autowired
    private TvSerieServiceImpl tvSerieService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private TvSeriesGenresImpl tvSeriesGenresService;

    @Autowired
    private IListTypeService listTypeService;

    @Autowired
    private UserController userController;

    private Long id_selectedSerie;

    private User actualUser;

    private int currentPageAux;

    private final String URL_IMAGE ="https://image.tmdb.org/t/p/w500";

    private final String URL_ORIGINAL_IMAGE ="https://image.tmdb.org/t/p/w500";


    @GetMapping("/all")
    public String getAllSeries(Model model) {
        actualUser = userController.getActualUser();
        model.addAttribute("user", actualUser);
        model.addAttribute("tvGenres",tvSeriesGenresService.findAll());
        return getOnePage(model, 1);
    }


    @GetMapping("/page/{pageNumber}")
    public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage){
        currentPageAux = currentPage;
        Page<TvSerie> page = tvSerieService.findPage(currentPage);
        model.addAttribute("title", "TV Series");
        model.addAttribute("urlImage", URL_IMAGE);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", currentPage +1);
        model.addAttribute("prevPage", currentPage -1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("tvseries", page.getContent());
        model.addAttribute("tvGenres",tvSeriesGenresService.findAll());
        model.addAttribute("user", actualUser);
        return "series/tvseries-list";
    }

    @GetMapping("/detail/{id}")
    public ModelAndView tvSerieDetail(@PathVariable("id") Long id, Model model){
        TvSerie selectedTvSerie = tvSerieService.findById(id);
        selectedTvSerie.getListType().addAll(listTypeService.findAll());
        id_selectedSerie = id;
        ModelAndView mv = new ModelAndView("series/tvserie-detail");
        mv.addObject("selectedTvSerie", selectedTvSerie);
        mv.addObject("title", selectedTvSerie.getTitle());
        mv.addObject("urlImage", URL_ORIGINAL_IMAGE);
        mv.addObject("pageNumber", currentPageAux);
        mv.addObject("user", actualUser);
        return mv;
    }

    @GetMapping("filter/{id}")
    public String filterByGenre(Model model, @PathVariable("id") Long id){
        log.info("Entra con id:"+id);
        List<TvSerie> filterTvSeriesByGenre = tvSerieService.findAll()
                .stream()
                .filter(tvSerie-> tvSerie.getGenres()
                        .stream()
                        .anyMatch(genre-> genre.getId().equals(id)))
                .collect(Collectors.toList());

        Page<TvSerie> filterGenrePage = new PageImpl<>(filterTvSeriesByGenre);

        model.addAttribute("tvSeriesGenres", tvSerieService.findAll());
        model.addAttribute("title", "TvSeries");
        model.addAttribute("urlImage", URL_IMAGE);
        model.addAttribute("totalPages", filterGenrePage.getTotalPages());
        model.addAttribute("totalItems", filterGenrePage.getTotalElements());
        model.addAttribute("user", actualUser);
        model.addAttribute("tvseries",filterGenrePage.getContent());
        return "series/tvseries-list";
    }


    @PostMapping("/addSerie")
    public ModelAndView addMovieToList(@Valid Movie selectedMovie, Model model){
        final TvSerie selectedSerieAux = tvSerieService.findById(id_selectedSerie);
        final User userAxu = userService.getById(actualUser.getId()).get();

        for(ListType l : selectedMovie.getListType()){
            switch (l.getListTypeName().name()){
                //FAVORITA
                case Constants.TYPE_LIST_FAVORITE:
                    if(!tvSerieExistsFavoriteList(selectedSerieAux, userAxu)){
                        log.info("Se añade a favorita");
                        userAxu.getFavorite_tvSeries().add(selectedSerieAux);
                        model.addAttribute(Constants.RESULT_LABEL, "Serie "
                                .concat(selectedSerieAux.getTitle()).concat(" añadida a la lista de Favoritos"));
                        //SI la marca como favorita y no existe en vista, se añade a vista.
                        if(!tvSerieExistsWatchedList(selectedSerieAux, userAxu)){
                            log.info("Al añadir en favorita y no estar en vista, se añade a vista");
                            userAxu.getWatched_tvSeries().add(selectedSerieAux);
                        }

                    }else  model.addAttribute(Constants.ERROR_LABEL, Constants.ERROR_MSG_SERIE_EXISTS_FAVORITE);

                    break;

                //PENDIENTE
                case Constants.TYPE_LIST_PENDING:
                    if(!tvSerieExistsPendingList(selectedSerieAux, userAxu)){
                        //Si la quiere añadir a PENDIENTE, no debe existir en VISTA
                        log.info("Entra en pendiente");
                        if(!tvSerieExistsWatchedList(selectedSerieAux, userAxu)){
                            userAxu.getPending_tvSeries().add(selectedSerieAux);
                            model.addAttribute(Constants.RESULT_LABEL, "Serie "
                                    .concat(selectedSerieAux.getTitle()).concat(" añadida a la lista de Pendientes"));
                        }else{
                            model.addAttribute(Constants.ERROR_LABEL,  Constants.ERROR_MSG_SERIE_EXISTS_WATCHED);
                        }
                    }else
                        model.addAttribute(Constants.ERROR_LABEL,  Constants.ERROR_MSG_SERIE_EXISTS_PENDING);
                    break;

                //VISTA
                case Constants.TYPE_LIST_WATCHED:
                    if(!tvSerieExistsWatchedList(selectedSerieAux, userAxu)){
                        //Si la marca como vista y existe en pendiente, la elimina de pendiente
                        log.info("Entra en vista y se añade");
                        userAxu.getWatched_tvSeries().add(selectedSerieAux);
                        model.addAttribute(Constants.RESULT_LABEL, "Serie "
                                .concat(selectedSerieAux.getTitle()).concat(" añadida a la lista de Vistas"));
                        if(tvSerieExistsPendingList(selectedSerieAux, userAxu)){
                            log.info("Si existe en pendiente, se elimina");
                            userAxu.getPending_tvSeries().remove(selectedSerieAux);
                        }

                    }else model.addAttribute(Constants.ERROR_LABEL,  Constants.ERROR_MSG_SERIE_EXISTS_WATCHED);
                    break;

                default: model.addAttribute(Constants.ERROR_LABEL, Constants.NO_LIST_SELECTED_ERROR);
                    break;
            }
        }
        userService.save(userAxu);
        return tvSerieDetail(selectedSerieAux.getId(), model);
    }
    @GetMapping("/user")
    public ModelAndView getUserProfile(){
        final User userAux = userService.getById(actualUser.getId()).get();
        ModelAndView mv =  new ModelAndView("user/profile");
        mv.addObject("user", userAux);
        mv.addObject("series", tvSerieService.findAll());
        mv.addObject("seriesGenres", tvSeriesGenresService.findAll());
        mv.addObject("totalFavoritesMovies", (long) userAux.getFavorite_movies().size());
        mv.addObject("totalWatchedMovies", (long) userAux.getWatched_movies().size());
        mv.addObject("totalPendingMovies", (long) userAux.getPending_movies().size());
        mv.addObject("totalFavoritesSeries", (long) userAux.getFavorite_tvSeries().size());
        mv.addObject("totalWatchedSeries", (long) userAux.getWatched_tvSeries().size());
        mv.addObject("totalPendingSeries", (long) userAux.getPending_tvSeries().size());
        return mv;
    }

    /**
     * Comprueba si una serie ya existe en la lista de Pendientes del usuario.
     * @param selectedSerie La Serie seleccionada por el usuario.
     * @param actualUser El usuario autenticado.
     * @return Boolean, true si ya existe, false si no.
     */
    private static boolean tvSerieExistsPendingList(TvSerie selectedSerie, User actualUser){
        boolean result = false;
        for(TvSerie tvSerie: actualUser.getPending_tvSeries()){
            if (tvSerie.getId().equals(selectedSerie.getId())) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Comprueba si una serie ya existe en la lista de Favoritas del usuario.
     * @param selectedSerie La Serie seleccionada por el usuario.
     * @param actualUser El usuario autenticado.
     * @return Boolean, true si ya existe, false si no.
     */
    private static boolean tvSerieExistsFavoriteList(TvSerie selectedSerie, User actualUser){
        boolean result = false;
        for(TvSerie tvSerie: actualUser.getFavorite_tvSeries()){
            if (tvSerie.getId().equals(selectedSerie.getId())) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Comprueba si una serie ya existe en la lista de Vistas del usuario.
     * @param selectedSerie La Serie seleccionada por el usuario.
     * @param actualUser El usuario autenticado.
     * @return Boolean, true si ya existe, false si no.
     */
    private static boolean tvSerieExistsWatchedList(TvSerie selectedSerie, User actualUser){
        boolean result = false;
        for(TvSerie tvSerie: actualUser.getWatched_tvSeries()){
            if (tvSerie.getId().equals(selectedSerie.getId())) {
                result = true;
                break;
            }
        }
        return result;
    }
}
