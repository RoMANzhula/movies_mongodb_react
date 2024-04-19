package org.romanzhula.movies_mongodb_react.controllers.respons;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserInfoResponse {

    private String id;

    private String username;

    private String email;

    private List<String> roles;
}
