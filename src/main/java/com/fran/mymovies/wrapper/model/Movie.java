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
public class Movie implements Serializable {
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
    private List<Long> genres;
}
