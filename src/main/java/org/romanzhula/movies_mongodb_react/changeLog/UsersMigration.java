package org.romanzhula.movies_mongodb_react.changeLog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.Collections;

@ChangeLog(order = "002")
public class UsersMigration {

    @ChangeSet(order = "002", id = "insertUsers", author = "user")
    public void insertUsers(MongoDatabase mongoDatabase) {

        Document roleUser = mongoDatabase
                .getCollection("roles")
                .find(new Document("name", "ROLE_USER"))
                .first()
        ;

        Document roleModerator = mongoDatabase
                .getCollection("roles")
                .find(new Document("name", "ROLE_MODERATOR"))
                .first()
        ;

        Document roleAdmin = mongoDatabase
                .getCollection("roles")
                .find(new Document("name", "ROLE_ADMIN"))
                .first()
        ;

        if (roleUser != null && roleModerator != null && roleAdmin != null) {
            Document user1 = new Document("username", "user")
                    .append("email", "user1@example.com")
                    .append("password", "password1")
                    .append("phone", "123456789")
                    .append("roles", Collections
                            .singletonList(
                                    new Document("$ref", "roles")
                                            .append("$id", roleUser.getObjectId("_id"))
                            )
                    );

            Document user2 = new Document("username", "moderator")
                    .append("email", "moderator1@example.com")
                    .append("password", "password2")
                    .append("phone", "987654321")
                    .append("roles", Collections
                            .singletonList(
                                    new Document("$ref", "roles")
                                            .append("$id", roleModerator.getObjectId("_id"))
                            )
                    );

            Document user3 = new Document("username", "admin")
                    .append("email", "admin1@example.com")
                    .append("password", "password3")
                    .append("phone", "987123654")
                    .append("roles", Collections
                            .singletonList(
                                    new Document("$ref", "roles")
                                            .append("$id", roleAdmin.getObjectId("_id"))
                            )
                    );

            mongoDatabase
                    .getCollection("users")
                    .insertMany(Arrays.asList(user1, user2, user3))
            ;

        } else {
            throw new IllegalStateException("Roles not found!");
        }
    }

}
