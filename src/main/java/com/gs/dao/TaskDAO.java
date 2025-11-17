package com.gs.dao;

import com.gs.model.Task;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import javax.sql.DataSource;
import java.sql.*;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TaskDAO {

    @Inject
    DataSource dataSource;

    // Não criamos tabela – ela já existe no Oracle
    private void ensureTable() {
        // vazio de propósito
    }

    // Gera próximo ID usando MAX(id_task)+1 (sem mexer no banco)
    private Long nextId(Connection con) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement("select nvl(max(id_task), 0) + 1 from tasks");
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getLong(1);
        }
    }

    // ===== usado pelo TaskResource.list() =====
    public List<Task> findAll() {
        ensureTable();
        String sql = """
            select id_task, id_user, title, start_at, end_at,
                   task_type, priority, status
              from tasks
             order by id_task desc
            """;

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

    // ===== buscar por usuário =====
    public List<Task> findByUser(Long userId) {
        ensureTable();
        String sql = """
            select id_task, id_user, title, start_at, end_at,
                   task_type, priority, status
              from tasks
             where id_user = ?
             order by start_at
            """;

        List<Task> list = new ArrayList<>();

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar tasks por usuário", e);
        }

        return list;
    }

    // ===== usado pelo TaskResource.getById() =====
    public Task findById(Long id) {
        ensureTable();
        String sql = """
            select id_task, id_user, title, start_at, end_at,
                   task_type, priority, status
              from tasks
             where id_task = ?
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
            throw new RuntimeException("Erro ao buscar task", e);
        }

        return null;
    }

    public Long insert(Task t) {
        ensureTable();
        String sql = """
            insert into tasks
              (id_task, id_user, title, start_at, end_at, task_type, priority, status)
            values
              (?,?,?,?,?,?,?,?)
            """;

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            Long id = nextId(con);
            ps.setLong(1, id);
            ps.setLong(2, t.getUserId());
            ps.setString(3, t.getTitle());

            // startAt pode ser nulo
            OffsetDateTime startAt = t.getStartAt();
            if (startAt != null) {
                ps.setTimestamp(4, Timestamp.from(startAt.toInstant()));
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }

            // endAt pode ser nulo
            OffsetDateTime endAt = t.getEndAt();
            if (endAt != null) {
                ps.setTimestamp(5, Timestamp.from(endAt.toInstant()));
            } else {
                ps.setNull(5, Types.TIMESTAMP);
            }

            // 🔹 TASK_TYPE – normaliza e valida com CK_TASKS_TYPE
            String taskType = t.getTaskType();
            if (taskType != null) {
                taskType = taskType.trim().toUpperCase();
            }

            if (!"FOCUS".equals(taskType) &&
                    !"MEETING".equals(taskType) &&
                    !"BREAK".equals(taskType) &&
                    !"PERSONAL".equals(taskType)) {
                throw new IllegalArgumentException("TaskType inválido: " + taskType);
            }

            ps.setString(6, taskType);

            // 🔹 PRIORITY – normaliza e valida com CK_TASKS_PRIORITY
            String priority = t.getPriority();
            if (priority != null) {
                priority = priority.trim().toUpperCase();
            }

            if (!"LOW".equals(priority) &&
                    !"MEDIUM".equals(priority) &&
                    !"HIGH".equals(priority)) {
                throw new IllegalArgumentException("Priority inválida: " + priority);
            }

            ps.setString(7, priority);

            // 🔹 STATUS – normaliza e valida com CK_TASKS_STATUS
            String status = t.getStatus();
            if (status != null) {
                status = status.trim().toUpperCase();
            }

            if (!"PENDING".equals(status) &&
                    !"IN_PROGRESS".equals(status) &&
                    !"DONE".equals(status) &&
                    !"CANCELLED".equals(status)) {
                throw new IllegalArgumentException("Status inválido: " + status);
            }

            ps.setString(8, status);

            ps.executeUpdate();
            return id;

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir task", e);
        }
    }

    public void delete(Long idTask) {
        ensureTable();
        String sql = "delete from tasks where id_task = ?";

        try (Connection con = dataSource.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setLong(1, idTask);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar task", e);
        }
    }

    private Task map(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.setId(rs.getLong("id_task"));
        t.setUserId(rs.getLong("id_user"));
        t.setTitle(rs.getString("title"));

        Timestamp start = rs.getTimestamp("start_at");
        if (start != null) {
            t.setStartAt(start.toInstant().atOffset(ZoneOffset.UTC));
        }

        Timestamp end = rs.getTimestamp("end_at");
        if (end != null) {
            t.setEndAt(end.toInstant().atOffset(ZoneOffset.UTC));
        }

        t.setTaskType(rs.getString("task_type"));
        t.setPriority(rs.getString("priority"));
        t.setStatus(rs.getString("status"));
        return t;
    }
}
