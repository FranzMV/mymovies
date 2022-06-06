package com.fran.mymovies.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *<p>Representa una película y sus atributos junto a los {@link MovieGenre} a los que pertenece.</p>
 * @see MovieGenre
 * @see java.io.Serializable
 * @author Francisco David Manzanedo.
 * @version 1.1
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "movies")
public class Movie implements Serializable {

    /**
     * El id de la película.
     */
    @Id
    private Long id;
    /**
     * El idioma original de la película.
     */
    private String original_language;
    /**
     * El lenguaje original de la película.
     */
    private String original_title;
    /**
     * La sinopsis de la película.
     */
    @Column(length = 10485760)
    private String overview;
    /**
     * La popularidad de la película.
     */
    private Double popularity;
    /**
     * La ruta del poster de la película.
     */
    private String poster_path;
    /**
     * La ruta de la contraportada de la película.
     */
    private String backdrop_path;
    /**
     * La fecha de estreno de la película.
     */
    private String release_date;
    /**
     * El título de la película.
     */
    private String title;
    /**
     * Si la película contiene trailer o no.
     */
    private Boolean video;
    /**
     * Media de votos de la película.
     */
    private Double vote_average;
    /**
     * Votos totales de la película.
     */
    private Long vote_count;

    /**
     * Set de géneros a los que pertenece la película.
     * @see MovieGenre
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "movies_genres",
        joinColumns = {@JoinColumn(name = "movie_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private Set<MovieGenre> genres = new HashSet<>();

    /**
     * Set de {@link ListType} a la que puede ser añadida la película.
     * @see ListType
     * @see Set
     */
    @Transient
    private Set<ListType> listType = new HashSet<>();
}
