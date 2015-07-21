package net.nortlam.event.registration.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.nortlam.event.registration.exception.BiggerException;

@Provider
public class BiggerMapper implements ExceptionMapper<BiggerException> {

    @Override
    public Response toResponse(BiggerException ex) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
