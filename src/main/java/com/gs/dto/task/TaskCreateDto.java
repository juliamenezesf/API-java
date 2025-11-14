package com.gs.dto.task;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class TaskCreateDto {

    @NotNull(message = "userId é obrigatório")
    private Long userId;

    @NotBlank(message = "title é obrigatório")
    private String title;

    @NotNull(message = "startAt é obrigatório")
    private OffsetDateTime startAt;

    @NotNull(message = "endAt é obrigatório")
    private OffsetDateTime endAt;

    @NotBlank(message = "taskType é obrigatório")
    private String taskType;

    @NotBlank(message = "priority é obrigatória")
    private String priority;

    @NotBlank(message = "status é obrigatório")
    private String status;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public OffsetDateTime getStartAt() { return startAt; }
    public void setStartAt(OffsetDateTime startAt) { this.startAt = startAt; }

    public OffsetDateTime getEndAt() { return endAt; }
    public void setEndAt(OffsetDateTime endAt) { this.endAt = endAt; }

    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
