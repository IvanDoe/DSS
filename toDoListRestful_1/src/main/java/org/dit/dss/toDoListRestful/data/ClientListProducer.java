/**
 * 
 */
package org.dit.dss.toDoListRestful.data;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.dit.dss.toDoListRestful.model.Client;


/**
 * @author ivo
 *
 */
@RequestScoped
public class ClientListProducer {
	
	 @Inject
	 private ClientRepository clientRepository;
	
	private List<Client> clients;

	 // @Named provides access the return value via the EL variable name "members" in the UI (e.g.
    // Facelets or JSP view)
    @Produces
    @Named
    public List<Client> getClients() {
        return clients;
    }
    
    public void onMemberListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Client client) {
        retrieveAllMembersOrderedByName();
    }
    
    @PostConstruct
    public void retrieveAllMembersOrderedByName() {
        clients = clientRepository.findAllOrderedByName();
    }
    
}
