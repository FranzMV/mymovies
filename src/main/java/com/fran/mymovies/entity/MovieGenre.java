package com.fran.mymovies.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Representa un género correspondiente a una {@link Movie} y sus atributos. </p>
 * @see Movie
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
@Table(name = "genres_of_movies")
public class MovieGenre implements Serializable {

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
     * Set de {@link Movie} a las que pertenece.
     * @see Movie
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "movies_genres",
            joinColumns = {@JoinColumn(name = "genre_id")},
            inverseJoinColumns = {@JoinColumn(name = "movie_id")})
    private Set<Movie> movies = new HashSet<>();

}
