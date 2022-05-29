package com.fran.mymovies.services;

import com.fran.mymovies.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Getter
@Setter
@NoArgsConstructor
@Service
@Transactional
public class UserServiceImpl {

    @Autowired
    IUserService userService;

    public List<User> userList (){
        return userService.findAll();
    }

    public Optional<User> getById(Long id){
        return userService.findById(id);
    }

    public Optional<User> getUserByName(String userName){
        return userService.findByUserName(userName);
    }

    public void save(User user){
        userService.save(user);
    }

    public boolean existsById(Long id){
        return userService.existsById(id);
    }

    public boolean existsByName(String userName){
        return userService.existsByUserName(userName);
    }

    public void deleteById(Long id){
        userService.deleteById(id);
    }
}
