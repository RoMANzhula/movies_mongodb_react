package org.romanzhula.movies_mongodb_react.repositories;

import org.romanzhula.movies_mongodb_react.models.Role;
import org.romanzhula.movies_mongodb_react.models.enums.EnumRole;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
    Optional<Role> findByName(EnumRole name);
}
