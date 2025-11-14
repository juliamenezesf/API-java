package com.gs.model;

import java.time.OffsetDateTime;

public class MoodLog {

    private Long id;
    private Long userId;
    private Integer score;
    private String note;
    private Integer stressScore;
    private OffsetDateTime loggedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Integer getScore() { return score; }
    public void setScore(Integer score) { this.score = score; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public Integer getStressScore() { return stressScore; }
    public void setStressScore(Integer stressScore) { this.stressScore = stressScore; }

    public OffsetDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(OffsetDateTime loggedAt) { this.loggedAt = loggedAt; }
}
