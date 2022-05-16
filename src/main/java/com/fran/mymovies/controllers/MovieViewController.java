package com.fran.mymovies.controllers;

import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.services.MovieServiceImpl;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


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

    /**
     * Vista principal.
     * @param model Model.
     * @return ruta people/index.
     */
    @GetMapping("/")
    public String getAllMovies(Model model) {
        return getOnePage(model, 1);
    }

//
//    @GetMapping(value = "all")
//    public String getAll(@RequestParam Map<String, Object> params, Model model){
//        int page = params.get("page") !=null ? (Integer.parseInt(params.get("page").toString())-1) : 0;
//        PageRequest pageRequest = PageRequest.of(page, 10 );
//        Page<Movie> moviesPage = movieService.getAll(pageRequest);
//        int totalPages = moviesPage.getTotalPages();
//        if(totalPages > 0){
//            List<Integer> pages = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
//            model.addAttribute("pages", pages);
//
//        }
//        model.addAttribute("title", "Movies");
//        model.addAttribute("urlImage", URL_IMAGE);
//        model.addAttribute("movies", moviesPage.getContent());
//        model.addAttribute("totalPages", totalPages);
//        return "movies/movieslist";
//    }

    @GetMapping("/page/{pageNumber}")
    public String getOnePage(Model model, @PathVariable("pageNumber") int currentPage){
        Page<Movie> page = movieService.findPage(currentPage);
        int totalPages = page.getTotalPages();
        long totalItems = page.getTotalElements();
        List<Movie> movies = page.getContent();

        model.addAttribute("title", "Movies");
        model.addAttribute("urlImage", URL_IMAGE);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("nextPage", currentPage +1);
        model.addAttribute("prevPage", currentPage -1);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("movies", movies);
        return "movies/movieslist";
    }

}
