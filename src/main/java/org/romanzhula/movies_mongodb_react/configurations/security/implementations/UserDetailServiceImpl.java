package org.romanzhula.movies_mongodb_react.configurations.security.implementations;

import lombok.RequiredArgsConstructor;
import org.romanzhula.movies_mongodb_react.models.User;
import org.romanzhula.movies_mongodb_react.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User Not Found: " + username)
                );

        return UserDetailsImpl.build(user);
    }
}
