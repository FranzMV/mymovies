package com.fran.mymovies.wrapper.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GenresOfMovies implements Serializable {
    private Long id;
    private String name;
}
