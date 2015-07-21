package net.nortlam.event.registration.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import net.nortlam.event.registration.exception.AlreadyExistsException;

@Provider
public class AlreadyExistsMapper implements ExceptionMapper<AlreadyExistsException> {

    @Override
    public Response toResponse(AlreadyExistsException ex) {
        return Response.status(Response.Status.CONFLICT).build();
    }
}
