package com.gentics.kitchenoffice.webservice.mapper;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.gentics.kitchenoffice.data.SystemMessage;
import com.gentics.kitchenoffice.data.SystemMessage.MessageType;
import com.sun.jersey.api.client.ClientResponse.Status;

@Provider
public class IllegalStateMapper implements ExceptionMapper<IllegalStateException> {

	@Override
	public Response toResponse(IllegalStateException ex) {
		
		SystemMessage message = new SystemMessage();
		message.setDescription(ex.getLocalizedMessage());
		message.setType(MessageType.error);
		
		return Response.status(Status.CONFLICT).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
