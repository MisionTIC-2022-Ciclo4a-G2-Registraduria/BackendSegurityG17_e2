package com.misiontic.grupo2.registraduria.services;

import com.misiontic.grupo2.registraduria.models.Permission;
import com.misiontic.grupo2.registraduria.models.Rol;
import com.misiontic.grupo2.registraduria.repositories.PermissionRepository;
import com.misiontic.grupo2.registraduria.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
/**
 *
 */
public class RolServices {
    @Autowired
    private RolRepository rolRepository;
    @Autowired
    private PermissionRepository permissionRepository;


    /**
     *
     * @return
     */

    public List<Rol> index(){
        return (List<Rol>) this.rolRepository.findAll();
    }

    /**
     *
     * @param id
     * @return
     */

    public Optional<Rol> show(int id) {
        Optional<Rol> result = this.rolRepository.findById(id);
        if (result.isEmpty())
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "The requested rol is does not exists");
        return result;
    }

    /**
     *
     * @param newRol
     * @return
     */
    public ResponseEntity<Rol> create(Rol newRol) {
        if (newRol.getIdRol() != null) {
            Optional<Rol> temRol = this.rolRepository.findById(newRol.getIdRol());
            if (temRol.isPresent())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "ID is yet in the database");
        }
        if (newRol.getName() != null) {
            return new ResponseEntity<>(this.rolRepository.save(newRol), HttpStatus.CREATED);
        } else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Mandatory fields had not been provided");
    }


    /**
     *
     * @param id
     * @param updatedRol
     * @return
     */
    public ResponseEntity<Rol> update(int id, Rol updatedRol){
        if (id  > 0){
            Optional<Rol> tempRol = this.rolRepository.findById(id);
            if (tempRol.isPresent()){
                if (updatedRol.getName() != null)
                    tempRol.get().setName(updatedRol.getName());
                if (updatedRol.getDescription() != null)
                    tempRol.get().setDescription(updatedRol.getDescription());
                return new ResponseEntity<>(this.rolRepository.save(tempRol.get()), HttpStatus.CREATED);
            }
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Rol id does not exist in database");
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Rol id cannot be negative");
    }

    /**
     *
     * @param idRol
     * @param idPermission
     * @return
     */
    public ResponseEntity<Rol> updateAddPermission(int idRol, int idPermission){
        Optional<Rol> rol = this.rolRepository.findById(idRol);
        if (rol.isPresent()){
            Optional<Permission> permission = this.permissionRepository.findById(idPermission);
            if (permission.isPresent()){
                if (rol.get().getPermissions().contains(permission.get()))
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Permission is yet related to the rol");
                else{
                    Set<Permission> tempPermissions = rol.get().getPermissions();
                    tempPermissions.add(permission.get());
                    rol.get().setPermissions(tempPermissions);
                    return new ResponseEntity<>(this.rolRepository.save(rol.get()), HttpStatus.CREATED);
                }
            }
            else
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Provided permission is not in database");
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Provided rol is not in database");
    }


    /**
     *
     * @param id
     * @return
     */
    public ResponseEntity<Boolean> delete(int id){
        Boolean success = this.show(id).map(rol ->{
            this.rolRepository.delete(rol);
            return true;
        }).orElse(false);
        if (success)
            return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
        else
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Rol cannot be deleted");
    }

    /**
     *
     * @param idRol
     * @param permission
     * @return
     */
    public ResponseEntity<Boolean> validateGrant(int idRol, Permission permission){
        boolean hasGrant = false;
        Optional<Rol> rol = this.rolRepository.findById(idRol);
        if(rol.isPresent()){
            for (Permission rolPermission: rol.get().getPermissions()){
                if (rolPermission.getUrl().equals(permission.getUrl()) &&
                    rolPermission.getMethod().equals(permission.getMethod())){
                    hasGrant = true;
                    break;
                }
            }
            if (hasGrant)
                return new ResponseEntity<>(true, HttpStatus.OK);
            else
                return new ResponseEntity<>(false, HttpStatus.UNAUTHORIZED);
        }
        else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Provided rol id does not exits");
    }


}
