package com.fran.mymovies.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * <p>Representa un género correspondiente a una {@link TvSerie} y sus atributos. </p>
 * @see TvSerie
 * @see java.io.Serializable
 *
 * @author Francisco David Manzanedo.
 * @version 1.1
 */


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "genres_of_tv_series")
public class TvSerieGenre implements Serializable {

    /**
     * Id correspondiente al género.
     */
    @Id
    private Long id;
    /**
     * Nombre del género.
     */
    private String name;
    /**
     * Set de {@link TvSerie} a las que pertenece.
     * @see TvSerie
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "tv_series_genres",
            joinColumns = {@JoinColumn(name = "genre_id")},
            inverseJoinColumns = {@JoinColumn(name = "tv_series_id")})
    private Set<TvSerie> genres;
}
