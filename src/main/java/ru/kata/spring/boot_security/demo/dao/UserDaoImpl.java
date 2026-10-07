package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void add(User user) {
        entityManager.persist(user);
    }

    @Override
    public List<User> listUsers() {
        return entityManager.createQuery(
                        "select distinct u from User u left join fetch u.roles order by u.id", User.class)
                .getResultList();
    }

    @Override
    public User getById(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User findByUsername(String username) {
        return entityManager.createQuery(
                        "select distinct u from User u left join fetch u.roles where u.email = :email", User.class)
                .setParameter("email", username)
                .getResultStream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public void delete(Long id) {
        User user = getById(id);
        if (user != null) {
            entityManager.remove(user);
        }
    }
}
