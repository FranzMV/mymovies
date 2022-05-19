package com.fran.mymovies.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/mymovies")
public class IndexController {

    @GetMapping({"/index"})
    public String index(Model model)
    {
        model.addAttribute("title", "MyMovies");
        return "/movies/moviesList";
    }

    @GetMapping({"/login"})
    public String login(Model model)
    {
        model.addAttribute("title", "My Movies");
        return "/user/login";
    }

    @GetMapping({"/registration"})
    public String registration(Model model)
    {
        model.addAttribute("title", "My Movies");
        return "/user/registration";
    }
}
