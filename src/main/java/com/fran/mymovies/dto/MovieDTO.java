package com.fran.mymovies.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Map;

/**
 * @author Francisco David Manzanedo.
 */
@Getter
@Setter
@NoArgsConstructor
public class MovieDTO {
    private Long id;
    private String original_language;
    private String original_title;
    private String overview;
    private Double popularity;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    private String title;
    private Boolean video;
    private Double vote_average;
    private Long vote_count;
    private Map<Long, String> genres;
}
