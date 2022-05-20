package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.TvSerie;
import com.fran.mymovies.services.TvSerieServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Francisco David Manzanedo.
 * */

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Controller
@RequestMapping("/tvseries")
public class TvSerieController {

    @Autowired
    private TvSerieServiceImpl tvSerieService;

    private final String URL_IMAGE ="https://image.tmdb.org/t/p/w500";

    private final String URL_ORIGINAL_IMAGE ="https://image.tmdb.org/t/p/original";

    /**
     * Vista principal.
     * @param model Model.
     * @return ruta people/index.
     */
    @GetMapping("/")
    public String getAllMovies(Model model) {
        return getOnePage(model, 1);
    }


    /**
     *
     * @param model
     * @param currentPage
     * @return
     */
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

        return "series/tvseries-list";
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public ModelAndView tvSerieDetail(@PathVariable("id") Long id){
        TvSerie selectedTvSerie = tvSerieService.findById(id);

        if(selectedTvSerie== null){
            log.info("Entra null");
            return new ModelAndView(" /tvseries/");
        }
        ModelAndView mv = new ModelAndView("series/tvserie-detail");
        mv.addObject("selectedTvSerie", selectedTvSerie);
        mv.addObject("title", selectedTvSerie.getTitle());
        mv.addObject("urlImage", URL_ORIGINAL_IMAGE);
       // mv.addObject("currentPage", page.getNumber());
        log.info(selectedTvSerie.getPoster_path());
        //log.info(String.valueOf(page.getNumber()));
        return mv;
    }
}
