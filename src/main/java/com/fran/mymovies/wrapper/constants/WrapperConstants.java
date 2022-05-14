package com.fran.mymovies.wrapper.constants;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WrapperConstants {

    // Link for POPULAR_MOVIES https://api.themoviedb.org/3/movie/popular?api_key=270517ab0eebf331b5a05ef69f7830ae
    //API-KEY: 270517ab0eebf331b5a05ef69f7830ae

    //JSON PAGE SIZE
    private final  int MAX_PAG_MOVIES = 300;//100
    private final  int MAX_PAG_TV_SERIES = 121;//121 -> TOTAL PAGINAS

    //API URLs
    private final String JSON_URL_MOVIES = "https://api.themoviedb" +
            ".org/3/movie/top_rated?api_key=270517ab0eebf331b5a05ef69f7830ae&language=es-ES&page=";
    private  final String JSON_URL_TV_SERIES ="https://api.themoviedb" +
            ".org/3/tv/top_rated?api_key=270517ab0eebf331b5a05ef69f7830ae&language=es-ES&page=";
    private  final String JSON_URL_MOVIE_GENRES ="https://api.themoviedb" +
            ".org/3/genre/movie/list?api_key=270517ab0eebf331b5a05ef69f7830ae&language=es-ES";
    private  final String JSON_URL_TV_GENRES ="https://api.themoviedb" +
            ".org/3/genre/tv/list?api_key=270517ab0eebf331b5a05ef69f7830ae&language=es-ES";

    //DB CONSTANTS
    private final String URL = "jdbc:postgresql://localhost:5432/mymovies_db_test";
    private final String USER ="postgres";
    private final String PASS ="postgres";

    private final String SQL_INSERT_MOVIES = "INSERT INTO movies ("+
            "id, original_language, original_title, overview, popularity," +
            "poster_path, backdrop_path, release_date, title, video, vote_average, vote_count)" +
            " VALUES(?,?,?,?,?,?,?,?,?,?,?,?);";
    private final String SQL_INSERT_TV_SERIES = "INSERT INTO tv_series ("+
            "id, original_language, original_title, overview, popularity," +
            "poster_path, backdrop_path, first_air_date, title, vote_average, vote_count)" +
            " VALUES(?,?,?,?,?,?,?,?,?,?,?);";
    private final String SQL_INSERT_GENRES_OF_MOVIES = "INSERT INTO genres_of_movies (id, name) " +
            "VALUES(?,?);";
    private final String SQL_INSERT_GENRES_OF_TV_SERIES = "INSERT INTO genres_of_tv_series (id, name) " +
            "VALUES(?,?);";
    private final String SQL_INSERT_MOVIES_GENRES = "INSERT INTO movies_genres (movie_id, genre_id) " +
            "VALUES(?,?);";
    private final String SQL_INSERT_TV_SERIES_GENRES = "INSERT INTO tv_series_genres (tv_series_id, genre_id) " +
            "VALUES(?,?);";
}
