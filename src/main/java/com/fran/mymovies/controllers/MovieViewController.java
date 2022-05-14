package com.fran.mymovies.controllers;

import com.fran.mymovies.services.MovieServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Getter
@Setter
@NoArgsConstructor
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
}
