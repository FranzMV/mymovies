package com.fran.mymovies.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Francisco David Manzanedo.
 */

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie implements Serializable {

    @Id
    private Long id;
    private String original_language;
    private String original_title;
    @Column(length = 10485760)
    private String overview;
    private Double popularity;
    private String poster_path;
    private String backdrop_path;
    private String release_date;
    private String title;
    private Boolean video;
    private Double vote_average;
    private Long vote_count;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "movies_genres",
        joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private Set<MovieGenre> genres = new HashSet<>();

    @Transient
    private Set<ListType> listType = new HashSet<>();
}
