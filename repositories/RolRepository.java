package com.misiontic.grupo2.registraduria.repositories;

import com.misiontic.grupo2.registraduria.models.Rol;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends CrudRepository<Rol, Integer> {
}
