package com.gs.resource;

import com.gs.bo.RecommendationBO;
import com.gs.dao.RecommendationDAO;
import com.gs.dto.recommendation.RecommendationCreateDto;
import com.gs.dto.recommendation.RecommendationDetailsDto;
import com.gs.model.Recommendation;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/v1/recommendations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RecommendationResource {

    @Inject
    RecommendationDAO recommendationDAO;

    @Inject
    RecommendationBO recommendationBO;

    @Context
    UriInfo uriInfo;

    @GET
    public List<RecommendationDetailsDto> list() {
        return recommendationDAO.findAll()
                .stream()
                .map(this::toDetails)
                .collect(Collectors.toList());
    }

    @POST
    @Transactional
    public Response create(@Valid RecommendationCreateDto dto) {
        Long id = recommendationBO.create(
                dto.getUserId(),
                dto.getMoodId(),
                dto.getKind(),
                dto.getMinutes()
        );

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(id))
                .build();

        return Response.created(location).build();
    }

    private RecommendationDetailsDto toDetails(Recommendation r) {
        RecommendationDetailsDto d = new RecommendationDetailsDto();
        d.setId(r.getId());
        d.setUserId(r.getUserId());
        d.setMoodId(r.getMoodId());
        d.setKind(r.getKind());
        d.setMinutes(r.getMinutes());
        d.setCreatedAt(r.getCreatedAt());
        d.setAccepted(r.getAccepted());
        return d;
    }
}
