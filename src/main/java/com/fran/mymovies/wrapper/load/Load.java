package com.fran.mymovies.wrapper.load;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.fran.mymovies.wrapper.constants.WrapperConstants;
import com.fran.mymovies.wrapper.model.MovieGenre;
import com.fran.mymovies.wrapper.model.TvSerieGenre;
import com.fran.mymovies.wrapper.model.Movie;
import com.fran.mymovies.wrapper.model.TvSerie;
import com.fran.mymovies.wrapper.db.JDBCutils;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@Slf4j
public class Load {

    private static WrapperConstants constants;
    private static Map<Long, List<Long>> moviesGenres;
    private static Map<Long, List<Long>> tvSeriesGenres;

    public static void tearUp() {
        constants = new WrapperConstants();
        List<Movie> movieList = getMovies();
        List<TvSerie> tvSeriesList = getTVSeries();
        List<MovieGenre> moviesGenresList = getGenresOfMovies(constants.getJSON_URL_MOVIE_GENRES());
        List<TvSerieGenre> tvSeriesGenresList = getGenresOfTvSeries(constants.getJSON_URL_TV_GENRES());

        JDBCutils.connectDB(constants.getURL(), constants.getUSER(), constants.getPASS());

        //INSERT INTO THE DB TABLES
        //Tabla movies
        int resultMovies = JDBCutils.insertMovies(constants.getSQL_INSERT_MOVIES(), movieList);
        if (resultMovies > -1)
            log.info("INSERTADAS " + resultMovies + " pelis.");

        //Tabla tv_series
        int resultTvSeries = JDBCutils.insertTvSeries(constants.getSQL_INSERT_TV_SERIES(), tvSeriesList);
        if (resultTvSeries > -1)
            log.info("INSERTADAS " + resultTvSeries + " series.");

        //Tabla géneros de pelis
        int resultGenresOfMovies = JDBCutils.insertGenresOfMovies(constants.getSQL_INSERT_GENRES_OF_MOVIES(),
                moviesGenresList);
        if (resultGenresOfMovies > -1)
            log.info("INSERTADAS " + resultGenresOfMovies + " géneros de pelis.");

        //Tabla géneros de tv_series
        int resultGenresOfTvSeries = JDBCutils.insertGenresOfTvSeries(constants.getSQL_INSERT_GENRES_OF_TV_SERIES(),
                tvSeriesGenresList);
        if (resultGenresOfTvSeries > -1)
            log.info("INSERTADAS " + resultGenresOfTvSeries + " géneros de tv series.");

        //Tabla intermedia de movies-genres
        int resultMoviesGenres = JDBCutils.insertManyToManyTables(constants.getSQL_INSERT_MOVIES_GENRES(),
                moviesGenres);
        if (resultMoviesGenres > -1)
            log.info("INSERTADOS " + resultMoviesGenres + " registros M:M pelis-géneros.");
/*
         CONTIENE ERRORES Y POR ESO SE INTRODUCEN LOS DATOS A MANO EN LA BD.
         //Tabla intermedia de tvSeries-genres
         int resultTvSeriesGenres = JDBCUtils.insertManyToManyTables(constants.getSQL_INSERT_TV_SERIES_GENRES(),
         tvSeriesGenres);
         if(resultTvSeriesGenres > -1) System.out.println("INSERTADAS "+resultTvSeriesGenres+".");
 */
        JDBCutils.disconnetDB();
    }

    /**
     * Recibe una url en formato String y devuelve el contenido de esa Url como una
     * cadena
     * @param web la cadena que almacena la url
     * @return Todo el contenido de esa url en un String
     */
    private static String readUrl(String web) {
        try {
            URL url = new URL(web);
            URLConnection uc = url.openConnection();
            uc.setRequestProperty("User-Agent", "PostmanRuntime/7.20.1");
            uc.connect();
            return new BufferedReader(new InputStreamReader(uc.getInputStream(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining());
        } catch (Exception e) { log.error("No se ha podido la leer la URL: " + web); }
        return null;
    }
    /**
     * Obtiene 5.000 películas correspondientes a la lista TOP_POPULAR.
     * @return Lista con las películas.
     */
    private static  List<Movie> getMovies(){
        List<Movie> result = new ArrayList<>();
        moviesGenres = new HashMap<>();
        final int[] contador = {1};
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            while (contador[0] <= constants.getMAX_PAG_MOVIES()) {
                try {
                    Object obj = new JSONParser().parse(readUrl( constants.getJSON_URL_MOVIES()+ contador[0]));
                    JSONObject jsonObject = (JSONObject) obj;
                    JSONArray jsonArray = (JSONArray) jsonObject.get("results");
                    for (Object object : jsonArray) {
                        Long id = (Long) (((JSONObject) object).get("id"));
                        String originalLanguage = (String) (((JSONObject) object).get("original_language"));
                        String originalTitle = (String) (((JSONObject) object).get("original_title"));
                        String overview = (String) (((JSONObject) object).get("overview"));
                        Object popularity = (((JSONObject) object)).get("popularity");
                        Double popularityAux = Double.parseDouble(popularity.toString());
                        String posterPath = (String) (((JSONObject) object).get("poster_path"));
                        String backdropPath = (String) (((JSONObject) object).get("backdrop_path"));
                        String releaseDate = (String) (((JSONObject) object).get("release_date"));
                        String title = (String) (((JSONObject) object).get("title"));
                        Boolean video = (Boolean) (((JSONObject) object).get("video"));
                        Object voteAverage = (((JSONObject) object).get("vote_average"));
                        Double voteAverageAux = Double.parseDouble(voteAverage.toString());
                        Long voteCount = (Long) (((JSONObject) object).get("vote_count"));
                        JSONArray jsonGenres = (JSONArray) ((JSONObject) object).get("genre_ids");
                        ArrayList<Long> genres = new ArrayList<Long>(jsonGenres);
                        result.add(
                                new Movie(
                                        id,
                                        originalLanguage,
                                        originalTitle,
                                        overview,
                                        popularityAux,
                                        posterPath,
                                        backdropPath,
                                        releaseDate,
                                        title,
                                        video,
                                        voteAverageAux,
                                        voteCount,
                                        genres
                                )
                        );
                        moviesGenres.put(id, genres);
                    }

                } catch(ParseException e){
                    log.info("Ha ocurrido un error leyendo pelis.");
                    e.printStackTrace();
                }
                contador[0]++;
            }
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }


    /**
     * Obtiene todas las series correspondientes a la lista TOP_POPULAR.
     * @return Lista con todas las series.
     */
    private static List<TvSerie> getTVSeries()  {
        List<TvSerie> result = new ArrayList<>();
        tvSeriesGenres = new HashMap<>();
        final int[] counter = {1};
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            while (counter[0] <= constants.getMAX_PAG_TV_SERIES()) {
                try {
                    Object obj = new JSONParser().parse(readUrl(constants.getJSON_URL_TV_SERIES() + counter[0]));
                    JSONObject jsonObject = (JSONObject) obj;
                    JSONArray jsonArray = (JSONArray) jsonObject.get("results");
                    for (Object object : jsonArray) {
                        Long id = (Long) (((JSONObject) object).get("id"));
                        String originalLanguage = (String) (((JSONObject) object).get("original_language"));
                        String originalTitle = (String) (((JSONObject) object).get("original_name"));
                        String overview = (String) (((JSONObject) object).get("overview"));
                        Object popularity = (((JSONObject) object)).get("popularity");
                        Double popularityAux = Double.parseDouble(popularity.toString());
                        String posterPath = (String) (((JSONObject) object).get("poster_path"));
                        String backdropPath = (String) (((JSONObject) object).get("backdrop_path"));
                        String first_air_date = (String) (((JSONObject) object).get("first_air_date"));
                        String title = (String) (((JSONObject) object).get("name"));
                        Object voteAverage = (((JSONObject) object).get("vote_average"));
                        Double voteAverageAux = Double.parseDouble(voteAverage.toString());
                        Long voteCount = (Long) (((JSONObject) object).get("vote_count"));
                        JSONArray jsonGenres = (JSONArray) ((JSONObject) object).get("genre_ids");
                        ArrayList<Long> genres = new ArrayList<Long>(jsonGenres);
                        result.add(
                                new TvSerie(
                                        id,
                                        originalLanguage,
                                        originalTitle,
                                        overview,
                                        popularityAux,
                                        posterPath,
                                        backdropPath,
                                        first_air_date,
                                        title,
                                        voteAverageAux,
                                        voteCount,
                                        genres
                                )
                        );
                        tvSeriesGenres.put(id,genres);
                    }

                } catch (ParseException e) {
                    log.info("Ha ocurrido un error leyendo series.");
                    e.printStackTrace();
                }
                counter[0]++;
            }
        });
        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     *Obtiene los géneros correspondientes a las movies.
     * @param jsonURL String correspondientes a la url de géneros de movies.
     * @return Lista con todos los géneros de movies.
     */
    private static List<MovieGenre> getGenresOfMovies(String jsonURL){
        List<MovieGenre> result = new ArrayList<>();
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            try {
                Object obj = new JSONParser().parse(readUrl(jsonURL));
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray jsonArray = (JSONArray) jsonObject.get("genres");
                for (Object object : jsonArray) {
                    Long id = (Long) (((JSONObject) object).get("id"));
                    String name = (String) (((JSONObject) object).get("name"));
                    result.add(new MovieGenre(id,name));
                }
            } catch(ParseException e){
                log.info("Ha ocurrido un error leyendo géneros de pelis.");
                e.printStackTrace();
            }
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * Obtiene los géneros correspondientes a todas las series.
     * @param jsonURL String correspondientes a la url de géneros de series.
     * @return Lista con todos los géneros de series.
     */
    private static List<TvSerieGenre> getGenresOfTvSeries(String jsonURL){
        List<TvSerieGenre> result = new ArrayList<>();
        CompletableFuture<Void> future = CompletableFuture.runAsync(()->{
            try {
                Object obj = new JSONParser().parse(readUrl(jsonURL));
                JSONObject jsonObject = (JSONObject) obj;
                JSONArray jsonArray = (JSONArray) jsonObject.get("genres");
                for (Object object : jsonArray) {
                    Long id = (Long) (((JSONObject) object).get("id"));
                    String name = (String) (((JSONObject) object).get("name"));
                    result.add(new TvSerieGenre(id,name));
                }
            } catch(ParseException e){
                log.info("Ha ocurrido un error leyendo géneros de tv series.");
                e.printStackTrace();
            }
        });

        try {
            future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
