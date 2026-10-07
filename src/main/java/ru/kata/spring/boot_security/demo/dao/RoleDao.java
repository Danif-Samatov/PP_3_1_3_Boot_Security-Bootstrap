package ru.kata.spring.boot_security.demo.dao;

import ru.kata.spring.boot_security.demo.model.Role;

import java.util.Collection;
import java.util.List;

public interface RoleDao {
    void save(Role role);

    List<Role> findByIds(Collection<Long> ids);

    List<Role> listRoles();
}
