package com.fran.mymovies.entity;

import lombok.*;
import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Representa una serie o programa de televisión y sus atributos junto a los {@link TvSerieGenre} géneros a los
 * que pertenece.</p>
 * @see TvSerieGenre
 * @see java.io.Serializable
 *
 * @author Francisco David Manzanedo.
 * @version 1.1
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tv_series")
public class TvSerie implements Serializable {

    /**
     * El id de la serie.
     */
    @Id
    private Long id;
    /**
     * El idioma original de la serie.
     */
    private String original_language;
    /**
     * El título original de la serie.
     */
    private String original_title;
    /**
     * La sinopsis de la serie.
     */
    @Column(length = 10485760)
    private String overview;
    /**
     * La popularidad de la serie.
     */
    private Double popularity;
    /**
     * La ruta del poster  de la serie.
     */
    private String poster_path;
    /**
     * La ruta de la contraportada de la serie.
     */
    private String backdrop_path;
    /**
     * Fecha de estreno de la serie.
     */
    private String first_air_date;
    /**
     * El título de la serie.
     */
    private String title;
    /**
     * Media de votos de la serie.
     */
    private Double vote_average;
    /**
     * Votos totales de la serie.
     */
    private Long vote_count;

    /**
     * Set de {@link TvSerieGenre} a los que pertenece la serie.
     * @see TvSerieGenre
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tv_series_genres",
            joinColumns = {@JoinColumn(name = "tv_series_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")})
    private Set<TvSerieGenre> genres;

    /**
     * Set de {@link ListType} a la que puede ser añadida la serie.
     * @see ListType
     * @see Set
     */
    @Transient
    private Set<ListType> listType = new HashSet<>();
}
