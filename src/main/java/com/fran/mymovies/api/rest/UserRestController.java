package com.fran.mymovies.api.rest;

import com.fran.mymovies.entity.TvSerie;
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
    public List<User> getAllUsers(){
        return userService.getUserService().findAll();
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
            response.put("mensaje",
                    "El usuario Código: ".concat(id.toString().concat(" no existe en la base de datos!")));
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }else{
            user = optionalUser.get();
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
