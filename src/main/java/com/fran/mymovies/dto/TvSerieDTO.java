package com.fran.mymovies.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class TvSerieDTO {

    private Long id;
    private String original_language;
    private String original_title;
    private String overview;
    private Double popularity;
    private String poster_path;
    private String backdrop_path;
    private String first_air_date;
    private String title;
    private Double vote_average;
    private Long vote_count;
    private Map<Long, String> genres;
}
