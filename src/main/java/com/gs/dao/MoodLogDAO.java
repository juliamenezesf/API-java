package com.gs.dao;

import com.gs.model.MoodLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MoodLogDAO {

    @Inject
    DataSource dataSource;

    private void ensureTable() {
        // tabela já existe no Oracle
    }

    private Long nextId(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("select nvl(max(id_mood), 0) + 1 from mood_logs");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    // ===== NOVO: usado por MoodLogResource.list() =====
    public List<MoodLog> findAll() {
        ensureTable();
        String sql = """
            select id_mood, id_user, score, note, stress_score, logged_at
              from mood_logs
             order by logged_at desc
            """;

        List<MoodLog> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar mood logs", e);
        }

        return list;
    }

    // ===== JÁ TINHA: buscar por usuário =====
    public List<MoodLog> findByUser(Long userId) {
        ensureTable();
        String sql = """
            select id_mood, id_user, score, note, stress_score, logged_at
              from mood_logs
             where id_user = ?
             order by logged_at desc
            """;

        List<MoodLog> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar mood logs por usuário", e);
        }

        return list;
    }

    // ===== NOVO: usado por MoodLogResource.getById() =====
    public MoodLog findById(Long id) {
        ensureTable();
        String sql = """
            select id_mood, id_user, score, note, stress_score, logged_at
              from mood_logs
             where id_mood = ?
            """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar mood log", e);
        }

        return null;
    }

    public Long insert(MoodLog m) {
        ensureTable();
        String sql = """
            insert into mood_logs
              (id_mood, id_user, score, note, stress_score, logged_at)
            values
              (?,?,?,?,?,?)
            """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            Long id = nextId(con);
            ps.setLong(1, id);
            ps.setLong(2, m.getUserId());
            ps.setInt(3, m.getScore());
            ps.setString(4, m.getNote());
            ps.setInt(5, m.getStressScore());

            OffsetDateTime loggedAt = m.getLoggedAt();
            if (loggedAt != null) {
                ps.setTimestamp(6, Timestamp.from(loggedAt.toInstant()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }

            ps.executeUpdate();
            return id;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir mood log", e);
        }
    }

    // ===== NOVO: usado por MoodLogResource.delete() =====
    public void delete(Long id) {
        ensureTable();
        String sql = "delete from mood_logs where id_mood = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar mood log", e);
        }
    }

    private MoodLog map(ResultSet rs) throws SQLException {
        MoodLog m = new MoodLog();
        m.setId(rs.getLong("id_mood"));
        m.setUserId(rs.getLong("id_user"));
        m.setScore(rs.getInt("score"));
        m.setNote(rs.getString("note"));
        m.setStressScore(rs.getInt("stress_score"));

        Timestamp ts = rs.getTimestamp("logged_at");
        if (ts != null) {
            m.setLoggedAt(ts.toInstant().atOffset(ZoneOffset.UTC));
        }

        return m;
    }
}
