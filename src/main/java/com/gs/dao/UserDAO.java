package com.gs.dao;

import com.gs.model.User;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserDAO {

    @Inject
    DataSource dataSource;

    // SQL de criação da tabela (H2)
    private static final String CREATE_TABLE_SQL = """
        create table if not exists users(
          id bigint auto_increment primary key,
          name varchar(120) not null,
          email varchar(120) not null unique,
          role varchar(40) not null,
          created_at timestamp not null
        )
        """;

    // Garante que a tabela existe antes de usar
    private void ensureTable() {
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement()) {

            st.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao garantir tabela users", e);
        }
    }

    public List<User> findAll() {
        ensureTable(); // <<< garante tabela
        String sql = "select id,name,email,role,created_at from users order by id desc";
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
        ensureTable(); // <<< garante tabela
        String sql = "select id,name,email,role,created_at from users where id = ?";

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
        ensureTable(); // <<< garante tabela
        String sql = "insert into users(name, email, role, created_at) values(?,?,?,?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getRole());
            ps.setTimestamp(4, Timestamp.from(u.getCreatedAt().toInstant()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        } catch (SQLException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("unique")) {
                throw new IllegalArgumentException("email já cadastrado");
            }
            throw new RuntimeException("Erro ao inserir usuário", e);
        }

        return null;
    }

    public void delete(Long id) {
        ensureTable(); // <<< garante tabela
        String sql = "delete from users where id = ?";
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
        u.setId(rs.getLong("id"));
        u.setName(rs.getString("name"));
        u.setEmail(rs.getString("email"));
        u.setRole(rs.getString("role"));
        Timestamp ts = rs.getTimestamp("created_at");
        u.setCreatedAt(ts.toInstant().atOffset(ZoneOffset.UTC));
        return u;
    }
}
