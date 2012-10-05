package com.gentics.kitchenoffice.rest.resource;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.gentics.kitchenoffice.rest.response.MessageResponse;

@Path("/server")
public class ServerResource {

    static Logger log = Logger.getLogger(ServerResource.class);

    @Context
    HttpServletRequest req;

    @GET
    @Path("/list")
    @Produces(MediaType.APPLICATION_JSON)
    public MessageResponse getServers() {
        return new MessageResponse("huhu es geht");
    }

}
