package org.oem.pinggo.repository;

import jakarta.persistence.PersistenceContext;
import org.oem.pinggo.entity.User;
import org.springframework.stereotype.Repository;
import jakarta.persistence.*;

import java.util.List;

@Repository
    class ComplexRepo {

        @PersistenceContext
        private EntityManager entityManager;

        public List<User> findUserLimitedTo(int limit) {
            return entityManager.createQuery("SELECT p FROM User p ",
                    User.class).setMaxResults(limit).getResultList();
        }
    }


