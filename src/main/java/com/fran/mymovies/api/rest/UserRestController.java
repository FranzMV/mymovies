package com.fran.mymovies.api.rest;

import com.fran.mymovies.dto.UserDTO;
import com.fran.mymovies.entity.User;
import com.fran.mymovies.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author Francisco David Manzanedo.
 */

@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api/v1")
public class UserRestController {

    @Autowired
    private UserServiceImpl userService;


    @GetMapping("/users")
    public List<UserDTO> getAllUsers(){
        List<User> userList = userService.getUserService().findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        userList.forEach(u-> userDTOS.add(getUserDTO(u)));
        return userDTOS;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id){
        User user;
        Optional<User> optionalUser;
        Map<String, Object> response = new HashMap<>();

        try {
            optionalUser= userService.getById(id);
        } catch(DataAccessException e) {
            response.put("mensaje", "Error al realizar la consulta en la base de datos");
            response.put("error", Objects.requireNonNull(e.getMessage()).concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if(optionalUser.isEmpty()) {
            response.put("mensaje", "El usuario Código: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }else{
            user = optionalUser.get();
        }
        return new ResponseEntity<>(getUserDTO(user), HttpStatus.OK);
    }

    private static UserDTO getUserDTO(User user){
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        user.getListTypes().forEach(g-> userDTO.getListTypes().put(g.getId(), g.getListTypeName().name()));
        user.getRoles().forEach(r-> userDTO.getRoles().put(r.getId(), r.getRoleName().name()));
        user.getFavorite_movies().forEach(f-> userDTO.getFavorite_movies().put(f.getId(),f.getTitle()));
        user.getPending_movies().forEach(p-> userDTO.getPending_movies().put(p.getId(), p.getTitle()));
        user.getWatched_movies().forEach(w-> userDTO.getWatched_movies().put(w.getId(), w.getTitle()));
        user.getFavorite_tvSeries().forEach(f-> userDTO.getFavorite_tvSeries().put(f.getId(),f.getTitle()));
        user.getPending_tvSeries().forEach(p-> userDTO.getPending_tvSeries().put(p.getId(), p.getTitle()));
        user.getWatched_tvSeries().forEach(w-> userDTO.getWatched_tvSeries().put(w.getId(), w.getTitle()));
        return userDTO;
    }
}
