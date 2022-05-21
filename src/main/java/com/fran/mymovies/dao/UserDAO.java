package com.fran.mymovies.dao;

import com.fran.mymovies.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDAO extends JpaRepository<User, Long> {

}
