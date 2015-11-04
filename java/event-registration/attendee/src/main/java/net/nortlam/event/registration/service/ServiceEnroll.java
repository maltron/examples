package net.nortlam.event.registration.service;

import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import net.nortlam.event.registration.entity.Enroll;
import net.nortlam.event.registration.exception.AlreadyExistsException;
import net.nortlam.event.registration.exception.BiggerException;
import net.nortlam.event.registration.exception.InternalServerErrorException;
import net.nortlam.event.registration.exception.MissingInformationException;
import net.nortlam.event.registration.util.AbstractService;

/**
 *
 * @author Mauricio "Maltron" Leal <maltron@gmail.com> */
@Stateless
public class ServiceEnroll extends AbstractService<Enroll> {

    private static final Logger LOG = Logger.getLogger(ServiceEnroll.class.getName());
    
    @PersistenceContext
    private EntityManager em;

    public ServiceEnroll() {
        super(Enroll.class);
    }
    
    @Override
    public void validation(Enroll enroll, boolean isNew) 
            throws IllegalArgumentException, BiggerException, 
                            MissingInformationException, AlreadyExistsException, 
                                                InternalServerErrorException {
    }
    
    // ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER 
    //  ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER ENTITY MANAGER 
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
