package com.fran.mymovies.services;

import com.fran.mymovies.dao.UserDAO;
import com.fran.mymovies.entity.User;

import java.util.Optional;

public interface IUserService extends UserDAO {

    Optional<User> findByUserName(String username);
    boolean existsByUserName(String username);

    void deleteById (Long id);

}
