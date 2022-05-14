package com.fran.mymovies.wrapper.model;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class TvSerie implements Serializable {
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
    private List<Long> genres;
}
