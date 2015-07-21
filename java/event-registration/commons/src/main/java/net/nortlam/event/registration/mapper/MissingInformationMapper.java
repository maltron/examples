package net.nortlam.event.registration.mapper;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import net.nortlam.event.registration.exception.MissingInformationException;

@Provider
public class MissingInformationMapper implements ExceptionMapper<MissingInformationException> {

    @Override
    public Response toResponse(MissingInformationException ex) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
