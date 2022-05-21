package com.fran.mymovies.entity;

import lombok.*;
import javax.persistence.*;
import java.util.Set;

/**
 * @author Francisco David Manzanedo.
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "tv_series")
public class TvSerie {

    @Id
    private Long id;
    private String original_language;
    private String original_title;
    @Column(length = 10485760)
    private String overview;
    private Double popularity;
    private String poster_path;
    private String backdrop_path;
    private String first_air_date;
    private String title;
    private Double vote_average;
    private Long vote_count;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tv_series_genres",
            joinColumns = {@JoinColumn(name = "tv_series_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private Set<TvSerieGenre> genres;
}
