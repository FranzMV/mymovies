package com.fran.mymovies.services;

import com.fran.mymovies.dao.IRoleDAO;
import com.fran.mymovies.entity.Role;
import com.fran.mymovies.entity.enums.RoleName;

import java.util.Optional;

public interface IRoleService extends IRoleDAO {

    Optional<Role> findByRoleName(RoleName roleName);
    boolean existsByRoleName(RoleName roleName);
}
