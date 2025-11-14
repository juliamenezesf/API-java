package com.gs.resource;

import com.gs.bo.TaskBO;
import com.gs.dao.TaskDAO;
import com.gs.dto.task.TaskCreateDto;
import com.gs.dto.task.TaskDetailsDto;
import com.gs.model.Task;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/v1/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Inject
    TaskDAO taskDAO;

    @Inject
    TaskBO taskBO;

    @Context
    UriInfo uriInfo;

    @GET
    public List<TaskDetailsDto> list() {
        return taskDAO.findAll()
                .stream()
                .map(this::toDetails)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        Task t = taskDAO.findById(id);
        if (t == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(toDetails(t)).build();
    }

    @POST
    @Transactional
    public Response create(@Valid TaskCreateDto dto) {
        Long id = taskBO.create(
                dto.getUserId(),
                dto.getTitle(),
                dto.getStartAt(),
                dto.getEndAt(),
                dto.getTaskType(),
                dto.getPriority(),
                dto.getStatus()
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
        taskDAO.delete(id);
        return Response.noContent().build();
    }

    private TaskDetailsDto toDetails(Task t) {
        TaskDetailsDto d = new TaskDetailsDto();
        d.setId(t.getId());
        d.setUserId(t.getUserId());
        d.setTitle(t.getTitle());
        d.setStartAt(t.getStartAt());
        d.setEndAt(t.getEndAt());
        d.setTaskType(t.getTaskType());
        d.setPriority(t.getPriority());
        d.setStatus(t.getStatus());
        return d;
    }
}
