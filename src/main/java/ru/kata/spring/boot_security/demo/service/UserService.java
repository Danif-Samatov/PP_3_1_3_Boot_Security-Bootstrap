package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService {
    void add(String firstName, String lastName, Integer age, String email, String rawPassword, List<Long> roleIds);

    List<User> listUsers();

    User getById(Long id);

    User findByUsername(String username);

    void update(Long id, String firstName, String lastName, Integer age, String email, String rawPassword, List<Long> roleIds);

    void delete(Long id);
}
