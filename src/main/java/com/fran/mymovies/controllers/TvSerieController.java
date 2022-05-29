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

    private final String URL_IMAGE ="https://image.tmdb.org/t/p/w500";

    private final String URL_ORIGINAL_IMAGE ="https://image.tmdb.org/t/p/w500";


    @GetMapping("/all")
    public String getAllSeries(Model model) {
        actualUser = userController.getActualUser();
        model.addAttribute("user", actualUser.getUserName());
        model.addAttribute("tvGenres",tvSeriesGenresService.findAll());
        return getOnePage(model, 1);
    }


    @GetMapping("/page/{pageNumber}")
    public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage){
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
        model.addAttribute("user", actualUser.getUserName());
        return "series/tvseries-list";
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
        model.addAttribute("user", actualUser.getUserName());
        model.addAttribute("tvseries",filterGenrePage.getContent());
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
        mv.addObject("user", actualUser);
        return mv;
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
                        //SI la marca como favorita y no existe en vista, se añade a vista.
                        if(tvSerieExistsWatchedList(selectedSerieAux, userAxu)){
                            userAxu.getWatched_tvSeries().add(selectedSerieAux);
                            model.addAttribute(Constants.RESULT_LABEL, "Serie "
                                    .concat(selectedSerieAux.getTitle()).concat(" añadida a la lista de Favoritos"));
                        }else{
                            userAxu.getFavorite_tvSeries().add(selectedSerieAux);
                            model.addAttribute(Constants.RESULT_LABEL, "Serie "
                                    .concat(selectedSerieAux.getTitle()).concat(" añadida a la lista de Favoritos"));
                        }
                    }else  model.addAttribute(Constants.ERROR_LABEL, Constants.ERROR_MSG_SERIE_EXISTS_FAVORITE);

                    break;

                //PENDIENTE
                case Constants.TYPE_LIST_PENDING:
                    if(!tvSerieExistsPendingList(selectedSerieAux, userAxu)){
                        //Si la quiere añadir a PENDIENTE, no debe existir en VISTA
                        if(tvSerieExistsWatchedList(selectedSerieAux, userAxu)){
                            userAxu.getWatched_tvSeries().add(selectedSerieAux);
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
                    if(tvSerieExistsWatchedList(selectedSerieAux, userAxu)){
                        //Si la marca como vista y existe en pendiente, la elimina de pendiente
                        if(tvSerieExistsPendingList(selectedSerieAux, userAxu)){
                            userAxu.getPending_tvSeries().remove(selectedSerieAux);
                            model.addAttribute(Constants.RESULT_LABEL, "Serie "
                                    .concat(selectedSerieAux.getTitle()).concat(" añadida a la lista de Vistas y " +
                                            "eliminada de Pendientes"));
                        }else{
                            userAxu.getWatched_tvSeries().add(selectedSerieAux);
                            model.addAttribute(Constants.RESULT_LABEL, "Serie "
                                    .concat(selectedSerieAux.getTitle()).concat(" añadida a la lista de Vistas"));
                        }
                    }else
                        model.addAttribute(Constants.ERROR_LABEL,  Constants.ERROR_MSG_SERIE_EXISTS_WATCHED);
                    break;

                default:
                    model.addAttribute(Constants.ERROR_LABEL, Constants.NO_LIST_SELECTED_ERROR);
                    break;

            }
        }
        return tvSerieDetail(selectedSerieAux.getId(), model);
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
