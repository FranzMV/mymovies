package com.fran.mymovies.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String userName;
    private String email;
    private String password;
    private Map<Long, String> listTypes = new HashMap<>();
    private Map<Long, String> roles = new HashMap<>();
    private Map<Long , String> favorite_movies = new HashMap<>();
    private Map<Long, String> pending_movies = new HashMap<>();
    private Map<Long, String> watched_movies = new HashMap<>();
    private Map<Long, String> favorite_tvSeries = new HashMap<>();
    private Map<Long, String> pending_tvSeries = new HashMap<>();
    private Map<Long, String> watched_tvSeries = new HashMap<>();

}
