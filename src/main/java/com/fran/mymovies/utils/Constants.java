package com.fran.mymovies.utils;


import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Constants {


        //REGISTRO Y LOGIN DE USUARIOS
        public static final String ERROR_LABEL = "error";

        public static final String RESULT_LABEL = "result";

        public static final String ERROR_INVALID_PASSWORD= "La contraseña no es correcta.";
        public static final String ERROR_PASS_USERNAME_INVALID= "Nombre de usuario o contraseña incorrectos.";

        public static final String ERROR_USERNAME_ALREADY_EXISTS="El nombre de usuario ya está en uso.";

        public static final String OK_REGISTER_LABEL ="okRegister";

        public static final String PLEASE_SIGNIN ="Por favor, inicie sesión.";

        //TIPOS DE LISTAS
        public static final String TYPE_LIST_FAVORITE= "FAVORITA";
        public static final String TYPE_LIST_PENDING= "PENDIENTE";
        public static final String TYPE_LIST_WATCHED= "VISTA";
        public static final String NO_LIST_SELECTED_ERROR="Selecciona la lista a la que desea añadir la película.";

        //PELICULAS
        public static final String ERROR_MSG_MOVIE_EXISTS_WATCHED= "La película seleccionada ya ha existe en esta " +
                "lista de Vistas.";
        public static final String ERROR_MSG_MOVIE_EXISTS_FAVORITE= "La película seleccionada ya ha existe en esta " +
                "lista de Favoritas.";
        public static final String ERROR_MSG_MOVIE_EXISTS_PENDING= "La película seleccionada ya ha existe en esta " +
                "lista de Pendientes.";

        //SERIES
        public static final String ERROR_MSG_SERIE_EXISTS_WATCHED= "La serie seleccionada ya ha existe en esta " +
                "lista de Vistas.";
        public static final String ERROR_MSG_SERIE_EXISTS_FAVORITE= "La serie seleccionada ya ha existe en esta " +
                "lista de Favoritas";

        public static final String ERROR_MSG_SERIE_EXISTS_PENDING= "La serie seleccionada ya ha existe en esta " +
                "lista de Pendientes.";

}
