package com.misiontic.grupo17.securityBackend.controllers;

import com.misiontic.grupo17.securityBackend.models.User;
import com.misiontic.grupo17.securityBackend.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserServices userServices;

    @GetMapping("/all")
    public List<User> getAllUsers(){
        return this.userServices.index();
    }

    @GetMapping("/{id}")
    public Optional<User> getUserById(@PathVariable("id") int id){
        return this.userServices.show(id);
    }

    @PostMapping("/insert")
    @ResponseStatus(HttpStatus.CREATED)
    public User insertUser(@RequestBody User user){
        return this.userServices.create(user);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public HashMap<String, Object> loginUser(@RequestBody User user){
        return this.userServices.login(user);
    }

    @PutMapping("/update/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public User updateUser(@PathVariable("id") int id, @RequestBody User user){
        return this.userServices.update(id, user);
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public boolean deleteUser(@PathVariable("id") int id){
        return this.userServices.delete(id);
    }
}
