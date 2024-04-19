package org.romanzhula.movies_mongodb_react.changeLog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Arrays;
import java.util.List;

@ChangeLog(order = "001")
public class RolesMigration {

    @ChangeSet(order = "001", id = "insertRoles", author = "user")
    public void insertRoles(MongoDatabase mongoDatabase) {
        List<Document> roles = Arrays.asList(
          new Document("name", "ROLE_USER"),
          new Document("name", "ROLE_MODERATOR"),
          new Document("name", "ROLE_ADMIN")
        );

        mongoDatabase
                .getCollection("roles")
                .insertMany(roles)
        ;
    }
}
