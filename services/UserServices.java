package com.misiontic.grupo17.securityBackend.services;

import com.misiontic.grupo17.securityBackend.models.User;
import com.misiontic.grupo17.securityBackend.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
/**
 *
 */
public class UserServices {
    @Autowired
    private UserRepository userRepository;

    /**
     *
     * @return
     */
    public List<User> index() {
        // TODO check class validations
        return (List<User>) this.userRepository.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<User> show(int id) {
        return this.userRepository.findById(id);
    }

    /**
     *
     * @param newUser
     * @return
     */
    public User create(User newUser) {
        if (newUser.getId() == null) {
            if ((newUser.getEmail() != null) && (newUser.getNickname() != null) && (newUser.getPassword() != null)){
                newUser.setPassword(this.convertToSHA256(newUser.getPassword()));
                return this.userRepository.save(newUser);
            }
            else {
                // TODO agregar excepción de 400 Bad Request
                return newUser;
            }
        }
        else {
            // TODO validar si ID ya existe y manejar excepción
            return newUser;
        }
    }

    /**
     *
     * @param id
     * @param user
     * @return
     */
    public User update(int id, User user) {
        if (id > 0) {
            Optional<User> tempUser = this.userRepository.findById(id);
            if (!tempUser.isEmpty()) {
                if (user.getNickname() != null)
                    tempUser.get().setNickname(user.getNickname());
                if (user.getPassword() != null)
                    tempUser.get().setPassword(this.convertToSHA256(user.getPassword()));
                return this.userRepository.save(tempUser.get());
            }
            else {
                // TODO error 404 not found
                return user;
            }
        }
        else {
            // TODO bad request 400 id < 0
            return user;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean delete(int id){
        Boolean success = this.show(id).map(user -> {
            this.userRepository.delete(user);
            return true;
        }).orElse(false);
        return success;
    }


    /**
     *
     * @param user
     * @return
     */
    public HashMap<String, Object> login(User user){
        HashMap<String, Object> result = new HashMap<>();
        if(user.getPassword() != null && user.getEmail() != null) {
            String email = user.getEmail();
            String password = this.convertToSHA256(user.getPassword());
            Optional<User> tempUser = this.userRepository.validateLogin(email, password);
            if (tempUser.isEmpty())
                result.put("permission", false);
            else {
                result.put("permission", true);
                result.put("nickname", tempUser.get().getNickname());
            }
        }
        else
            result.put("permission", false);
        return result;
    }

    /**
     *
     * @param password
     * @return
     */
    public String convertToSHA256(String password){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
            return null;
        }
        StringBuffer sb = new StringBuffer();
        byte[] hash = md.digest(password.getBytes());
        for(byte b: hash)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
