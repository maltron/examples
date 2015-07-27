package net.nortlam.event.registration.rest;

import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.nortlam.event.registration.entity.Event;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Resource {
    
    @EJB
    private Service service;
    
    
    @GET @Path("/{ID}")
    public Response fetch(@PathParam("ID")long ID) 
                        throws NotFoundException, InternalServerErrorException {
        Event event = service.read(ID);
        
        return Response.ok(event).build();
    }
    
    @POST @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Event event) throws IllegalArgumentException, 
            BiggerException, MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        Event inserted = service.create(event);
        return Response.ok(inserted).status(Response.Status.CREATED).build();
    }
    
    @PUT @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Event event) throws IllegalArgumentException, 
                                    BiggerException, MissingInformationException, 
                            AlreadyExistsException, InternalServerErrorException {
        Event updated = service.update(event);
        
        return Response.ok(updated).status(Response.Status.ACCEPTED).build();
    }
    
    @DELETE @Path("/{ID}")
    public Response delete(@PathParam("ID")long ID) throws NotFoundException,
                                                 InternalServerErrorException {
        service.delete(ID);
        return Response.ok().build();
    }
}
