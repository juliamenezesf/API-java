package com.gs.model;

import java.time.OffsetDateTime;

public class Recommendation {

    private Long id;
    private Long userId;
    private Long moodId;
    private String kind;
    private Integer minutes;
    private OffsetDateTime createdAt;
    private Boolean accepted;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getMoodId() { return moodId; }
    public void setMoodId(Long moodId) { this.moodId = moodId; }

    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }

    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer minutes) { this.minutes = minutes; }

    public OffsetDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getAccepted() { return accepted; }
    public void setAccepted(Boolean accepted) { this.accepted = accepted; }
}
