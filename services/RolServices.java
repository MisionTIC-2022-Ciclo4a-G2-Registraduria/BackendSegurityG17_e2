package com.misiontic.grupo17.securityBackend.services;

import com.misiontic.grupo17.securityBackend.models.Rol;
import com.misiontic.grupo17.securityBackend.repositories.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolServices {
    @Autowired
    private RolRepository rolRepository;

    /**
     *
     * @return
     */
    public List<Rol> index(){
        return (List<Rol>)this.rolRepository.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Rol> show(int id){
        return this.rolRepository.findById(id);
    }

    /**
     *
     * @param newRol
     * @return
     */
    public Rol create(Rol newRol){
        if(newRol.getId() == null){
            if(newRol.getName() != null)
                return this.rolRepository.save(newRol);
            else {
                // TODO 400 BadRequest, non mandatory field
                return newRol;
            }
        }
        else {
            // TODO validate id, 400 BadRequest
            return newRol;
        }
    }

    /**
     *
     * @param id
     * @param updateRol
     * @return
     */
    public Rol update(int id, Rol updateRol){
        if(id > 0){
            Optional<Rol> tempRol = this.show(id);
            if(tempRol.isPresent()){
                if(updateRol.getName() != null)
                    tempRol.get().setName(updateRol.getName());
                if(updateRol.getDescription() != null)
                    tempRol.get().setDescription(updateRol.getDescription());
                return this.rolRepository.save(tempRol.get());
            }
            else {
                // TODO 404 Not Found
                return updateRol;
            }
        }
        else{
            // TODO BadRequest 400, i<=0
            return updateRol;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean delete(int id){
        Boolean success = this.show(id).map(rol -> {
            this.rolRepository.delete(rol);
            return true;
        }).orElse(false);
        return success;
    }
}
