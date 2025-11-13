package com.gs.bo;

import com.gs.dao.UserDAO;
import com.gs.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;

@ApplicationScoped
public class UserBO {

    @Inject
    UserDAO userDAO;

    public Long create(String name, String email, String role) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("email inválido");
        }

        User u = new User();
        u.setName(name.trim());
        u.setEmail(email.trim().toLowerCase());
        u.setRole(role.trim().toLowerCase());
        u.setCreatedAt(OffsetDateTime.now());

        return userDAO.insert(u);
    }
}
