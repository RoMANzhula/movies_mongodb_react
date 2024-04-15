package org.romanzhula.movies_mongodb_react.repositories;

import org.bson.types.ObjectId;
import org.romanzhula.movies_mongodb_react.models.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends MongoRepository<Movie, ObjectId> {

}
