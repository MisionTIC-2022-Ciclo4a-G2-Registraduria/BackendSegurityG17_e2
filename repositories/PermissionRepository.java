package com.misiontic.grupo2.registraduria.repositories;

import com.misiontic.grupo2.registraduria.models.Permission;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends CrudRepository<Permission, Integer> {
}
