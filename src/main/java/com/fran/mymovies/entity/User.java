package com.fran.mymovies.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>Representa un usuario y sus atributos.</p>
 * @see java.io.Serializable
 * @see ListType
 * @see Role
 * @see Movie
 * @see TvSerie
 *
 * @author Francisco David Manzanedo.
 * @version 1.1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="`users`")
public class User implements Serializable {

    /**
     * Id correspondiente al usuario.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * String correspondiente al nombre de usuario.
     */
    private String name;

    /**
     * String correspondiente al nombre de usuario en la aplicación.
     */
    @Column( name = "user_name", unique = true)
    private String userName;

    /**
     * String correspondiente al email de usuario.
     */
    @Column(name = "user_email", unique = true)
    private String email;

    /**
     * String correspondiente al password de usuario.
     */
    private String password;

    /**
     * Set de {@link ListType} asignadas al usuario.
     * @see ListType
     */
    @ManyToMany(fetch = FetchType.LAZY)//Para mostrar el usuario y sus roles
    @JoinTable(name = "user_type_list", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "list_type_id"))
    private Set<ListType> listTypes = new HashSet<>();

    /**
     * Set correspondiente a los {@link Role} asignado al usuario.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade ={
            CascadeType.PERSIST,
    })//Para mostrar el usuario y sus roles
    @JoinTable(name = "rol_user", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();


    /**
     * Set de {@link Movie} correspondiente a la lista de películas favoritas del usuario.
     * @see Movie
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.PERSIST
    })
    @JoinTable(name = "favorite_movies", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> favorite_movies = new HashSet<>();
    /**
     * Set de {@link Movie} correspondiente a la lista de películas pendientes del usuario.
     * @see Movie
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "pending_movies", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> pending_movies = new HashSet<>();

    /**
     * Set de {@link Movie} correspondiente a la lista de películas vistas del usuario.
     * @see Movie
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "watched_movies", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> watched_movies = new HashSet<>();


    /**
     * Set de {@link TvSerie} correspondiente a la lista de series favoritas del usuario.
     * @see TvSerie
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "favorite_tv_series", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tv_serie_id"))
    private Set<TvSerie> favorite_tvSeries = new HashSet<>();
    /**
     * Set de {@link TvSerie} correspondiente a la lista de series pendientes del usuario.
     * @see TvSerie
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "pending_tv_series", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tv_serie_id"))
    private Set<TvSerie> pending_tvSeries = new HashSet<>();

    /**
     * Set de {@link TvSerie} correspondiente a la lista de series vistas del usuario.
     * @see TvSerie
     * @see Set
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "watched_tv_series", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tv_serie_id"))
    private Set<TvSerie> watched_tvSeries = new HashSet<>();

}
