package com.fran.mymovies.controllers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/registration")
    public String register(){
        return "user/registration";
    }
}
