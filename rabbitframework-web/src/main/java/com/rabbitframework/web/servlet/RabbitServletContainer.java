package com.rabbitframework.web.servlet;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

public class RabbitServletContainer extends ServletContainer {
	private static final long serialVersionUID = 1L;

	/**
	 * Create Jersey Servlet container.
	 */
	public RabbitServletContainer() {
		super();
	}

	/**
	 * Create Jersey Servlet container.
	 *
	 * @param resourceConfig container configuration.
	 */
	public RabbitServletContainer(final ResourceConfig resourceConfig) {
		super(resourceConfig);
	}
}
