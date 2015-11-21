package net.nortlam.event.registration.rest;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonReader;
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
import net.nortlam.event.registration.entity.Order;
import net.nortlam.event.registration.entity.Organizer;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.exception.NotFoundException;
import net.nortlam.event.registration.service.Service;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Resource {

    private static final Logger LOG = Logger.getLogger(Resource.class.getName());
    
    @EJB
    private Service service;
    
    @GET @Path("/{ID}")
    public Response fetch(@PathParam("ID") long ID) throws NotFoundException, 
                                                 InternalServerErrorException {
        Organizer found = service.read(ID);
        
        return Response.ok(found).build();
    }
    
    @Path("/order")
    @POST @Consumes(MediaType.APPLICATION_JSON)
    public Response create(String content) throws InternalServerErrorException {
        try {
            JsonReader reader = Json.createReader(new StringReader(content));
            Order order = new Order(reader.readObject());
            service.save(order);
            
        } catch(JsonException ex) {
            LOG.log(Level.SEVERE, "### JSON EXCEPTION:{0}", ex.getMessage());
            throw new InternalServerErrorException(ex.getMessage());
        }
        
        return Response.ok().build();
    }
    
    @POST @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Organizer organizer) throws IllegalArgumentException, BiggerException,
                        MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        Organizer inserted = service.create(organizer);
        
        return Response.ok(inserted).status(Response.Status.CREATED).build();
    }
    
    @PUT @Consumes(MediaType.APPLICATION_JSON)
    public Response update(Organizer organizer) throws IllegalArgumentException, BiggerException,
                        MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
        Organizer updated = service.update(organizer);
        
        return Response.ok(updated).status(Response.Status.ACCEPTED).build();
    }
    
    @DELETE @Path("/{ID}")
    public Response delete(@PathParam("ID")long ID) throws NotFoundException, 
                                                        InternalServerErrorException {
        service.delete(ID);
        
        return Response.ok().build();
    }
    
}
