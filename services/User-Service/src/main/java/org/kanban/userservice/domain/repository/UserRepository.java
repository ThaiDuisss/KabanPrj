package org.kanban.userservice.domain.repository;

import org.kanban.userservice.domain.model.Email;
import org.kanban.userservice.domain.model.User;
import org.kanban.userservice.domain.model.UserId;
import org.kanban.userservice.domain.model.Username;

import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UserId id);
    Optional<User> findByUsername(Username username);
    Optional<User> findByEmail(Email email);
    boolean existsByUsername(Username username);
    boolean existsByEmail(Email email);
    void delete(UserId id);
}
