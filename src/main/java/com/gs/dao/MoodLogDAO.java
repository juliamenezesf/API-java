package com.gs.dao;

import com.gs.model.MoodLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MoodLogDAO {

    @Inject
    DataSource dataSource;

    private static final String CREATE_TABLE = """
        create table if not exists mood_logs(
          id bigint auto_increment primary key,
          user_id bigint not null,
          score int not null,
          note varchar(255),
          stress_score int,
          logged_at timestamp not null
        )
        """;

    private void ensureTable() {
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement()) {
            st.execute(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao criar tabela mood_logs", e);
        }
    }

    public List<MoodLog> findAll() {
        ensureTable();
        String sql = "select id,user_id,score,note,stress_score,logged_at from mood_logs order by id desc";

        List<MoodLog> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar mood logs", e);
        }
    }

    public MoodLog findById(Long id) {
        ensureTable();
        String sql = "select id,user_id,score,note,stress_score,logged_at from mood_logs where id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar mood log", e);
        }
    }

    public Long insert(MoodLog m) {
        ensureTable();
        String sql = "insert into mood_logs(user_id,score,note,stress_score,logged_at) values(?,?,?,?,?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, m.getUserId());
            ps.setInt(2, m.getScore());
            ps.setString(3, m.getNote());
            ps.setInt(4, m.getStressScore() == null ? 0 : m.getStressScore());
            ps.setTimestamp(5, Timestamp.from(m.getLoggedAt().toInstant()));

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir mood log", e);
        }
    }

    public void delete(Long id) {
        ensureTable();
        String sql = "delete from mood_logs where id = ?";

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
        m.setId(rs.getLong("id"));
        m.setUserId(rs.getLong("user_id"));
        m.setScore(rs.getInt("score"));
        m.setNote(rs.getString("note"));
        m.setStressScore(rs.getInt("stress_score"));
        Timestamp ts = rs.getTimestamp("logged_at");
        m.setLoggedAt(ts.toInstant().atOffset(ZoneOffset.UTC));
        return m;
    }

}
