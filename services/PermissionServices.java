package com.misiontic.grupo17.securityBackend.services;

import com.misiontic.grupo17.securityBackend.models.Permission;
import com.misiontic.grupo17.securityBackend.repositories.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionServices {
    @Autowired
    private PermissionRepository permissionRepository;

    /**
     *
     * @return
     */
    public List<Permission> index(){
        return (List<Permission>)this.permissionRepository.findAll();
    }

    /**
     *
     * @param id
     * @return
     */
    public Optional<Permission> show(int id){
        return this.permissionRepository.findById(id);
    }

    /**
     *
     * @param newPermission
     * @return
     */
    public Permission create(Permission newPermission) {
        if (newPermission.getId() == null) {
            if (newPermission.getUrl() != null && newPermission.getMethod() != null)
                return this.permissionRepository.save(newPermission);
            else {
                // TODO 400 BadRequest, non-mandatory fields
                return newPermission;
            }
        }
        else {
            // TODO validate if exists, BadRequest 400
            return newPermission;
        }
    }

    /**
     *
     * @param id
     * @param updatePermission
     * @return
     */
    public Permission update(int id, Permission updatePermission){
        if(id > 0){
            Optional<Permission> tempPermission = this.show(id);
            if(tempPermission.isPresent()){
                if(updatePermission.getMethod() != null)
                    tempPermission.get().setMethod(updatePermission.getMethod());
                if(updatePermission.getUrl() != null)
                    tempPermission.get().setUrl(updatePermission.getUrl());
                return this.permissionRepository.save(tempPermission.get());
            }
            else {
                // TODO 404 NotFound
                    return updatePermission;
            }
        }
        else {
            // TODO 400 BadRequest, Id <= 0
            return updatePermission;
        }
    }

    /**
     *
     * @param id
     * @return
     */
    public boolean delete(int id){
        Boolean success = this.show(id).map(permission -> {
            this.permissionRepository.delete(permission);
            return true;
        }).orElse(false);
        return success;
    }
}
