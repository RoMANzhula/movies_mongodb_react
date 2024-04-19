package org.romanzhula.movies_mongodb_react.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.romanzhula.movies_mongodb_react.configurations.security.implementations.UserDetailsImpl;
import org.romanzhula.movies_mongodb_react.configurations.security.jwt.components.JwtUtils;
import org.romanzhula.movies_mongodb_react.controllers.requests.LoginRequest;
import org.romanzhula.movies_mongodb_react.controllers.requests.RegisterRequest;
import org.romanzhula.movies_mongodb_react.controllers.respons.MessageResponse;
import org.romanzhula.movies_mongodb_react.controllers.respons.UserInfoResponse;
import org.romanzhula.movies_mongodb_react.models.Role;
import org.romanzhula.movies_mongodb_react.models.User;
import org.romanzhula.movies_mongodb_react.models.enums.EnumRole;
import org.romanzhula.movies_mongodb_react.repositories.RoleRepository;
import org.romanzhula.movies_mongodb_react.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @PostMapping("/sign_in")
    public ResponseEntity<?> authenticatedUser(
            @Valid
            @RequestBody LoginRequest loginRequest
    ) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles
                ));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(
            @Valid
            @RequestBody RegisterRequest registerRequest
    ) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {

            logger.error("ERROR: Username is already exists! {}", registerRequest.getUsername());

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Username is already exists!"));
        }

        if (userRepository.existsByEmail(registerRequest.getEmail())) {

            logger.error("ERROR: Email is already using! {}", registerRequest.getEmail());

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("ERROR: Email is already using!"));

        }

        User user = new User(
                registerRequest.getUsername(),
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword())
        );

        Set<String> setRoles = registerRequest.getRoles();
        Set<Role> roles = new HashSet<>();

        Role defaultRole = roleRepository.findByName(EnumRole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("ERROR: Role is NOT FOUND!"));

        if (setRoles != null) {
            setRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(EnumRole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("ERROR: Role is NOT FOUND!"));
                        roles.add(adminRole);
                        break;

                    case "moderator":
                        Role moderatorRole = roleRepository.findByName(EnumRole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("ERROR: Role is NOT FOUND!"));
                        roles.add(moderatorRole);
                        break;

                    default:
                        roles.add(defaultRole);
                }
            });
        } else {
            roles.add(defaultRole);
        }

        user.setRoles(roles);

        userRepository.save(user);

        return ResponseEntity.ok(
                new MessageResponse("User registered successfully!")
        );

    }

}
