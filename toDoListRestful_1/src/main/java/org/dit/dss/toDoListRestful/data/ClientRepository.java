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
package org.dit.dss.toDoListRestful.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.dit.dss.toDoListRestful.model.CList;
import org.dit.dss.toDoListRestful.model.Client;
import org.dit.dss.toDoListRestful.model.Task;
import org.dit.dss.toDoListRestful.service.CListService;
import org.dit.dss.toDoListRestful.util.TaskGson;




@ApplicationScoped
public class ClientRepository {

    @Inject
    private EntityManager entityManager;
    
    @Inject
    private CListService cListService;
    
    @Inject	private Logger log;

    /* (non-Javadoc)
	 * @see org.dit.dss.toDoList.data.ClientRepositoryI#findByIntPK(int)
	 */
    @SuppressWarnings("unchecked")
	public Client findByIntPK( int pK) throws Exception{
			List<Client> rows = entityManager.createQuery("select x from Client"+
			" x where x.client_ID=:cid").
			setParameter("cid",pK ).getResultList();
			//entityManager.close();
			if (rows.size() == 0)
				return null;
			else 
				return rows.get(0);
	}
    
    /**
     * Method to Update a Task. Only a proxy due to missing persistence context.</b>
     * Delegates to >ListService.editTask(oldTask, newTask);<
     * @param oldTask
     * @param newTask
     * @throws Exception 
     */
    public void editTask(String oldTask, String newTask) throws Exception{
    	
    	cListService.editTask(oldTask, newTask);
    }
    
    /**
     * Method to Delete a Task. Only a proxy due to missing persistence context.</b>
     * Delegates to >ListService.deleteTask(request);<
     * @param request
     */
    public void deleteTask(TaskGson taskGson) {
		
    	cListService.deleteTask(taskGson);
		
	}
    
    public void addTask(String task, String username) throws Exception{
    	Task newTask = new Task();
    	newTask.setName(task);
    	cListService.persistTask(newTask);
    	//task PK should be initialized already. no need to read from db
    	log.info("just persisted task: "+newTask.toString());
    	
    	Client fkClient = findByEmail(username);
    	log.info("matched following client from DB: "+fkClient.toString());
    	
    	CList newCList = new CList();
    	newCList.setClient(fkClient);
    	newCList.setTask(newTask);
    	newCList.setTask_ID(newTask.getTask_ID());
    	log.info("persisting CList: "+newCList.toString());
    	cListService.persistCList(newCList);
    }
    
    @SuppressWarnings("unchecked")
  	public List<Task> findTaskByClientPK( String pK) throws Exception{
    	List<Task> tasks=new ArrayList<Task>();
    	log.info("got into: public List<CList> findTaskByClientPK( String "+pK+") throws Exception ClientRepository method");
    	List<CList> rows = entityManager.createNamedQuery("CList.findByUserPK").
  			setParameter("clientID",(Integer.parseInt(pK))).getResultList();
  			//entityManager.close();
  			if (rows.size() == 0)
  				return null;
  			else {
  				for(int i=0; i<rows.size(); i++){
  					Task t = new Task();
  					t= getTaskByCList(rows.get(i));
  					tasks.add(t);
  				}
  					
  				log.info("returning: "+rows.size()+" tasks");
  				return tasks;
  			}
  				
  	}
    
    @SuppressWarnings("unchecked")
	private Task getTaskByCList(CList cList){
    	List <Task> rows = entityManager.createNamedQuery("Task.findByPK").
    			setParameter("task_ID",cList.getTask_ID()).getResultList();
    	if (rows.size() == 0)
				return null;
			else 
				log.info("found task: "+rows.get(0).toString()+" tasks");
    	Task t = rows.get(0).clone();
				return t;
    }
    
	
	/* (non-Javadoc)
	 * @see org.dit.dss.toDoList.data.ClientRepositoryI#loginClient(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public Client loginClient(String email, String password)throws Exception{
		List<Client> rows = entityManager.createNamedQuery("Client.login").
		setParameter("user",email ).setParameter("pass", password).getResultList();
		//entityManager.close();
		if (rows.size() == 0)
			return null;
		else 
			return rows.get(0);
	}

	
	@SuppressWarnings("unchecked")
	public Client findByEmail(String email){
		log.setLevel(Level.FINEST);
	log.info("in client repository. Email: "+email);
		List<Client> rows = (List<Client>) entityManager.createNamedQuery("Client.findByUsername").
		setParameter("user",email ).getResultList();
		if (rows.size() == 0){
			log.info("rows.size: "+rows.size());
			return null;
		}else 
			log.info("should return something");
			return rows.get(0);
	}

	 /* (non-Javadoc)
	 * @see org.dit.dss.toDoList.data.ClientRepositoryI#findAllOrderedByName()
	 */
	public List<Client> findAllOrderedByName() {
	        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
	        CriteriaQuery<Client> criteria = cb.createQuery(Client.class);
	        Root<Client> client = criteria.from(Client.class);
	        // Swap criteria statements if you would like to try out type-safe criteria queries, a new
	        // feature in JPA 2.0
	        // criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
	        criteria.select(client).orderBy(cb.asc(client.get("name")));
	        return entityManager.createQuery(criteria).getResultList();
	    }

	
}
