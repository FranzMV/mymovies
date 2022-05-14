package com.fran.mymovies.wrapper.db;

import com.fran.mymovies.wrapper.model.MovieGenre;
import com.fran.mymovies.wrapper.model.TvSerieGenre;
import com.fran.mymovies.wrapper.model.Movie;
import com.fran.mymovies.wrapper.model.TvSerie;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Slf4j
public class JDBCutils {

    private static Connection con = null;
    private static PreparedStatement ps = null;

    /**
     * Conecta con la Base de datos
     * @param url String correspondiente a la dirección de la base de datos.
     * @param user String correspondiente al nombre de usuario de la base de datos.
     * @param password String correspondiente al password de la base de datos.
     */
    public static void connectDB(String url, String user, String password) {

        try {
            con = DriverManager.getConnection(url, user, password);
            log.info("Conexión establecida");
        }catch (SQLException e) {
            log.error("Ha ocurrido un problema al conectar la base de datos");
            e.printStackTrace();
        }
    }

    /**Desconecta la Base de datos*/
    public static void disconnetDB() {
        try {
            con.close();
            log.info("Conexión cerrada");
        } catch (SQLException e) {
            log.error("Ha ocurrido un problema al desconectar la base de datos.");
            e.printStackTrace();
        }
    }

    /**
     * Inserta los datos de {@link Movie} en la tabla Movies de la BD.
     * @param sql String correspondiente a la sentencia SQL.
     * @param moviesList Lista de {@link Movie} a insertar en la BD.
     * @return Integer El número de los registros insertados en la tabla o -1 si no se ha podido insertar.
     */
    public static int insertMovies(String sql, List<Movie> moviesList){
        try{
            ps = con.prepareStatement(sql);
            int count = 0;
            for(Movie m : moviesList){
                ps.setLong(1, m.getId());
                ps.setString(2, m.getOriginal_language());
                ps.setString(3, m.getOriginal_title());
                ps.setString(4, m.getOverview());
                ps.setDouble(5, m.getPopularity());
                ps.setString(6, m.getPoster_path());
                ps.setString(7, m.getBackdrop_path());
                ps.setString(8, m.getRelease_date());
                ps.setString(9, m.getTitle());
                ps.setBoolean(10, m.getVideo());
                ps.setDouble(11, m.getVote_average());
                ps.setLong(12, m.getVote_count());
                ps.executeUpdate();
                count++;
            }
            return count;

        }catch (SQLException e){ log.error("Ha ocurrido un error insertando las pelis: ".concat(e.getMessage())); }

        return -1;
    }


    /**
     * Inserta los datos de {@link TvSerie} en la tabla TvSeries de la BD.
     * @param sql String correspondiente a la sentencia SQL.
     * @param tvSeriesList Lista de {@link TvSerie} a insertar en la BD.
     * @return Integer El número de los registros insertados en la tabla o -1 si no se ha podido insertar.
     */
    public static int insertTvSeries(String sql, List<TvSerie> tvSeriesList){
        try{
            ps = con.prepareStatement(sql);
            int count = 0;
            for(TvSerie tv: tvSeriesList){
                ps.setLong(1, tv.getId());
                ps.setString(2, tv.getOriginal_language());
                ps.setString(3, tv.getOriginal_title());
                ps.setString(4, tv.getOverview());
                ps.setDouble(5, tv.getPopularity());
                ps.setString(6, tv.getPoster_path());
                ps.setString(7, tv.getBackdrop_path());
                ps.setString(8, tv.getFirst_air_date());
                ps.setString(9, tv.getTitle());
                ps.setDouble(10, tv.getVote_average());
                ps.setLong(11, tv.getVote_count());
                ps.executeUpdate();
                count++;
            }
            return count;

        }catch (SQLException e){ log.error("Ha ocurrido un error insertando tv_series: ".concat(e.getMessage())); }

        return -1;
    }

    /**
     *
     * @param sql
     * @param genresList
     * @return
     */
    public static int insertGenresOfMovies(String sql, List<MovieGenre> genresList){
        try{
            ps = con.prepareStatement(sql);
            int count = 0;
            for(MovieGenre g: genresList){
                ps.setLong(1, g.getId());
                ps.setString(2, g.getName());
                ps.executeUpdate();
                count++;
            }
            return count;
        }catch (SQLException e){ log.error("Ha ocurrido un error insertando tv_series: ".concat(e.getMessage())); }
        return -1;
    }

    /**
     *
     * @param sql
     * @param genresList
     * @return
     */
    public static int insertGenresOfTvSeries(String sql, List<TvSerieGenre> genresList){
        try{
            ps = con.prepareStatement(sql);
            int count = 0;
            for(TvSerieGenre g: genresList){
                ps.setLong(1, g.getId());
                ps.setString(2, g.getName());
                ps.executeUpdate();
                count++;
            }
            return count;
        }catch (SQLException e){ log.error("Ha ocurrido un error insertando tv_series: ".concat(e.getMessage())); }
        return -1;
    }

    /**
     * Inserta los registros en las tablas cuya relación es M:M
     * @param sql String correspondiente al Insert del registro
     * @param map HashMap con los id's a cuyas tablas hacen referencia.
     * @return Integer El número de los registros insertados en la tabla o -1 si no se ha podido insertar.
     */
    public static int insertManyToManyTables(String sql, Map<Long, List<Long>> map) {
        int count = 0;
        try {
            ps = con.prepareStatement(sql);
            for(Map.Entry<Long, List<Long>> entry : map.entrySet()){
                long key = entry.getKey();
                List<Long> valueList = entry.getValue();
                ps.setLong(1, key);
                for(Long value : valueList) {
                    ps.setLong(2, value);
                    ps.executeLargeUpdate();
                    count++;
                }
            }
            //map.entrySet().stream().forEach(e-> log.info(e.getKey()+": "+e.getValue()));
        }catch (SQLException e) { log.error("Ha ocurrido un error insertado en las tablas M:M: ".concat(e.getMessage()));  }

        return count;
    }
}
