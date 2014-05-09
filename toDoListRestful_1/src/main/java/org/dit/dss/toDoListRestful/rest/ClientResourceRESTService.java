/**
 * 
 */
package org.dit.dss.toDoListRestful.rest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.validation.Validator;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.dit.dss.toDoListRestful.data.ClientRepository;
import org.dit.dss.toDoListRestful.model.CList;
import org.dit.dss.toDoListRestful.model.Client;
import org.dit.dss.toDoListRestful.model.Task;
import org.dit.dss.toDoListRestful.service.ClientLogin;
import org.dit.dss.toDoListRestful.util.Authentication;
import org.dit.dss.toDoListRestful.util.ClientPK;
import org.dit.dss.toDoListRestful.util.Login;
import org.dit.dss.toDoListRestful.util.RegisterGson;
import org.dit.dss.toDoListRestful.util.TaskEditGson;
import org.dit.dss.toDoListRestful.util.TaskGson;

import com.google.gson.Gson;




/**
 * @author ivo
 * 
 *         <p/>
 *         This class produces a RESTful service to read/write the contents of
 *         the clients table.
 */

@Path("/clients")
@RequestScoped
// @WebService (endpointInterface=
// "/toDoList/src/main/java/service/TaskServiceEJBi.java")
// @TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ClientResourceRESTService {

	@Inject	private Logger log;

	@Inject	private Validator validator;

	@Inject private ClientRepository clientRepository;
	 
	/* @Inject
	 private ClientRegistration registration;*/
	 
	@Inject private ClientLogin clientLogin;
	 
	@Inject private Client clientToLogin;
	 
	/* private Gson json = new Gson();*/
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see service.TaskServiceEJBi#findByIntPK(int)
	 */
	@POST
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	@Path("/loginClient/findTasksByPK")
	public javax.ws.rs.core.Response findTasksByPK(String request) {
		log.info("got in /loginClient/findTasksByPK/{"+request+"} rest method");
		javax.ws.rs.core.Response.ResponseBuilder builder = null;
		ClientPK clientPK = new Gson().fromJson(request, ClientPK.class);
		
		 builder = Response.ok();
		try {
			List<Task> tasks = clientRepository.findTaskByClientPK(clientPK.getpk());
			String json = new Gson().toJson(tasks);
			log.info("Attaching to response: "+json);
			builder = Response.status(Response.Status.ACCEPTED).entity(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return builder.build();
	}
	
	@POST
	@Path("/loginClient/addTask")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public javax.ws.rs.core.Response addTask(String request){
		
		log.info("got in /loginClient/addTask rest method");
		javax.ws.rs.core.Response.ResponseBuilder builder = null;
		
		TaskGson taskGson = new Gson().fromJson(request, TaskGson.class);
		
		 builder = Response.ok();
		 try {
			 log.info("persisting TaskGson: "+taskGson.toString());
			 clientRepository.addTask(taskGson.getTask(), taskGson.getUsername());
			 log.info("done persisting. return TaskGson in header as confirmation.("+taskGson.toString()+")");
				String json = new Gson().toJson(taskGson);
				log.info("Attaching to response: "+json);
				builder = Response.status(Response.Status.ACCEPTED).entity(json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			// TODO Auto-generated method stub
			return builder.build();
	}
	
	@POST
	@Path("/loginClient/editTask")
	@Consumes(MediaType.APPLICATION_JSON)
	public void editTask(String request){
		
		TaskEditGson taskEditGson = new Gson().fromJson(request, TaskEditGson.class);
		log.info("got in /loginClient/editTask rest method. Task to Edit: "+taskEditGson.toString());
		try {
			clientRepository.editTask(taskEditGson.getOldTask(), taskEditGson.getNewTask());
		} catch (Exception e) {
			log.info("error updating task!");
			e.printStackTrace();
		}
	}
	
	@POST
	@Path("/loginClient/deleteTask")
	@Consumes(MediaType.APPLICATION_JSON)
	public void deleteTask(String request){
		
		TaskGson taskGson = new Gson().fromJson(request, TaskGson.class);
		log.info("got in /loginClient/deleteTask rest method. Task to Delete: "+taskGson.toString());
		try {
			clientRepository.deleteTask(taskGson);
		} catch (Exception e) {
			log.info("error deleting task!");
			e.printStackTrace();
		}
	}

	@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Client> listAllClients() {
        return clientRepository.findAllOrderedByName();
    }
	
	@GET
	@Path("/{id:[0-9][0-9]*}")
	@Produces(MediaType.APPLICATION_JSON)
	public Client findByIntPK(@PathParam("id") long id) throws Exception {
		Client client = clientRepository.findByIntPK((int) id);
		if (client == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		return client;
	}

	/**
	 * Creates a new client from the values provided. Performs validation, and
	 * will return a JAX-RS response with either 200 ok, or with a map of
	 * fields, and related errors.
	 */
	@POST
	@Path("/registerClient")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public javax.ws.rs.core.Response registerClient(String request) {
		
		log.setLevel(Level.FINEST);
		log.info("got in login /registerClient method");
		
		javax.ws.rs.core.Response.ResponseBuilder builder = null;
		
		RegisterGson registerGson = new Gson().fromJson(request, RegisterGson.class);
		log.info("Registration mapping: "+registerGson.toString());
		
		Authentication authentication = new Authentication();
		authentication.setUsername(registerGson.getUsername());	
		authentication.setUrl("");
		 builder = Response.ok();
		
		try {
			log.info("Registration validating...");
			clientToLogin.setName(registerGson.getName());
			clientToLogin.setPassword(registerGson.getPassword());
			clientToLogin.setUsername(registerGson.getUsername());
			String validationMsg=validateRegisterCredentials();
			Client loginStatus=null;
			log.info("validation message: "+validationMsg);
			if((validationMsg==null)){
				log.info(registerGson.getUsername()+" validated");
				loginStatus=clientLogin.registerClient(clientToLogin);
				

				if(loginStatus==null){
					//registration unsuccessful
					authentication.setStatus("registration unsuccessful");
					builder = Response.status(Response.Status.BAD_REQUEST).entity(authentication).type(MediaType.APPLICATION_JSON);
					log.info("registration "+registerGson.getUsername()+" unsuccessful");
				}else {
					//successfully logged in
					authentication.setStatus("successfully registered");
					authentication.setUrl("");
					log.info(new Gson().toJson(authentication));
					
					log.info("successfully registered in "+registerGson.getUsername());
					String json = new Gson().toJson(authentication);
					log.info(Response.ok(json, MediaType.APPLICATION_JSON).build().toString());
					//return Response.ok(json, MediaType.APPLICATION_JSON).build();//ok(json, MediaType.APPLICATION_JSON).build();
					builder = Response.status(Response.Status.ACCEPTED).entity(json);
					//return Response.ok(new Gson().toJson(authentication)).type(MediaType.APPLICATION_JSON_TYPE).build();
				}
			}else if(!validationMsg.equals(registerGson.getUsername())){
				//email inexistent
				authentication.setStatus("email already EXISTS. Registration terminated.");
				builder = Response.status(Response.Status.BAD_REQUEST).entity(authentication).type(MediaType.APPLICATION_JSON);
				log.info("email "+registerGson.getUsername()+" email already EXISTS. Registration terminated.");
			}
		}catch(Exception e){
			//unspecified error occurred
			authentication.setStatus("unspecified error occurred");
			builder = Response.status(Response.Status.BAD_REQUEST).entity(authentication).type(MediaType.APPLICATION_JSON);
			log.info("unspecified error occurred");
		}
		return builder.build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/loginClient")
	public javax.ws.rs.core.Response loginClient(String request) {
		log.info("got in login rest method");
		
		javax.ws.rs.core.Response.ResponseBuilder builder = null;
		Login user = new Gson().fromJson(request, Login.class);
		Authentication authentication = new Authentication();
		authentication.setUsername(user.getUsername());	
		authentication.setUrl("http://localhost:8080/toDoListRestful_1/index.html");
		 builder = Response.ok();
		try{
			log.info("validating "+user.getUsername());
			String validationMsg=validateLoginCredentials(user.getUsername(),user.getPassword());
			Client loginStatus=null;
			
			if(validationMsg.equals(user.getUsername())){
				log.info(user.getUsername()+" validated");
				loginStatus=clientLogin.loginClient(clientToLogin);
				
				if(loginStatus==null){
					//login unsuccessful
					authentication.setStatus("login unsuccessful");
					builder = Response.status(Response.Status.BAD_REQUEST).entity(authentication).type(MediaType.APPLICATION_JSON);
					log.info("login "+user.getUsername()+" unsuccessful");
				}else {
					//successfully logged in
					authentication.setStatus("successfully logged in");
					authentication.setUrl("http://localhost:8080/toDoListRestful_1/main.html");
					log.info(new Gson().toJson(authentication));
					
					log.info("successfully logged in "+user.getUsername());
					String json = new Gson().toJson(authentication);
					log.info(Response.ok(json, MediaType.APPLICATION_JSON).build().toString());
					//return Response.ok(json, MediaType.APPLICATION_JSON).build();//ok(json, MediaType.APPLICATION_JSON).build();
					builder = Response.status(Response.Status.ACCEPTED).entity(json);
					//return Response.ok(new Gson().toJson(authentication)).type(MediaType.APPLICATION_JSON_TYPE).build();
				}
			}else{
				//email inexistent
				authentication.setStatus("email inexistent");
				builder = Response.status(Response.Status.BAD_REQUEST).entity(authentication).type(MediaType.APPLICATION_JSON);
				log.info("email "+user.getUsername()+" inexistent");
			}
		}catch(Exception e){
			//unspecified error occurred
			authentication.setStatus("unspecified error occurred");
			builder = Response.status(Response.Status.BAD_REQUEST).entity(authentication).type(MediaType.APPLICATION_JSON);
			log.info("unspecified error occurred");
		}
		
		return builder.build();
		//return Response.status(200).build();//.entity(new Gson().toJson(authentication)).type(MediaType.APPLICATION_JSON_TYPE).build();
	}
	
	
	@POST
	@Path("/loginClient/getPKbyUsername")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public String getPKByUsername(String credentials) throws Exception {
		log.info("got in /loginClient/getPKbyUsername rest method");
		log.info(credentials);
		Login user = new Gson().fromJson(credentials, Login.class);
		
		Client client = clientRepository.findByEmail(user.getUsername());
		
		if (client == null) {
			throw new WebApplicationException(Response.Status.NOT_FOUND);
		}
		
		return ""+client.getClient_ID();
	}
	
	
	/**
	 * 
	 * @param email
	 * @param password
	 * @return
	 * @throws Exception
	 */
	private String validateLoginCredentials(String email, String password) throws Exception {
		clientToLogin.setName("loggingIn");
		clientToLogin.setPassword(password);
		clientToLogin.setUsername(email);
		
		// Check the uniqueness of the email address
		if (emailAlreadyExists(clientToLogin.getUsername())) {
			return email;
		}else{
			log.info("User: "+clientToLogin.getUsername()+" doesn't exist!");
			return "User: "+clientToLogin.getUsername()+" doesn't exist!";
		}
		
	}
	
	private String validateRegisterCredentials() throws Exception {
		
		// Check the uniqueness of the email address
		if (emailAlreadyExists(clientToLogin.getUsername())) {
			return clientToLogin.getUsername();
		}else{
			log.info("User: "+clientToLogin.getUsername()+" doesn't exist! Registration can proceed.");
			return null;
		}
		
	}
	
/**
 * 
 * @param client
 * @throws Exception
 */
	private void validateMember(Client client) throws Exception {
		log.info("Got in: private void validateMember(Client client)");
		// Create a bean validator and check for issues.
		Set<ConstraintViolation<Client>> violations = validator
				.validate(client);

		if (!violations.isEmpty()) {
			throw new ConstraintViolationException(
					new HashSet<ConstraintViolation<?>>(violations));
		}

		// Check the uniqueness of the email address
		if (emailAlreadyExists(client.getUsername())) {
			throw new ValidationException("Unique Email Violation");
		}
	}

	/**@GET
    @Produces(MediaType.APPLICATION_JSON)
	 * Creates a JAX-RS "Bad Request" response including a map of all violation
	 * fields, and their message. This can then be used by clients to show
	 * violations.
	 * 
	 * @param violations
	 *            A set of violations that needs to be reported
	 * @return JAX-RS response containing all violations
	 */
	private Response.ResponseBuilder createViolationResponse(
			Set<ConstraintViolation<?>> violations) {
		log.fine("Validation completed. violations found: " + violations.size());

		Map<String, String> responseObj = new HashMap<String, String>();

		for (ConstraintViolation<?> violation : violations) {
			responseObj.put(violation.getPropertyPath().toString(),
					violation.getMessage());
		}

		return Response.status(Response.Status.BAD_REQUEST).entity(responseObj);
	}

	/**
	 * Checks if a client with the same email address is already registered.
	 * This is the only way to easily capture the
	 * "@UniqueConstraint(columnNames = "email")" constraint from the Member
	 * class.
	 * 
	 * @param email
	 *            The email to check (username)
	 * @return True if the email already exists, and false otherwise
	 * @throws Exception
	 */
	public boolean emailAlreadyExists(String email) throws Exception {
		Client client = null;
		try {
			client = clientRepository.findByEmail(email);
		} catch (NoResultException e) {
			// ignore
		}
		return client != null;
	}

	/*
	 * @PUT
	 * 
	 * @Consumes(MediaType.APPLICATION_XML) public void update(Customer
	 * customer) { entityManager.merge(customer); }
	 * 
	 * @DELETE
	 * 
	 * @Path("{id}") public void delete(@PathParam("id") long id) { Customer
	 * customer = read(id); if(null != customer) {
	 * entityManager.remove(customer); } }
	 */

}
