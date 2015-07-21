package net.nortlam.event.registration.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.nortlam.event.registration.exception.InternalServerErrorException;

@Provider
public class InternalServerErrorMapper implements ExceptionMapper<InternalServerErrorException> {

    @Override
    public Response toResponse(InternalServerErrorException ex) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
    }
}
