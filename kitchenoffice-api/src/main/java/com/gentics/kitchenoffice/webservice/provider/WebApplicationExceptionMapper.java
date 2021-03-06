package com.gentics.kitchenoffice.webservice.provider;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gentics.kitchenoffice.data.SystemMessage;
import com.gentics.kitchenoffice.data.SystemMessage.MessageType;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

	private static Logger log = LoggerFactory.getLogger(WebApplicationExceptionMapper.class);

	@Override
	public Response toResponse(WebApplicationException ex) {

		log.info("Webapplication exception", ex);

		SystemMessage message = new SystemMessage();
		message.setDescription("Ooops, something went wrong: " + ex.getResponse().getStatus());
		message.setType(MessageType.error);

		return Response.status(ex.getResponse().getStatus()).entity(message).type(MediaType.APPLICATION_JSON).build();
	}

}
