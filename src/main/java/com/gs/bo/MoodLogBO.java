package com.gs.bo;

import com.gs.dao.MoodLogDAO;
import com.gs.model.MoodLog;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;

@ApplicationScoped
public class MoodLogBO {

    @Inject
    MoodLogDAO moodLogDAO;

    public Long create(Long userId,
                       Integer score,
                       String note,
                       Integer stressScore,
                       OffsetDateTime loggedAt) {

        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("score deve estar entre 1 e 5");
        }

        MoodLog m = new MoodLog();
        m.setUserId(userId);
        m.setScore(score);
        m.setNote(note == null ? "" : note.trim());
        m.setStressScore(stressScore == null ? 0 : stressScore);
        m.setLoggedAt(loggedAt);

        return moodLogDAO.insert(m);
    }
}
