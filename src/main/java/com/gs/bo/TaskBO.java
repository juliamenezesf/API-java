package com.gs.bo;

import com.gs.dao.TaskDAO;
import com.gs.model.Task;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;

@ApplicationScoped
public class TaskBO {

    @Inject
    TaskDAO taskDAO;

    public Long create(Long userId,
                       String title,
                       OffsetDateTime startAt,
                       OffsetDateTime endAt,
                       String taskType,
                       String priority,
                       String status) {

        if (endAt.isBefore(startAt)) {
            throw new IllegalArgumentException("endAt não pode ser antes de startAt");
        }

        Task t = new Task();
        t.setUserId(userId);
        t.setTitle(title.trim());
        t.setStartAt(startAt);
        t.setEndAt(endAt);
        t.setTaskType(taskType.trim().toLowerCase());
        t.setPriority(priority.trim().toLowerCase());
        t.setStatus(status.trim().toLowerCase());

        return taskDAO.insert(t);
    }
}
