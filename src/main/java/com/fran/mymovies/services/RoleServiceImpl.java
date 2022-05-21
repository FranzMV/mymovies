package com.fran.mymovies.services;

import com.fran.mymovies.entity.Role;
import com.fran.mymovies.entity.enums.RoleName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Service
@Transactional
public class RoleServiceImpl {

    @Autowired
    IRoleService roleService;

    public void save (Role role){
        roleService.save(role);
    }

    public Optional<Role> getRoleByName(RoleName roleName){
        return roleService.findByRoleName(roleName);
    }

    public boolean existByRoleName(RoleName roleName){
        return roleService.existsByRoleName(roleName);
    }
}
