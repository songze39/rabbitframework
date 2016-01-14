package com.rabbitframework.web.resources;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.container.ResourceContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.xml.ws.spi.http.HttpContext;

import org.glassfish.jersey.server.CloseableService;

public abstract class RabbitContextResource {
	@Context
	protected UriInfo uriInfo;

	@Context
	protected Request request;

	@Context
	protected SecurityContext securityContext;

	@Context
	protected HttpContext httpContext;

	@Context
	protected CloseableService closeableService;

	@Context
	protected HttpServletRequest httpServletRequest;

	@Context
	protected HttpServletResponse httpServletResponse;

	@Context
	protected ResourceContext resourceContext;
}
