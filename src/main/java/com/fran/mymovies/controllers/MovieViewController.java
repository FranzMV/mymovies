package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.services.MovieServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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

    /**
     * Vista principal.
     * @param model Model.
     * @return ruta people/index.
     */
    @GetMapping("")
    public String indexMovies(Model model) {
        model.addAttribute("title", "Vista pelis");
        return "index";
    }

    @GetMapping("/listar")
    public String listarMovies(@PageableDefault(sort = "titulo", size = 5) Pageable pageable , Model model){

        model.addAttribute("title", "Lista de películas");
        model.addAttribute("urlImage", "https://image.tmdb.org/t/p/w500");
        model.addAttribute("movies", movieService.findAll());

        return "movies/movieslist";
    }
}
