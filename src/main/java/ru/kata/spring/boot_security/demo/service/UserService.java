package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void add(User user, List<Long> roleIds);

    List<User> listUsers();

    User findByUsername(String username);

    void update(User user, List<Long> roleIds);

    void delete(Long id);
}
