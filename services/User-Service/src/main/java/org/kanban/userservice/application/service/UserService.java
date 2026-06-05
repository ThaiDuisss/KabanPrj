package org.kanban.userservice.application.service;

import lombok.RequiredArgsConstructor;
import org.kanban.userservice.application.dto.RegisterUserCommand;
import org.kanban.userservice.application.dto.UpdateUserCommand;
import org.kanban.userservice.application.dto.UserResponse;
import org.kanban.userservice.application.port.in.CreateUserUseCase;
import org.kanban.userservice.application.port.in.QueryUserUseCase;
import org.kanban.userservice.application.port.in.UpdateUserUseCase;
import org.kanban.userservice.domain.exception.UserDomainException;
import org.kanban.userservice.domain.model.*;
import org.kanban.userservice.domain.repository.UserRepository;

import java.util.Optional;

@RequiredArgsConstructor
public class UserService implements CreateUserUseCase, UpdateUserUseCase, QueryUserUseCase {

    private final UserRepository userRepository;

    @Override
    public UserResponse registerUser(RegisterUserCommand command) {
        Username username = new Username(command.getUsername());
        Email email = new Email(command.getEmail());

        if (userRepository.existsByUsername(username)) {
            throw new UserDomainException("Username is already taken");
        }
        if (userRepository.existsByEmail(email)) {
            throw new UserDomainException("Email is already registered");
        }

        // Simulating password hashing (e.g. prefixing for plain-text placeholder)
        PasswordHash passwordHash = new PasswordHash("{noop}" + command.getPassword());
        FullName fullName = new FullName(command.getFullName());
        AvatarUrl avatarUrl = new AvatarUrl(command.getAvatarUrl());

        User user = User.createNew(username, email, passwordHash, fullName, avatarUrl);
        User savedUser = userRepository.save(user);

        return UserResponse.fromDomain(savedUser);
    }

    @Override
    public UserResponse updateProfile(Long id, UpdateUserCommand command) {
        User user = userRepository.findById(new UserId(id))
                .orElseThrow(() -> new UserDomainException("User not found with id: " + id));

        FullName fullName = new FullName(command.getFullName());
        AvatarUrl avatarUrl = new AvatarUrl(command.getAvatarUrl());

        user.updateProfile(fullName, avatarUrl);
        User updatedUser = userRepository.save(user);

        return UserResponse.fromDomain(updatedUser);
    }

    @Override
    public Optional<UserResponse> getUserById(Long id) {
        return userRepository.findById(new UserId(id))
                .map(UserResponse::fromDomain);
    }

    @Override
    public Optional<UserResponse> getUserByEmail(String email) {
        return userRepository.findByEmail(new Email(email))
                .map(UserResponse::fromDomain);
    }

    @Override
    public Optional<UserResponse> getUserByUsername(String username) {
        return userRepository.findByUsername(new Username(username))
                .map(UserResponse::fromDomain);
    }
}
