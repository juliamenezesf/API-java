package com.gs.bo;

import com.gs.dao.RecommendationDAO;
import com.gs.model.Recommendation;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.OffsetDateTime;

@ApplicationScoped
public class RecommendationBO {

    @Inject
    RecommendationDAO recommendationDAO;

    public Long create(Long userId, Long moodId, String kind, Integer minutes) {

        Recommendation r = new Recommendation();
        r.setUserId(userId);
        r.setMoodId(moodId);
        r.setKind(kind.trim());
        r.setMinutes(minutes);
        r.setCreatedAt(OffsetDateTime.now());
        r.setAccepted(false);

        return recommendationDAO.insert(r);
    }
}
