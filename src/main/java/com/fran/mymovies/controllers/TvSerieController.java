package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.entity.TvSerie;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.services.IListTypeService;
import com.fran.mymovies.services.TvSerieServiceImpl;
import com.fran.mymovies.services.TvSeriesGenresImpl;
import com.fran.mymovies.services.UserServiceImpl;
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
    public ModelAndView tvSerieDetail(@PathVariable("id") Long id){
        TvSerie selectedTvSerie = tvSerieService.findById(id);
        selectedTvSerie.getListType().addAll(listTypeService.findAll());
        ModelAndView mv = new ModelAndView("series/tvserie-detail");
        mv.addObject("selectedTvSerie", selectedTvSerie);
        mv.addObject("title", selectedTvSerie.getTitle());
        mv.addObject("urlImage", URL_ORIGINAL_IMAGE);
        mv.addObject("user", actualUser);
        return mv;
    }

    @PostMapping("/addSerie")
    public ModelAndView addMovieToList(@Valid Movie selectedMovie){
        ModelAndView mv = new ModelAndView("redirect:/tvseries/all");
        log.info("Entra a añandir-------------------------");
        selectedMovie.getListType().forEach(l-> log.info(l.getListTypeName().name()));
        return mv;
    }
}
