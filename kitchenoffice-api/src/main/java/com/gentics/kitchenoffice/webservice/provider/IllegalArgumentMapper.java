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
public class IllegalArgumentMapper implements ExceptionMapper<IllegalArgumentException> {
	
	private static Logger log = Logger.getLogger(IllegalArgumentMapper.class);

	@Override
	public Response toResponse(IllegalArgumentException ex) {
		
		log.info(ex);
		
		SystemMessage message = new SystemMessage();
		message.setDescription("Please provide a valid value: " + ex);
		message.setType(MessageType.error);
		
		return Response.status(Status.NOT_ACCEPTABLE).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
