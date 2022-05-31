package com.fran.mymovies.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="`users`")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;


    @Column( name = "user_name", unique = true)
    private String userName;

    @Column(name = "user_email", unique = true)
    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)//Para mostrar el usuario y sus roles
    @JoinTable(name = "user_type_list", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "list_type_id"))
    private Set<ListType> listTypes = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade ={
            CascadeType.PERSIST,
    })//Para mostrar el usuario y sus roles
    @JoinTable(name = "rol_user", joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {
            CascadeType.PERSIST
    })
    @JoinTable(name = "favorite_movies", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> favorite_movies = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pending_movies", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> pending_movies = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "watched_movies", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "movie_id"))
    private Set<Movie> watched_movies = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "favorite_tv_series", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tv_serie_id"))
    private Set<TvSerie> favorite_tvSeries = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "pending_tv_series", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tv_serie_id"))
    private Set<TvSerie> pending_tvSeries = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "watched_tv_series", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "tv_serie_id"))
    private Set<TvSerie> watched_tvSeries = new HashSet<>();


    public User(String name, String userName, String email, String password, Set<ListType> listTypes, Set<Role> roles, Set<Movie> favorite_movies, Set<Movie> pending_movies, Set<Movie> watched_movies, Set<TvSerie> favorite_tvSeries, Set<TvSerie> pending_tvSeries, Set<TvSerie> watched_tvSeries) {
        this.name = name;
        this.userName = userName;
        this.email = email;
        this.password = password;
        this.listTypes = listTypes;
        this.roles = roles;
        this.favorite_movies = favorite_movies;
        this.pending_movies = pending_movies;
        this.watched_movies = watched_movies;
        this.favorite_tvSeries = favorite_tvSeries;
        this.pending_tvSeries = pending_tvSeries;
        this.watched_tvSeries = watched_tvSeries;
    }
}
