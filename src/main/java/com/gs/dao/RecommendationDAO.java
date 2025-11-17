package com.gs.dao;

import com.gs.model.Recommendation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class RecommendationDAO {

    @Inject
    DataSource dataSource;

    // Não mexemos em tabela – ela já existe no Oracle
    private void ensureTable() {
        // vazio de propósito
    }

    // Gera próximo ID via MAX(id_rec)+1, sem mexer em sequence/estrutura do banco
    private Long nextId(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(
                "select nvl(max(id_rec), 0) + 1 from break_recommendations");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    // ===== NOVO: usado pelo RecommendationResource.list() =====
    public List<Recommendation> findAll() {
        ensureTable();
        String sql = """
            select id_rec,
                   id_user,
                   id_mood,
                   kind,
                   minutes,
                   created_at,
                   accepted
              from break_recommendations
             order by created_at desc
            """;

        List<Recommendation> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar recomendações", e);
        }

        return list;
    }

    // ===== JÁ EXISTENTE: buscar por usuário =====
    public List<Recommendation> findByUser(Long userId) {
        ensureTable();
        String sql = """
            select id_rec,
                   id_user,
                   id_mood,
                   kind,
                   minutes,
                   created_at,
                   accepted
              from break_recommendations
             where id_user = ?
             order by created_at desc
            """;

        List<Recommendation> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar recomendações", e);
        }

        return list;
    }

    public Long insert(Recommendation r) {
        ensureTable();
        String sql = """
            insert into break_recommendations
              (id_rec, id_user, id_mood, kind, minutes, created_at, accepted)
            values
              (?,?,?,?,?,?,?)
            """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            Long id = nextId(con);
            ps.setLong(1, id);
            ps.setLong(2, r.getUserId());
            ps.setLong(3, r.getMoodId());
            ps.setString(4, r.getKind());
            ps.setInt(5, r.getMinutes());

            OffsetDateTime createdAt = r.getCreatedAt();
            if (createdAt != null) {
                ps.setTimestamp(6, Timestamp.from(createdAt.toInstant()));
            } else {
                ps.setNull(6, Types.TIMESTAMP);
            }

            Boolean accepted = r.getAccepted(); // assume getter getAccepted()
            ps.setString(7, Boolean.TRUE.equals(accepted) ? "Y" : "N");

            ps.executeUpdate();
            return id;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir recomendação", e);
        }
    }

    private Recommendation map(ResultSet rs) throws SQLException {
        Recommendation r = new Recommendation();
        r.setId(rs.getLong("id_rec"));
        r.setUserId(rs.getLong("id_user"));
        r.setMoodId(rs.getLong("id_mood"));
        r.setKind(rs.getString("kind"));
        r.setMinutes(rs.getInt("minutes"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            r.setCreatedAt(ts.toInstant().atOffset(ZoneOffset.UTC));
        }

        String acc = rs.getString("accepted");
        r.setAccepted("Y".equalsIgnoreCase(acc));

        return r;
    }
}
