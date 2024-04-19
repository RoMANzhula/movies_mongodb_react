package org.romanzhula.movies_mongodb_react.repositories;

import org.bson.types.ObjectId;
import org.romanzhula.movies_mongodb_react.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, ObjectId> {

}
