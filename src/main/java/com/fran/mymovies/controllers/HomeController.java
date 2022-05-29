package com.fran.mymovies.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Francisco David Manzanedo
 */
@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping(value = {"/", "/index"})
    public String index(){
        return "index";
    }

    @GetMapping("/login")
    public String login(){
        return "user/login";
    }

    @GetMapping("/registration")
    public String registration(){
        return "user/register";
    }

//    @GetMapping("/forbidden")
//    public String forbidden(){
//        return "/errors/forbidden";
//    }
}
