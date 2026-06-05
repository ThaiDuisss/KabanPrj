package org.kanban.userservice.infrastructure.adapter.out.persistence;

import lombok.RequiredArgsConstructor;
import org.kanban.userservice.domain.model.Email;
import org.kanban.userservice.domain.model.User;
import org.kanban.userservice.domain.model.UserId;
import org.kanban.userservice.domain.model.Username;
import org.kanban.userservice.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public User save(User user) {
        UserJpaEntity entity = UserJpaEntity.fromDomain(user);
        UserJpaEntity savedEntity = jpaUserRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaUserRepository.findById(id.getValue())
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return jpaUserRepository.findByUsername(username.getValue())
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaUserRepository.findByEmail(email.getValue())
                .map(UserJpaEntity::toDomain);
    }

    @Override
    public boolean existsByUsername(Username username) {
        return jpaUserRepository.existsByUsername(username.getValue());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaUserRepository.existsByEmail(email.getValue());
    }

    @Override
    public void delete(UserId id) {
        jpaUserRepository.deleteById(id.getValue());
    }
}
