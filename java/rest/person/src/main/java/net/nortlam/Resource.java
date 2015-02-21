package net.nortlam;

import java.io.Serializable;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

/**
 *
 * @author Mauricio "Maltron" Leal */
@Path("/resource")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Stateless
public class Resource implements Serializable {
    
    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOG = Logger.getLogger(Resource.class.getName());
    
    @GET
    public Collection<Person> all() {
        LOG.log(Level.INFO, ">>> Resource.all()");
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery criteria = builder.createQuery();
        Root<Person> root = criteria.from(Person.class);
        criteria.select(root).orderBy(builder.asc(root.get("firstName")), 
                                        builder.asc(root.get("lastName")));
        
        return entityManager.createQuery(criteria).getResultList();
    }
    
    @GET @Path("/{ID}")
    public Person findByID(@PathParam("ID")long ID) {
        LOG.log(Level.INFO, ">>> Resource.findByID({0})", ID);
        return entityManager.find(Person.class, ID);
    }
    
    @GET @Path("/count")
    public int findCount() {
        CriteriaQuery criteria = entityManager.getCriteriaBuilder().createQuery();
        Root<Person> root = criteria.from(Person.class);
        criteria.select(entityManager.getCriteriaBuilder().count(root));
        
        Query query = entityManager.createQuery(criteria);
        return ((Long)query.getSingleResult()).intValue();
    }
    
    @GET @Path("/find/{firstName}/{lastName}")
    public Response findByFirstLastName(@PathParam("firstName")String firstName,
            @PathParam("lastName")String lastName) {
        LOG.log(Level.INFO, ">>> Resource.findByFirstLastName() First:{0} Last:{1}",
                new Object[] {firstName, lastName});
        try {
            Person person = entityManager.createNamedQuery(Person.FIND_BY_FIRST_LAST_NAME, Person.class)
                .setParameter("FIRST_NAME", firstName)
                .setParameter("LAST_NAME", lastName).getSingleResult();
            
            return Response.ok(person).build();
            
        } catch(Exception ex) {
            LOG.log(Level.WARNING, "### Resource.findByFirstLastName() NOT FOUND {0} First:{1} Last:{2}",
                    new Object[] {ex.getMessage(), firstName, lastName});
            
        }
       
        return Response.status(Response.Status.NOT_FOUND).build();
    }
    
    @POST
    public Response createPerson(Person person) {
        try {
            LOG.log(Level.INFO, ">>> Resource.createPerson() <BEFORE> Person:{0}", 
                person != null ? person.toString() : "NULL");
            entityManager.persist(person);
            LOG.log(Level.INFO, ">>> Resource.createPerson() <AFTER> Person:{0}", 
                person != null ? person.toString() : "NULL");
            return Response.ok(person).build();
            
        } catch(Exception ex) {
            LOG.log(Level.SEVERE, "### Resource.createPerson()\"{0}\"", ex.getMessage());
        }
        
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
    
    @PUT
    public Response updatePerson(Person person) {
        try {
            LOG.log(Level.INFO, ">>> Resource.updatePerson() <BEFORE> Person:{0}", 
                person != null ? person.toString() : "NULL");
            entityManager.merge(person);
            LOG.log(Level.INFO, ">>> Resource.updatePerson() <AFTER> Person:{0}", 
                person != null ? person.toString() : "NULL");
            
            return Response.ok(person).build();
        } catch(Exception ex) {
            LOG.log(Level.SEVERE, "### Resource.updatePerson() \"{0}\"", ex.getMessage());
        }
        
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
    
    @DELETE @Path("/{ID}")
    public Response deletePerson(@PathParam("ID")long ID) {
        try {
            LOG.log(Level.INFO, ">>> Resource.deletePerson() ID:{0}", ID);
            Person person = entityManager.find(Person.class, ID);
            entityManager.remove(person);
            
            return Response.ok(person).build();
            
        } catch(Exception ex) {
            LOG.log(Level.SEVERE, "### Resource.deletePerson() \"{0}\"", ex.getMessage());
        }
        
        return Response.status(Response.Status.NOT_ACCEPTABLE).build();
    }
}
