package com.gentics.kitchenoffice.webservice.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.gentics.kitchenoffice.data.SystemMessage;
import com.gentics.kitchenoffice.data.SystemMessage.MessageType;
import com.sun.jersey.api.client.ClientResponse.Status;

@Provider
public class DefaultExceptionMapper implements ExceptionMapper<Exception> {
	
	private static Logger log = Logger.getLogger(DefaultExceptionMapper.class);

	@Override
	public Response toResponse(Exception ex) {
		
		log.info(ex);
		
		SystemMessage message = new SystemMessage();
		message.setDescription("Ooops, something went wrong: " + ex.getLocalizedMessage());
		message.setType(MessageType.error);
		
		return Response.status(Status.INTERNAL_SERVER_ERROR).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
