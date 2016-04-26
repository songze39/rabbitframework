package com.rabbitframework.security.web.servlet;

import java.io.IOException;
import java.util.concurrent.Callable;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.ExecutionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.mgt.FilterChainResolver;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.mgt.WebSecurityManager;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.apache.shiro.web.servlet.OncePerRequestFilter;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.apache.shiro.web.servlet.ShiroHttpServletResponse;
import org.apache.shiro.web.subject.WebSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractSecurityFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory
			.getLogger(AbstractShiroFilter.class);

	private static final String STATIC_INIT_PARAM_NAME = "staticSecurityManagerEnabled";

	private WebSecurityManager securityManager;

	private FilterChainResolver filterChainResolver;

	private boolean staticSecurityManagerEnabled;

	protected AbstractSecurityFilter() {
		this.staticSecurityManagerEnabled = false;
	}

	public WebSecurityManager getSecurityManager() {
		return securityManager;
	}

	public void setSecurityManager(WebSecurityManager sm) {
		this.securityManager = sm;
	}

	public FilterChainResolver getFilterChainResolver() {
		return filterChainResolver;
	}

	public void setFilterChainResolver(FilterChainResolver filterChainResolver) {
		this.filterChainResolver = filterChainResolver;
	}

	public boolean isStaticSecurityManagerEnabled() {
		return staticSecurityManagerEnabled;
	}

	public void setStaticSecurityManagerEnabled(
			boolean staticSecurityManagerEnabled) {
		this.staticSecurityManagerEnabled = staticSecurityManagerEnabled;
	}

	protected final void onFilterConfigSet() throws Exception {
		applyStaticSecurityManagerEnabledConfig();
		init();
		ensureSecurityManager();
		if (isStaticSecurityManagerEnabled()) {
			SecurityUtils.setSecurityManager(getSecurityManager());
		}
	}

	private void applyStaticSecurityManagerEnabledConfig() {
		String value = getInitParam(STATIC_INIT_PARAM_NAME);
		if (value != null) {
			Boolean b = Boolean.valueOf(value);
			if (b != null) {
				setStaticSecurityManagerEnabled(b);
			}
		}
	}

	public void init() throws Exception {
	}

	/**
	 * A fallback mechanism called in {@link #onFilterConfigSet()} to ensure
	 * that the {@link #getSecurityManager() securityManager} property has been
	 * set by configuration, and if not, creates one automatically.
	 */
	private void ensureSecurityManager() {
		WebSecurityManager securityManager = getSecurityManager();
		if (securityManager == null) {
			log.info("No SecurityManager configured.  Creating default.");
			securityManager = createDefaultSecurityManager();
			setSecurityManager(securityManager);
		}
	}

	protected WebSecurityManager createDefaultSecurityManager() {
		return new DefaultWebSecurityManager();
	}

	protected boolean isHttpSessions() {
		return getSecurityManager().isHttpSessionMode();
	}

	/**
	 * Wraps the original HttpServletRequest in a
	 * {@link ShiroHttpServletRequest}, which is required for supporting Servlet
	 * Specification behavior backed by a
	 * {@link org.apache.shiro.subject.Subject Subject} instance.
	 *
	 * @param orig
	 *            the original Servlet Container-provided incoming
	 *            {@code HttpServletRequest} instance.
	 * @return {@link ShiroHttpServletRequest ShiroHttpServletRequest} instance
	 *         wrapping the original.
	 * @since 1.0
	 */
	protected ServletRequest wrapServletRequest(HttpServletRequest orig) {
		return new ShiroHttpServletRequest(orig, getServletContext(),
				isHttpSessions());
	}

	protected ServletRequest prepareServletRequest(ServletRequest request,
			ServletResponse response, FilterChain chain) {
		ServletRequest toUse = request;
		if (request instanceof HttpServletRequest) {
			HttpServletRequest http = (HttpServletRequest) request;
			toUse = wrapServletRequest(http);
		}
		return toUse;
	}

	/**
	 * Returns a new {@link ShiroHttpServletResponse} instance, wrapping the
	 * {@code orig} argument, in order to provide correct URL rewriting behavior
	 * required by the Servlet Specification when using Shiro-based sessions
	 * (and not Servlet Container HTTP-based sessions).
	 *
	 * @param orig
	 *            the original {@code HttpServletResponse} instance provided by
	 *            the Servlet Container.
	 * @param request
	 *            the {@code ShiroHttpServletRequest} instance wrapping the
	 *            original request.
	 * @return the wrapped ServletResponse instance to use during
	 *         {@link FilterChain} execution.
	 * @since 1.0
	 */
	protected ServletResponse wrapServletResponse(HttpServletResponse orig,
			ShiroHttpServletRequest request) {
		return new ShiroHttpServletResponse(orig, getServletContext(), request);
	}

	protected ServletResponse prepareServletResponse(ServletRequest request,
			ServletResponse response, FilterChain chain) {
		ServletResponse toUse = response;
		if (!isHttpSessions() && (request instanceof ShiroHttpServletRequest)
				&& (response instanceof HttpServletResponse)) {
			// the ShiroHttpServletResponse exists to support URL rewriting for
			// session ids. This is only needed if
			// using Shiro sessions (i.e. not simple HttpSession based
			// sessions):
			toUse = wrapServletResponse((HttpServletResponse) response,
					(ShiroHttpServletRequest) request);
		}
		return toUse;
	}

	/**
	 * Creates a {@link WebSubject} instance to associate with the incoming
	 * request/response pair which will be used throughout the request/response
	 * execution.
	 *
	 * @param request
	 *            the incoming {@code ServletRequest}
	 * @param response
	 *            the outgoing {@code ServletResponse}
	 * @return the {@code WebSubject} instance to associate with the
	 *         request/response execution
	 * @since 1.0
	 */
	protected WebSubject createSubject(ServletRequest request,
			ServletResponse response) {
		return new WebSubject.Builder(getSecurityManager(), request, response)
				.buildWebSubject();
	}

	/**
	 * Updates any 'native' Session's last access time that might exist to the
	 * timestamp when this method is called. If native sessions are not enabled
	 * (that is, standard Servlet container sessions are being used) or there is
	 * no session ({@code subject.getSession(false) == null}), this method does
	 * nothing.
	 * <p/>
	 * This method implementation merely calls
	 * <code>Session.{@link org.apache.shiro.session.Session#touch() touch}()</code>
	 * on the session.
	 *
	 * @param request
	 *            incoming request - ignored, but available to subclasses that
	 *            might wish to override this method
	 * @param response
	 *            outgoing response - ignored, but available to subclasses that
	 *            might wish to override this method
	 * @since 1.0
	 */
	protected void updateSessionLastAccessTime(ServletRequest request,
			ServletResponse response) {
		if (!isHttpSessions()) { // 'native' sessions
			Subject subject = SecurityUtils.getSubject();
			// Subject should never _ever_ be null, but just in case:
			if (subject != null) {
				Session session = subject.getSession(false);
				if (session != null) {
					try {
						session.touch();
					} catch (Throwable t) {
						log.error(
								"session.touch() method invocation has failed.  Unable to update"
										+ "the corresponding session's last access time based on the incoming request.",
								t);
					}
				}
			}
		}
	}

	/**
	 * {@code doFilterInternal} implementation that sets-up, executes, and
	 * cleans-up a Shiro-filtered request. It performs the following ordered
	 * operations:
	 * <ol>
	 * <li>
	 * {@link #prepareServletRequest(ServletRequest, ServletResponse, FilterChain)
	 * Prepares} the incoming {@code ServletRequest} for use during Shiro's
	 * processing</li>
	 * <li>
	 * {@link #prepareServletResponse(ServletRequest, ServletResponse, FilterChain)
	 * Prepares} the outgoing {@code ServletResponse} for use during Shiro's
	 * processing</li>
	 * <li>
	 * {@link #createSubject(javax.servlet.ServletRequest, javax.servlet.ServletResponse)
	 * Creates} a {@link Subject} instance based on the specified
	 * request/response pair.</li>
	 * <li>Finally {@link Subject#execute(Runnable) executes} the
	 * {@link #updateSessionLastAccessTime(javax.servlet.ServletRequest, javax.servlet.ServletResponse)}
	 * and
	 * {@link #executeChain(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)}
	 * methods</li>
	 * </ol>
	 * <p/>
	 * The {@code Subject.}{@link Subject#execute(Runnable) execute(Runnable)}
	 * call in step #4 is used as an implementation technique to guarantee
	 * proper thread binding and restoration is completed successfully.
	 *
	 * @param servletRequest
	 *            the incoming {@code ServletRequest}
	 * @param servletResponse
	 *            the outgoing {@code ServletResponse}
	 * @param chain
	 *            the container-provided {@code FilterChain} to execute
	 * @throws IOException
	 *             if an IO error occurs
	 * @throws javax.servlet.ServletException
	 *             if an Throwable other than an IOException
	 */
	protected void doFilterInternal(ServletRequest servletRequest,
			ServletResponse servletResponse, final FilterChain chain)
			throws ServletException, IOException {

		Throwable t = null;

		try {
			final ServletRequest request = prepareServletRequest(
					servletRequest, servletResponse, chain);
			final ServletResponse response = prepareServletResponse(request,
					servletResponse, chain);
			final Subject subject = createSubject(request, response);
			// noinspection unchecked
			subject.execute(new Callable() {
				public Object call() throws Exception {
					updateSessionLastAccessTime(request, response);
					executeChain(request, response, chain);
					return null;
				}
			});
		} catch (ExecutionException ex) {
			t = ex.getCause();
		} catch (Throwable throwable) {
			t = throwable;
		}

		if (t != null) {
			if (t instanceof ServletException) {
				throw (ServletException) t;
			}
			if (t instanceof IOException) {
				throw (IOException) t;
			}
			// otherwise it's not one of the two exceptions expected by the
			// filter method signature - wrap it in one:
			String msg = "Filtered request failed.";
			throw new ServletException(msg, t);
		}
	}

	protected FilterChain getExecutionChain(ServletRequest request,
			ServletResponse response, FilterChain origChain) {
		FilterChain chain = origChain;

		FilterChainResolver resolver = getFilterChainResolver();
		if (resolver == null) {
			log.debug("No FilterChainResolver configured.  Returning original FilterChain.");
			return origChain;
		}

		FilterChain resolved = resolver.getChain(request, response, origChain);
		if (resolved != null) {
			log.trace("Resolved a configured FilterChain for the current request.");
			chain = resolved;
		} else {
			log.trace("No FilterChain configured for the current request.  Using the default.");
		}

		return chain;
	}

	protected void executeChain(ServletRequest request,
			ServletResponse response, FilterChain origChain)
			throws IOException, ServletException {
		FilterChain chain = getExecutionChain(request, response, origChain);
		chain.doFilter(request, response);
	}
}
