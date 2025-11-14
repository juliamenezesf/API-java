package com.gs.dao;

import com.gs.model.Task;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TaskDAO {

    @Inject
    DataSource dataSource;

    private static final String CREATE_TABLE_SQL = """
        create table if not exists tasks(
          id bigint auto_increment primary key,
          user_id bigint not null,
          title varchar(150) not null,
          start_at timestamp not null,
          end_at timestamp not null,
          task_type varchar(20) not null,
          priority varchar(10) not null,
          status varchar(20) not null
        )
        """;

    private void ensureTable() {
        try (Connection con = dataSource.getConnection();
             Statement st = con.createStatement()) {
            st.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao garantir tabela tasks", e);
        }
    }

    public List<Task> findAll() {
        ensureTable();
        String sql = "select id,user_id,title,start_at,end_at,task_type,priority,status from tasks order by id desc";
        List<Task> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tasks", e);
        }

        return list;
    }

    public Task findById(Long id) {
        ensureTable();
        String sql = "select id,user_id,title,start_at,end_at,task_type,priority,status from tasks where id = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar task", e);
        }

        return null;
    }

    public Long insert(Task t) {
        ensureTable();
        String sql = "insert into tasks(user_id,title,start_at,end_at,task_type,priority,status) values(?,?,?,?,?,?,?)";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, t.getUserId());
            ps.setString(2, t.getTitle());
            ps.setTimestamp(3, Timestamp.from(t.getStartAt().toInstant()));
            ps.setTimestamp(4, Timestamp.from(t.getEndAt().toInstant()));
            ps.setString(5, t.getTaskType());
            ps.setString(6, t.getPriority());
            ps.setString(7, t.getStatus());

            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir task", e);
        }

        return null;
    }

    public void delete(Long id) {
        ensureTable();
        String sql = "delete from tasks where id = ?";
        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar task", e);
        }
    }

    private Task map(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.setId(rs.getLong("id"));
        t.setUserId(rs.getLong("user_id"));
        t.setTitle(rs.getString("title"));
        Timestamp st = rs.getTimestamp("start_at");
        Timestamp en = rs.getTimestamp("end_at");
        t.setStartAt(st.toInstant().atOffset(ZoneOffset.UTC));
        t.setEndAt(en.toInstant().atOffset(ZoneOffset.UTC));
        t.setTaskType(rs.getString("task_type"));
        t.setPriority(rs.getString("priority"));
        t.setStatus(rs.getString("status"));
        return t;
    }
}
