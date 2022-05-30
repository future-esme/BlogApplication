package com.esempla.test.demo.service;

import com.esempla.test.demo.domain.User;
import com.esempla.test.demo.domain.UserRole;
import com.esempla.test.demo.domain.UserStatus;
import com.esempla.test.demo.repository.UserRepository;
import com.esempla.test.demo.repository.UserRoleRepository;
import com.esempla.test.demo.security.AuthoritiesConstants;
import com.esempla.test.demo.service.dto.UserDto;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    public UserService(PasswordEncoder passwordEncoder, UserRepository userRepository, UserRoleRepository userRoleRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
    }

    public User createUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setStatus(new UserStatus("Active"));
        if (dto.getEmail() != null) {
            user.setEmail(dto.getEmail().toLowerCase());
        }
        Set<UserRole> userRoleSet = new HashSet<>();
        userRoleSet.add(new UserRole(AuthoritiesConstants.USER));
        user.setAuthorities(userRoleSet);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        return user;
    }
    public Optional<User> replace(UserDto dto, Long id) {
        return Optional
                .of(userRepository.findById(id))
                .map(Optional::get)
                .map(user -> {
                    user.setUsername(dto.getUsername());
                    user.setFirstName(dto.getFirstName());
                    user.setLastName(dto.getLastName());
                    user.setStatus(new UserStatus("Active"));
                    if (dto.getEmail() != null) {
                        user.setEmail(dto.getEmail().toLowerCase());
                    }
                    user.setPassword(dto.getPassword());
                    return user;
                })
                .map(User::new);
    }

    public Optional<User> update(UserDto dto, Long id) {
        return Optional
                .of(userRepository.findById(id))
                .map(Optional::get)
                .map(user -> {
                    if (dto.getUsername() != null) {
                        user.setUsername(dto.getUsername());
                    }
                    if (dto.getFirstName() != null) {
                        user.setFirstName(dto.getFirstName());
                    }
                    if (dto.getLastName() != null) {
                        user.setLastName(dto.getLastName());
                    }
                    if (dto.getEmail() != null) {
                        user.setEmail(dto.getEmail().toLowerCase());
                    }
                    return user;
                })
                .map(User::new);
    }
}
