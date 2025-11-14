package com.gs.dao;

import com.gs.model.Recommendation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RecommendationDAO {

    @Inject
    DataSource dataSource;

    private static final String CREATE_TABLE = """
        create table if not exists break_recommendations(
          id bigint auto_increment primary key,
          user_id bigint not null,
          mood_id bigint not null,
          kind varchar(30) not null,
          minutes int not null,
          created_at timestamp not null,
          accepted char(1) default 'N'
        )
        """;

    private void ensureTable() {
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement()) {
            st.execute(CREATE_TABLE);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao garantir tabela break_recommendations", e);
        }
    }

    public List<Recommendation> findAll() {
        ensureTable();
        String sql = "select id,user_id,mood_id,kind,minutes,created_at,accepted from break_recommendations order by id desc";
        List<Recommendation> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar recomendações", e);
        }
    }

    public Recommendation findById(Long id) {
        ensureTable();
        String sql = "select id,user_id,mood_id,kind,minutes,created_at,accepted from break_recommendations where id = ?";

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
            throw new RuntimeException("Erro ao buscar recomendação", e);
        }
    }

    public Long insert(Recommendation r) {
        ensureTable();
        String sql = """
            insert into break_recommendations(user_id,mood_id,kind,minutes,created_at,accepted)
            values(?,?,?,?,?,?)
            """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, r.getUserId());
            ps.setLong(2, r.getMoodId());
            ps.setString(3, r.getKind());
            ps.setInt(4, r.getMinutes());
            ps.setTimestamp(5, Timestamp.from(r.getCreatedAt().toInstant()));
            ps.setString(6, Boolean.TRUE.equals(r.getAccepted()) ? "Y" : "N");

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getLong(1);
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir recomendação", e);
        }
    }

    public void delete(Long id) {
        ensureTable();
        String sql = "delete from break_recommendations where id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar recomendação", e);
        }
    }

    private Recommendation map(ResultSet rs) throws SQLException {
        Recommendation r = new Recommendation();
        r.setId(rs.getLong("id"));
        r.setUserId(rs.getLong("user_id"));
        r.setMoodId(rs.getLong("mood_id"));
        r.setKind(rs.getString("kind"));
        r.setMinutes(rs.getInt("minutes"));
        Timestamp ts = rs.getTimestamp("created_at");
        r.setCreatedAt(ts.toInstant().atOffset(ZoneOffset.UTC));
        String accepted = rs.getString("accepted");
        r.setAccepted("Y".equalsIgnoreCase(accepted));
        return r;
    }
}
