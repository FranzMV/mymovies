package com.fran.mymovies.wrapper.model;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TvSerieGenre implements Serializable {
    private Long id;
    private String name;
}
