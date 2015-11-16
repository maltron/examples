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
import net.nortlam.event.registration.entity.Attendee;
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
    public Response fetch(@PathParam("ID") long ID) throws NotFoundException, 
                                                 InternalServerErrorException {
        Attendee found = service.read(ID);
        
        return Response.ok(found).build();
    }
    
    @GET @Path("/email/{email}")
    public Response fetch(@PathParam("email")String email) throws NotFoundException, 
                                        InternalServerErrorException {
        Attendee found = service.findByEmail(email);
        
        return Response.ok(found).build();
    }
    
    @POST @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Attendee organizer) throws IllegalArgumentException, BiggerException,
                        MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        Attendee inserted = service.create(organizer);
        
        return Response.ok(inserted).status(Response.Status.CREATED).build();
    }
    
//    @POST @Consumes(MediaType.APPLICATION_JSON)
//    @Path("/enroll")
//    public Response createEnroll(Enroll enroll) throws IllegalArgumentException, BiggerException,
//                        MissingInformationException, AlreadyExistsException, 
//                                                InternalServerErrorException {
//        
//        Enroll inserted = serviceEnroll.create(enroll);
//        
//        return Response.ok(inserted).status(Response.Status.CREATED).build();
//    }
    
    @PUT @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Attendee attendee) throws IllegalArgumentException, BiggerException,
                        MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        Attendee updated = service.update(attendee);
        
        return Response.ok(updated).status(Response.Status.ACCEPTED).build();
    }
    
    @DELETE @Path("/{ID}")
    public Response delete(@PathParam("ID")long ID) throws NotFoundException, 
                                                        InternalServerErrorException {
        service.delete(ID);
        
        return Response.ok().build();
    }
    
}
