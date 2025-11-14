package com.gs.resource;

import com.gs.bo.MoodLogBO;
import com.gs.dao.MoodLogDAO;
import com.gs.dto.mood.MoodLogCreateDto;
import com.gs.dto.mood.MoodLogDetailsDto;
import com.gs.model.MoodLog;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/v1/mood-logs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MoodLogResource {

    @Inject
    MoodLogDAO moodLogDAO;

    @Inject
    MoodLogBO moodLogBO;

    @Context
    UriInfo uriInfo;

    @GET
    public List<MoodLogDetailsDto> list() {
        return moodLogDAO.findAll()
                .stream()
                .map(this::toDetails)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        MoodLog m = moodLogDAO.findById(id);
        if (m == null) return Response.status(Response.Status.NOT_FOUND).build();
        return Response.ok(toDetails(m)).build();
    }

    @POST
    @Transactional
    public Response create(@Valid MoodLogCreateDto dto) {

        Long id = moodLogBO.create(
                dto.getUserId(),
                dto.getScore(),
                dto.getNote(),
                dto.getStressScore(),
                dto.getLoggedAt()
        );

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(id))
                .build();

        return Response.created(location).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        moodLogDAO.delete(id);
        return Response.noContent().build();
    }

    private MoodLogDetailsDto toDetails(MoodLog m) {
        MoodLogDetailsDto d = new MoodLogDetailsDto();
        d.setId(m.getId());
        d.setUserId(m.getUserId());
        d.setScore(m.getScore());
        d.setNote(m.getNote());
        d.setStressScore(m.getStressScore());
        d.setLoggedAt(m.getLoggedAt());
        return d;
    }
}
