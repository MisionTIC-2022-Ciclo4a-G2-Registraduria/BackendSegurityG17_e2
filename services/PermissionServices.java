package com.misiontic.grupo2.registraduria.services;

import com.misiontic.grupo2.registraduria.models.Permission;
import com.misiontic.grupo2.registraduria.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.ListResourceBundle;
import java.util.Optional;

@Service
/**
 *
 */
public class PermissionServices {
   @Autowired
   private PermissionRepository permissionRepository;

    /**
     *
     * @return
     */
   public List<Permission> index(){
       return (List<Permission>) this.permissionRepository.findAll();
   }

    /**
     *
     * @param id
     * @return
     */
   public Optional<Permission> show(int id){
       Optional<Permission> result = this.permissionRepository.findById(id);
       if (result.isEmpty())
           throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                   "The requested permission is does not exists");
       return result;
   }

    /**
     *
     * @param newPermission
     * @return
     */
   public ResponseEntity<Permission> create(Permission newPermission){
       if (newPermission.getId() != null){
           Optional<Permission> tempPermission = this.permissionRepository.findById(newPermission.getId());
           if (tempPermission.isPresent())
               throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                       "ID is yet in the database");
       }
       if ((newPermission.getUrl() != null) && (newPermission.getMethod() != null)){
           return new ResponseEntity<>(this.permissionRepository.save(newPermission), HttpStatus.CREATED);
       }
       else
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                   "Mandatory fields had not been provided");
   }

    /**
     *
     * @param id
     * @param updatedPermission
     * @return
     */
   public ResponseEntity<Permission> update(int id, Permission updatedPermission){
       if(id > 0){
            Optional<Permission> tempPermission= this.permissionRepository.findById(id);
            if (tempPermission.isPresent()){
                if (updatedPermission.getMethod() != null)
                    tempPermission.get().setMethod(updatedPermission.getMethod());
                if(updatedPermission.getUrl() != null)
                    tempPermission.get().setUrl(updatedPermission.getUrl());
                return new ResponseEntity<>(this.permissionRepository.save(tempPermission.get()), HttpStatus.CREATED);
            }
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Permission id does not exist in database");}

       else
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                   "Permission id cannot be negative");
   }

    /**
     *
     * @param id
     * @return
     */
   public ResponseEntity<Boolean> delete(int id){
       Boolean success = this.show(id).map(permission -> {
           this.permissionRepository.delete(permission);
           return true;
       }).orElse(false);
       if (success)
           return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
       else
           throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                   "Permission cannot be deleted");
   }


}
