package com.gentics.kitchenoffice.webservice.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;

import com.gentics.kitchenoffice.data.SystemMessage;
import com.gentics.kitchenoffice.data.SystemMessage.MessageType;
import com.sun.jersey.api.client.ClientResponse.Status;

@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {
	
	private static Logger log = Logger.getLogger(AccessDeniedExceptionMapper.class);

	@Override
	public Response toResponse(AccessDeniedException ex) {
		
		log.info(ex);
		
		SystemMessage message = new SystemMessage();
		message.setDescription("Sorry, access denied: " + ex.getLocalizedMessage());
		message.setType(MessageType.error);
		
		return Response.status(Status.UNAUTHORIZED).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
