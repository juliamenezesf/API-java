package com.gs.resource;

import com.gs.bo.UserBO;
import com.gs.dao.UserDAO;
import com.gs.model.User;
import com.gs.dto.user.UserCreateDto;
import com.gs.dto.user.UserDetailsDto;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    @Inject
    UserDAO userDAO;

    @Inject
    UserBO userBO;

    @Context
    UriInfo uriInfo;

    @GET
    public List<UserDetailsDto> list() {
        return userDAO.findAll()
                .stream()
                .map(this::toDetails)
                .collect(Collectors.toList());
    }

    @GET
    @Path("{id}")
    public Response get(@PathParam("id") Long id) {
        User u = userDAO.findById(id);
        if (u == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(toDetails(u)).build();
    }

    @POST
    @Transactional
    public Response create(@Valid UserCreateDto dto) {
        Long id = userBO.create(dto.getName(), dto.getEmail(), dto.getRole());

        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(id))
                .build();

        return Response.created(location).build();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        userDAO.delete(id);
        return Response.noContent().build();
    }

    private UserDetailsDto toDetails(User u) {
        UserDetailsDto d = new UserDetailsDto();
        d.setId(u.getId());
        d.setName(u.getName());
        d.setEmail(u.getEmail());
        d.setRole(u.getRole());
        d.setCreatedAt(u.getCreatedAt());
        return d;
    }
}
