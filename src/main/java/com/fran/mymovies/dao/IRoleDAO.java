package com.fran.mymovies.dao;

import com.fran.mymovies.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRoleDAO extends JpaRepository<Role, Long> {
}
