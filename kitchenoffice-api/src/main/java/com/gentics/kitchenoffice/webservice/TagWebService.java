package com.gentics.kitchenoffice.webservice;

import javax.ws.rs.Path;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("singleton")
@Path("/tags")
public class TagWebService {

}
