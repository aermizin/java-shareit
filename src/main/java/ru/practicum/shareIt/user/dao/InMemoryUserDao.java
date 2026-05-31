package ru.practicum.shareIt.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareIt.user.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class InMemoryUserDao implements UserDao {
    private final Map<Long, User> users = new HashMap<>();

    private long nextUserId = 1;

    @Override
    public Collection<User> getAllUsers() {
        return users.values();
    }

    @Override
    public Optional<User> getUser(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public User createUser(User newUser) {
        newUser.setId(nextUserId++);
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public User updatedUser(User user) {
        User updatedUser = users.get(user.getId());

        if (user.getName() != null) {
            updatedUser.setName(user.getName());
        }

        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        }
        return updatedUser;
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    public Boolean checkEmail(User validationUser) {
        return users.values().stream()
                .filter(user -> !user.getId().equals(validationUser.getId()))
                .anyMatch(user -> user.getEmail().equals(validationUser.getEmail()));
    }
}
