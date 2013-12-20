package com.gentics.kitchenoffice.webservice.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;

import com.gentics.kitchenoffice.data.SystemMessage;
import com.gentics.kitchenoffice.data.SystemMessage.MessageType;
import com.sun.jersey.api.client.ClientResponse.Status;

@Provider
public class AccessDeniedExceptionMapper implements ExceptionMapper<AccessDeniedException> {

	private static Logger log = LoggerFactory.getLogger(AccessDeniedExceptionMapper.class);

	@Override
	public Response toResponse(AccessDeniedException ex) {

		log.info("Access denied", ex);

		SystemMessage message = new SystemMessage();
		message.setDescription("Sorry, access denied: " + ex.getLocalizedMessage());
		message.setType(MessageType.error);

		return Response.status(Status.UNAUTHORIZED).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
