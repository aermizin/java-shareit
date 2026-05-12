package ru.practicum.shareIt.user.dao;

import ru.practicum.shareIt.user.User;

import java.util.Collection;
import java.util.Optional;

public interface UserDao {

    Collection<User> getAllUsers();

    Optional<User> getUser(Long id);

    User createUser(User newUser);

    User updatedUser(User updatedUser);

    void deleteUser(Long id);

    Boolean checkEmail(User user);
}
