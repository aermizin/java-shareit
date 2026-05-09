package ru.practicum.shareIt.user.dao;

import ru.practicum.shareIt.user.User;

import java.util.Collection;

public interface UserDao {
    Collection<User> getAllUsers();
    User getUser(Long id);
    User createUser(User newUser);
    User updatedUser(User updatedUser);
    Boolean checkEmail(User user);
    void deleteUser(Long id);
}
