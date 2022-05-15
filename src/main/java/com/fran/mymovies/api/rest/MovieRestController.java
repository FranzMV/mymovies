package com.fran.mymovies.api.rest;

import com.fran.mymovies.dto.MovieDTO;
import com.fran.mymovies.entity.Movie;
import com.fran.mymovies.services.MovieServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Francisco David Manzanedo.
 */

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/v1")
public class MovieRestController {

    @Autowired
    private  MovieServiceImpl movieService;


    @GetMapping("/movies")
    public List<MovieDTO> index(){

        List<Movie> movies = movieService.findAll();
        List<MovieDTO> moviesDTO = new ArrayList<>();
        movies.forEach(m-> moviesDTO.add(getMovieDTO(m)));
        return  moviesDTO;
    }

    @GetMapping("/movies/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        Movie movie;
        Map<String, Object> response = new HashMap<>();

        try {
            movie = movieService.findById(id);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(movie == null) {
            response.put("mensaje", "La película Código: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(getMovieDTO(movie), HttpStatus.OK);
    }


    private static MovieDTO getMovieDTO(Movie movie){
        MovieDTO movieDTO = new MovieDTO();
        Map<Long, String> moviesGenres = new HashMap<>();
        movieDTO.setId(movie.getId());
        movieDTO.setOriginal_language(movie.getOriginal_language());
        movieDTO.setOriginal_title(movie.getOriginal_title());
        movieDTO.setOverview(movieDTO.getOverview());
        movieDTO.setPopularity(movie.getPopularity());
        movieDTO.setPoster_path(movie.getPoster_path());
        movieDTO.setBackdrop_path(movie.getBackdrop_path());
        movieDTO.setRelease_date(movie.getRelease_date());
        movieDTO.setTitle(movieDTO.getTitle());
        movieDTO.setVideo(movie.getVideo());
        movieDTO.setVote_average(movie.getVote_average());
        movieDTO.setVote_count(movie.getVote_count());
        movie.getGenres().forEach(g-> moviesGenres.put(g.getId(), g.getName()));
        movieDTO.setGenres(moviesGenres);
        return movieDTO;

    }
}
