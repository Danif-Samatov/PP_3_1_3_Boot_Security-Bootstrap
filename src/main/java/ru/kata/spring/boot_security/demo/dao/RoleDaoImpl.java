package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.Role;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Role role) {
        entityManager.persist(role);
    }

    @Override
    public List<Role> findByIds(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Collections.emptyList();
        }
        return entityManager.createQuery("select r from Role r where r.id in :ids", Role.class)
                .setParameter("ids", ids)
                .getResultList();
    }

    @Override
    public List<Role> listRoles() {
        return entityManager.createQuery("select r from Role r order by r.name", Role.class)
                .getResultList();
    }
}
