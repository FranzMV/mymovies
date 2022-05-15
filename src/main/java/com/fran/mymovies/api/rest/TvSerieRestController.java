package com.fran.mymovies.api.rest;

import com.fran.mymovies.dto.TvSerieDTO;
import com.fran.mymovies.entity.TvSerie;
import com.fran.mymovies.services.TvSerieServiceImpl;
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
public class TvSerieRestController {

    @Autowired
    private TvSerieServiceImpl tvSerieService;

    @GetMapping("/tvseries")
    public List<TvSerieDTO> index(){
        List<TvSerie> tvSerieList = tvSerieService.findAll();
        List<TvSerieDTO> tvSerieDTOList = new ArrayList<>();
        tvSerieList.forEach(s-> tvSerieDTOList.add(getTvSerieDTO(s)));
        return tvSerieDTOList;
    }

    @GetMapping("/tvseries/{id}")
    public ResponseEntity<?> show(@PathVariable Long id){
        TvSerie tvSerie;
        Map<String, Object> response = new HashMap<>();

        try {
            tvSerie = tvSerieService.findById(id);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(tvSerie == null) {
            response.put("mensaje", "La serie Código: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(getTvSerieDTO(tvSerie), HttpStatus.OK);
    }

    private static TvSerieDTO getTvSerieDTO(TvSerie tvSerie){
        TvSerieDTO tvSerieDTO = new TvSerieDTO();
        Map<Long, String> tvSeriesGenres = new HashMap<>();
        tvSerieDTO.setId(tvSerie.getId());
        tvSerieDTO.setOriginal_language(tvSerie.getOriginal_language());
        tvSerieDTO.setOriginal_title(tvSerie.getOriginal_title());
        tvSerieDTO.setOverview(tvSerie.getOverview());
        tvSerieDTO.setPopularity(tvSerie.getPopularity());
        tvSerieDTO.setPoster_path(tvSerie.getPoster_path());
        tvSerieDTO.setBackdrop_path(tvSerie.getBackdrop_path());
        tvSerieDTO.setFirst_air_date(tvSerie.getFirst_air_date());
        tvSerieDTO.setTitle(tvSerie.getTitle());
        tvSerieDTO.setVote_average(tvSerie.getVote_average());
        tvSerieDTO.setVote_count(tvSerie.getVote_count());
        tvSerie.getGenres().forEach(s-> tvSeriesGenres.put(s.getId(),s.getName()));
        tvSerieDTO.setGenres(tvSeriesGenres);
        return tvSerieDTO;
    }
}
