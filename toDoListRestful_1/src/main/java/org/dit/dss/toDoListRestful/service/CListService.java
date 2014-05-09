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

import org.dit.dss.toDoListRestful.model.CList;
import org.dit.dss.toDoListRestful.model.Client;
import org.dit.dss.toDoListRestful.model.Task;
import org.dit.dss.toDoListRestful.util.TaskGson;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class CListService {

    @Inject
    private Logger log;

    @Inject
    private EntityManager entityManager;
    
    public void persistTask(Task task) throws Exception{
    	entityManager.persist(task);
    }
    
    public void persistCList(CList cList) throws Exception{
    	entityManager.persist(cList);
    }
    
    @SuppressWarnings("unchecked")
	public void editTask(String oldT, String newT) throws Exception{
    	
    	List<Task> oldTask = entityManager.createNamedQuery("Task.findByName").
    			setParameter("task",oldT).getResultList();
			//entityManager.close();
			if (oldTask.size() != 0)
			{
				Task tempTask = oldTask.get(0);
				log.info("Matched old task: "+tempTask.toString());
				tempTask.setName(newT);
				log.info("Merging tasks ...");
				entityManager.merge(tempTask);entityManager.flush();
				}
			log.info("Confirming old task retrieves with updated name ...");
			oldTask = entityManager.createNamedQuery("Task.findByName").
	    			setParameter("task",newT).getResultList();
			if(oldTask.size()==0)
				log.info("Didn't retrive anything");
			else
				log.info("Found task: "+oldTask.get(0));
    }

	@SuppressWarnings("unchecked")
	public void deleteTask(TaskGson taskGson) {
		
		List<Task> oldTask = entityManager.createNamedQuery("Task.findByName").
    			setParameter("task",taskGson.getTask()).getResultList();
		
		List<Client> clients = entityManager.createNamedQuery("Client.findByUsername").
				setParameter("user", taskGson.getUsername()).getResultList();
		
		if ((oldTask.size() != 0)&&(clients.size()!=0))
		{
			
			
			List<CList> cLists = entityManager.createNamedQuery("CList.findByTask_ClientPK").
					setParameter("taskID", oldTask.get(0).getTask_ID()).
					setParameter("clientID", clients.get(0).getClient_ID()).getResultList();
			
			if(cLists.size() != 0){
				CList tempCList = cLists.get(0);
				log.info("Matched CList to delete: "+tempCList.toString());
				log.info("Deleting CList ...");
				entityManager.remove(tempCList);
				
				Task tempTask = oldTask.get(0);
				log.info("Matched task to delete: "+tempTask.toString());
				
				log.info("Deleting task ...");
				entityManager.remove(tempTask);
				entityManager.flush();
			}
					
			}
		log.info("Confirming task deleted ...");
		oldTask = entityManager.createNamedQuery("Task.findByName").
    			setParameter("task",taskGson.getTask()).getResultList();
		if(oldTask.size()==0)
			log.info("Didn't retrive anything. Deletion succesful");
		else
			log.info("Found task: "+oldTask.get(0)+" Deletion failed (or multiple tasks with same name)");
		
	}
 
}
