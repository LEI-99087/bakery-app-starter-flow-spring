package com.vaadin.starter.bakery.app.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.backend.repositories.UserRepository;

/**
 * Implements the {@link UserDetailsService} for authenticating users.
 *
 * <p>This implementation searches for {@link User} entities by the e-mail address
 * supplied in the login screen and creates Spring Security UserDetails objects
 * for authentication and authorization.</p>
 *
 * @see UserDetailsService
 * @see User
 * @see UserRepository
 */
@Service
@Primary
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs a new UserDetailsServiceImpl with the specified user repository.
     *
     * @param userRepository the repository used to find users by email
     */
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Provides a password encoder bean for encrypting and verifying passwords.
     * Uses BCrypt strong hashing for secure password storage.
     *
     * @return a BCryptPasswordEncoder instance for password encoding
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Loads user details by username (email address) for authentication.
     *
     * <p>Recovers the {@link User} from the database using the e-mail address supplied
     * in the login screen. If the user is found, returns a Spring Security
     * {@link org.springframework.security.core.userdetails.User} object with
     * the user's credentials and role-based authorities.</p>
     *
     * @param username the user's e-mail address used for login
     * @return UserDetails object containing user credentials and authorities
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(username);
        if (null == user) {
            throw new UsernameNotFoundException("No user present with username: " + username);
        } else {
            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPasswordHash(),
                    Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole())));
        }
    }
}