package com.gentics.kitchenoffice.webservice.provider;

import javax.validation.ValidationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.log4j.Logger;

import com.gentics.kitchenoffice.data.SystemMessage;
import com.gentics.kitchenoffice.data.SystemMessage.MessageType;
import com.sun.jersey.api.client.ClientResponse.Status;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {
	
	private static Logger log = Logger.getLogger(ValidationExceptionMapper.class);

	@Override
	public Response toResponse(ValidationException ex) {
		
		log.info(ex);
		
		SystemMessage message = new SystemMessage();
		
		message.setDescription(ex.getMessage());
		message.setType(MessageType.error);
		
		return Response.status(Status.NOT_ACCEPTABLE).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
