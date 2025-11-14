package com.gs.dto.mood;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public class MoodLogCreateDto {

    @NotNull(message = "userId é obrigatório")
    private Long userId;

    @NotNull(message = "score é obrigatório")
    @Min(value = 1, message = "score mínimo é 1")
    @Max(value = 5, message = "score máximo é 5")
    private Integer score;

    private String note;

    @Min(value = 0, message = "stressScore mínimo é 0")
    @Max(value = 100, message = "stressScore máximo é 100")
    private Integer stressScore;

    @NotNull(message = "loggedAt é obrigatório")
    private OffsetDateTime loggedAt;

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
