/*
 * 
 * Copyright 2014 Mauricio "Maltron" Leal <maltron@gmail.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package net.nortlam.learning;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Mauricio "Maltron" Leal */
public class Test1 implements Serializable {

    private static final Logger LOG = Logger.getLogger(Test1.class.getName());
    
    public static void main(String[] args) {
        new Test1();
    }

    public Test1() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("STUDIES_PU");
        EntityManager entityManager = factory.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        
        try {
            LOG.log(Level.INFO, ">>> START");
            transaction.begin();
//            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//            CriteriaQuery<Country> query = builder.createQuery(Country.class);
//            Root<Country> root = query.from(Country.class);
//            query.select(root).where(builder.equal(root.get("name"), "Brazil"));
            
            CriteriaBuilder builder = entityManager.getCriteriaBuilder();
            CriteriaQuery<Country> query = builder.createQuery(Country.class);
            Root<Country> root = query.from(Country.class);
            
            
            
            transaction.commit();
            
            
            LOG.log(Level.INFO, ">>> END");
        } finally {
            LOG.log(Level.WARNING, ">>> Closing Connection");
            if(entityManager != null) entityManager.close();
            if(factory != null) factory.close();
        }
    }
    
    
}
