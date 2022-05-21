package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.services.MovieServiceImpl;
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
        Page<Movie> page = movieService.findPage(currentPage);
        model.addAttribute("title", "Movies");
        model.addAttribute("urlImage", URL_IMAGE);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", currentPage +1);
        model.addAttribute("prevPage", currentPage -1);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("movies", page.getContent());
        return "movies/movies-list";
    }

    /**
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public ModelAndView movieDetail(@PathVariable("id") Long id){
        Movie selectedMovie = movieService.findById(id);
        if(selectedMovie == null){
            log.info("Entra null");
            return new ModelAndView("redirect: /movies/");
        }
        ModelAndView mv = new ModelAndView("movies/movie-detail");
        mv.addObject("selectedMovie", selectedMovie);
        mv.addObject("title", selectedMovie.getTitle());
        mv.addObject("urlImage", URL_ORIGINAL_IMAGE);
        return mv;
    }

    @GetMapping("/addlist/{id}")
    public String addListToUser(@PathVariable("id") Long id){
        log.info("Id Peli: "+ id);

        return "index";
    }
}
