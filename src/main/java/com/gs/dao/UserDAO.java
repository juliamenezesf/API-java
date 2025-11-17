package com.gs.dao;

import com.gs.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserDAO {

    @Inject
    DataSource dataSource;

    // Tabela já existe no Oracle (usada pelo Python) – não criamos nada aqui
    private void ensureTable() {
        // vazio de propósito
    }

    // Gera próximo ID usando MAX(ID_USER)+1 (sem mexer na estrutura do banco)
    private Long nextId(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("select nvl(max(id_user), 0) + 1 from users");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    public List<User> findAll() {
        ensureTable();
        String sql = "select id_user, name, email, role, created_at from users order by id_user desc";
        List<User> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar usuários", e);
        }

        return list;
    }

    public User findById(Long id) {
        ensureTable();
        String sql = "select id_user, name, email, role, created_at from users where id_user = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar usuário", e);
        }

        return null;
    }

    public Long insert(User u) {
        ensureTable();
        String sql = "insert into users (id_user, name, email, role, created_at) values (?,?,?,?,?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            Long id = nextId(con);          // gera ID_USER
            ps.setLong(1, id);
            ps.setString(2, u.getName());
            ps.setString(3, u.getEmail());

            // ROLE precisa respeitar CK_USERS_ROLE: EMPLOYEE / MANAGER / ADMIN
            String role = u.getRole();
            if (role != null) {
                role = role.trim().toUpperCase();
            }
            if (!"EMPLOYEE".equals(role) &&
                    !"MANAGER".equals(role) &&
                    !"ADMIN".equals(role)) {
                throw new IllegalArgumentException("role inválido: " + role);
            }
            ps.setString(4, role);

            OffsetDateTime createdAt = u.getCreatedAt();
            if (createdAt != null) {
                ps.setTimestamp(5, Timestamp.from(createdAt.toInstant()));
            } else {
                ps.setTimestamp(5, Timestamp.from(OffsetDateTime.now(ZoneOffset.UTC).toInstant()));
            }

            ps.executeUpdate();
            return id;

        } catch (SQLException e) {
            // ORA-00001 (unique) – provavelmente UK_USERS_EMAIL
            if (e.getErrorCode() == 1) {
                throw new IllegalArgumentException("email já cadastrado");
            }
            throw new RuntimeException("Erro ao inserir usuário", e);
        }
    }

    public void delete(Long id) {
        ensureTable();
        String sql = "delete from users where id_user = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar usuário", e);
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getLong("id_user"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            u.setCreatedAt(ts.toInstant().atOffset(ZoneOffset.UTC));
        }

        return u;
    }
}
