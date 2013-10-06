package com.gentics.kitchenoffice.webservice.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.gentics.kitchenoffice.data.SystemMessage;
import com.gentics.kitchenoffice.data.SystemMessage.MessageType;
import com.sun.jersey.api.client.ClientResponse.Status;

@Provider
public class IllegalStateMapper implements ExceptionMapper<IllegalStateException> {
	
	private static Logger log = Logger.getLogger(IllegalArgumentMapper.class);

	@Override
	public Response toResponse(IllegalStateException ex) {
		
		log.info(ex);
		
		SystemMessage message = new SystemMessage();
		message.setDescription("Sorry, conflict: " + ex.getLocalizedMessage());
		message.setType(MessageType.error);
		
		return Response.status(Status.CONFLICT).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
