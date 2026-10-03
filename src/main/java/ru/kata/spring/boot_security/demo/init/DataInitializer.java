package ru.kata.spring.boot_security.demo.init;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.dao.RoleDao;
import ru.kata.spring.boot_security.demo.dao.UserDao;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements ApplicationRunner {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserDao userDao, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        Role adminRole = getOrCreate("ROLE_ADMIN");
        Role userRole = getOrCreate("ROLE_USER");

        ensureUser("admin", "admin", 35, "admin@mail.ru", "admin",
                new HashSet<>(Set.of(adminRole, userRole)));
        ensureUser("user", "user", 30, "user@mail.ru", "user",
                new HashSet<>(Set.of(userRole)));
    }

    private void ensureUser(String firstName, String lastName, int age, String email, String rawPassword, Set<Role> roles) {
        User user = userDao.findByUsername(email);
        if (user == null) {
            user = new User(firstName, lastName, age, email, passwordEncoder.encode(rawPassword));
            user.setRoles(roles);
            userDao.add(user);
            return;
        }
        if (user.getAge() == null) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setAge(age);
        }
    }

    private Role getOrCreate(String name) {
        Role role = roleDao.findByName(name);
        if (role == null) {
            role = new Role(name);
            roleDao.save(role);
        }
        return role;
    }
}
