package com.fran.mymovies.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Francisco David Manzanedo.
 */


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "genres_of_tv_series")
public class TvSerieGenre {
    @Id
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tv_series_genres",
            joinColumns = {@JoinColumn(name = "genre_id")},
            inverseJoinColumns = {@JoinColumn(name = "tv_series_id")})
    private Set<TvSerie> genres;
}
