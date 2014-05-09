/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dit.dss.toDoListRestful.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.dit.dss.toDoListRestful.model.Client;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class ClientLogin {

    @Inject
    private Logger log;

    @Inject
    private EntityManager entityManager;
    
   

    public Client loginClient(Client client)throws Exception{
    	log.info("Loging in " + client.getUsername());
		@SuppressWarnings("unchecked")
		List<Client> rows = entityManager.createNamedQuery("Client.login").
		setParameter("user", client.getUsername() ).setParameter("pass",  client.getPassword()).getResultList();
		//entityManager.close();
		 
		if (rows.size() == 0){
			
			log.info("User "+ client.getUsername()+" not found");
			return null;
		}else {
			client = rows.get(0);
			log.info("User "+ client.getUsername()+"loged in succesfuly");
			return client;}
	}
    
    public Client registerClient(Client client)throws Exception{
    	log.info("Registering in " + client.getUsername());
    	entityManager.persist(client);entityManager.flush();
    	
		@SuppressWarnings("unchecked")
		List<Client> rows = entityManager.createNamedQuery("Client.login").
		setParameter("user", client.getUsername() ).setParameter("pass",  client.getPassword()).getResultList();
		//entityManager.close();
		 
		if (rows.size() == 0){
			
			log.info("User "+ client.getUsername()+" NOT registered");
			return null;
		}else {
			client = rows.get(0);
			log.info("User "+ client.getUsername()+"registered succesfuly");
			return client;}
	}
 
}
