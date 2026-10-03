package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void add(String firstName, String lastName, Integer age, String email, String rawPassword, List<Long> roleIds) {
        validate(firstName, lastName, age, email);
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("Enter a password");
        }
        checkEmailUnique(email, null);

        User user = new User(firstName, lastName, age, email, passwordEncoder.encode(rawPassword));
        user.setRoles(resolveRoles(roleIds));
        userDao.add(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> listUsers() {
        return userDao.listUsers();
    }

    @Transactional(readOnly = true)
    @Override
    public User getById(Long id) {
        return userDao.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Transactional
    @Override
    public void update(Long id, String firstName, String lastName, Integer age, String email, String rawPassword, List<Long> roleIds) {
        validate(firstName, lastName, age, email);
        User user = userDao.getById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        checkEmailUnique(email, id);

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setEmail(email);
        if (rawPassword != null && !rawPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }
        user.setRoles(resolveRoles(roleIds));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        userDao.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDao.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        user.getAuthorities().size();
        return user;
    }

    private void validate(String firstName, String lastName, Integer age, String email) {
        if (firstName == null || firstName.isBlank()
                || lastName == null || lastName.isBlank()
                || email == null || email.isBlank()
                || age == null || age < 0) {
            throw new IllegalArgumentException("Fill in all fields");
        }
    }

    private void checkEmailUnique(String email, Long currentId) {
        User existing = userDao.findByUsername(email);
        if (existing != null && (currentId == null || !existing.getId().equals(currentId))) {
            throw new IllegalArgumentException("User with this email already exists");
        }
    }

    private Set<Role> resolveRoles(List<Long> roleIds) {
        Set<Role> roles = new HashSet<>(roleDao.findByIds(roleIds));
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("Select at least one role");
        }
        return roles;
    }
}
