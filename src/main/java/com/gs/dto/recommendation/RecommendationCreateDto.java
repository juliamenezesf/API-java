package com.gs.dto.recommendation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class RecommendationCreateDto {

    @NotNull(message = "userId é obrigatório")
    private Long userId;

    @NotNull(message = "moodId é obrigatório")
    private Long moodId;

    @NotBlank(message = "kind é obrigatório")
    private String kind;

    @NotNull(message = "minutes é obrigatório")
    private Integer minutes;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getMoodId() { return moodId; }
    public void setMoodId(Long moodId) { this.moodId = moodId; }

    public String getKind() { return kind; }
    public void setKind(String kind) { this.kind = kind; }

    public Integer getMinutes() { return minutes; }
    public void setMinutes(Integer minutes) { this.minutes = minutes; }
}
