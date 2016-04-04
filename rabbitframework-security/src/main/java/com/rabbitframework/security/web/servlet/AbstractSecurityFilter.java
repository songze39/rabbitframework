/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.rabbitframework.security.web.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.security.SecurityUtils;
import com.rabbitframework.security.session.Session;
import com.rabbitframework.security.subject.ExecutionException;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.web.filter.mgt.FilterChainResolver;
import com.rabbitframework.security.web.mgt.DefaultWebSecurityManager;
import com.rabbitframework.security.web.mgt.WebSecurityManager;
import com.rabbitframework.security.web.subject.WebSubject;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.Callable;

/**
 * Abstract base class that provides all standard Security request filtering behavior and expects
 * subclasses to implement configuration-specific logic (INI, XML, .properties, etc).
 * <p/>
 * Subclasses should perform configuration and construction logic in an overridden
 * {@link #init()} method implementation.  That implementation should make available any constructed
 * {@code SecurityManager} and {@code FilterChainResolver} by calling
 * {@link #setSecurityManager(WebSecurityManager)} and
 * {@link #setFilterChainResolver(FilterChainResolver)} methods respectively.
 * <h3>Static SecurityManager</h3>
 * By default the {@code SecurityManager} instance enabled by this filter <em>will not</em> be enabled in static
 * memory via the {@code SecurityUtils.}{@link SecurityUtils#setSecurityManager(com.rabbitframework.security.mgt.SecurityManager) setSecurityManager}
 * method.  Instead, it is expected that Subject instances will always be constructed on a request-processing thread
 * via instances of this Filter class.
 * <p/>
 * However, if you need to construct {@code Subject} instances on separate (non request-processing) threads, it might
 * be easiest to enable the SecurityManager to be available in static memory via the
 * {@link SecurityUtils#getSecurityManager()} method.  You can do this by additionally specifying an {@code init-param}:
 * <pre>
 * &lt;filter&gt;
 *     ... other config here ...
 *     &lt;init-param&gt;
 *         &lt;param-name&gt;staticSecurityManagerEnabled&lt;/param-name&gt;
 *         &lt;param-value&gt;true&lt;/param-value&gt;
 *     &lt;/init-param&gt;
 * &lt;/filter&gt;
 * </pre>
 * See the Shiro <a href="http://shiro.apache.org/subject.html">Subject documentation</a> for more information as to
 * if you would do this, particularly the sections on the {@code Subject.Builder} and Thread Association.
 *
 * @see <a href="http://shiro.apache.org/subject.html">Subject documentation</a>
 * @since 1.0
 */
public abstract class AbstractSecurityFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AbstractSecurityFilter.class);

    private static final String STATIC_INIT_PARAM_NAME = "staticSecurityManagerEnabled";

    // Reference to the security manager used by this filter
    private WebSecurityManager securityManager;

    // Used to determine which chain should handle an incoming request/response
    private FilterChainResolver filterChainResolver;

    /**
     * Whether or not to bind the constructed SecurityManager instance to static memory (via
     * SecurityUtils.setSecurityManager).  This was added to support https://issues.apache.org/jira/browse/SHIRO-287
     *
     * @since 1.2
     */
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

    /**
     * Returns {@code true} if the constructed {@link #getSecurityManager() securityManager} reference should be bound
     * to static memory (via
     * {@code SecurityUtils.}{@link SecurityUtils#setSecurityManager(com.rabbitframework.security.mgt.SecurityManager) setSecurityManager}),
     * {@code false} otherwise.
     * <p/>
     * The default value is {@code false}.
     * <p/>
     *
     * @return {@code true} if the constructed {@link #getSecurityManager() securityManager} reference should be bound
     * to static memory (via {@code SecurityUtils.}{@link SecurityUtils#setSecurityManager(com.rabbitframework.security.mgt.SecurityManager) setSecurityManager}),
     * {@code false} otherwise.
     * @see <a href="https://issues.apache.org/jira/browse/SHIRO-287">SHIRO-287</a>
     * @since 1.2
     */
    public boolean isStaticSecurityManagerEnabled() {
        return staticSecurityManagerEnabled;
    }

    /**
     * Sets if the constructed {@link #getSecurityManager() securityManager} reference should be bound
     * to static memory (via {@code SecurityUtils.}{@link SecurityUtils#setSecurityManager(com.rabbitframework.security.mgt.SecurityManager) setSecurityManager}).
     * <p/>
     * The default value is {@code false}.
     *
     * @param staticSecurityManagerEnabled if the constructed {@link #getSecurityManager() securityManager} reference
     *                                     should be bound to static memory (via
     *                                     {@code SecurityUtils.}{@link SecurityUtils#setSecurityManager(com.rabbitframework.security.mgt.SecurityManager) setSecurityManager}).
     * @see <a href="https://issues.apache.org/jira/browse/SHIRO-287">SHIRO-287</a>
     * @since 1.2
     */
    public void setStaticSecurityManagerEnabled(boolean staticSecurityManagerEnabled) {
        this.staticSecurityManagerEnabled = staticSecurityManagerEnabled;
    }

    /**
     * Filter的初始化方法
     *
     * @throws Exception
     * @see AbstractFilter#init(FilterConfig)
     */
    protected final void onFilterConfigSet() throws Exception {
        applyStaticSecurityManagerEnabledConfig();
        init();
        ensureSecurityManager();
        if (isStaticSecurityManagerEnabled()) {
            SecurityUtils.setSecurityManager(getSecurityManager());
        }
    }

    /**
     * Checks if the init-param that configures the filter to use static memory has been configured, and if so,
     * sets the {@link #setStaticSecurityManagerEnabled(boolean)} attribute with the configured value.
     *
     * @see <a href="https://issues.apache.org/jira/browse/SHIRO-287">SHIRO-287</a>
     * @since 1.2
     */
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
     * A fallback mechanism called in {@link #onFilterConfigSet()} to ensure that the
     * {@link #getSecurityManager() securityManager} property has been set by configuration, and if not,
     * creates one automatically.
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
     * Wraps the original HttpServletRequest in a {@link SecurityHttpServletRequest}, which is required for supporting
     * Servlet Specification behavior backed by a {@link Subject Subject} instance.
     *
     * @param orig the original Servlet Container-provided incoming {@code HttpServletRequest} instance.
     * @return {@link SecurityHttpServletRequest SecurityHttpServletRequest} instance wrapping the original.
     * @since 1.0
     */
    protected ServletRequest wrapServletRequest(HttpServletRequest orig) {
        return new SecurityHttpServletRequest(orig, getServletContext(), isHttpSessions());
    }

    /**
     * Prepares the {@code ServletRequest} instance that will be passed to the {@code FilterChain} for request
     * processing.
     * <p/>
     * If the {@code ServletRequest} is an instance of {@link HttpServletRequest}, the value returned from this method
     * is obtained by calling {@link #wrapServletRequest(HttpServletRequest)} to allow Shiro-specific
     * HTTP behavior, otherwise the original {@code ServletRequest} argument is returned.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @param chain    the Servlet Container provided {@code FilterChain} that will receive the returned request.
     * @return the {@code ServletRequest} instance that will be passed to the {@code FilterChain} for request processing.
     * @since 1.0
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected ServletRequest prepareServletRequest(ServletRequest request, ServletResponse response, FilterChain chain) {
        ServletRequest toUse = request;
        if (request instanceof HttpServletRequest) {
            HttpServletRequest http = (HttpServletRequest) request;
            toUse = wrapServletRequest(http);
        }
        return toUse;
    }

    /**
     * Returns a new {@link SecurityHttpServletResponse} instance, wrapping the {@code orig} argument, in order to provide
     * correct URL rewriting behavior required by the Servlet Specification when using Shiro-based sessions (and not
     * Servlet Container HTTP-based sessions).
     *
     * @param orig    the original {@code HttpServletResponse} instance provided by the Servlet Container.
     * @param request the {@code SecurityHttpServletRequest} instance wrapping the original request.
     * @return the wrapped ServletResponse instance to use during {@link FilterChain} execution.
     * @since 1.0
     */
    protected ServletResponse wrapServletResponse(HttpServletResponse orig, SecurityHttpServletRequest request) {
        return new SecurityHttpServletResponse(orig, getServletContext(), request);
    }

    /**
     * Prepares the {@code ServletResponse} instance that will be passed to the {@code FilterChain} for request
     * processing.
     * <p/>
     * This implementation delegates to {@link #wrapServletRequest(HttpServletRequest)}
     * only if Shiro-based sessions are enabled (that is, !{@link #isHttpSessions()}) and the request instance is a
     * {@link SecurityHttpServletRequest}.  This ensures that any URL rewriting that occurs is handled correctly using the
     * Shiro-managed Session's sessionId and not a servlet container session ID.
     * <p/>
     * If HTTP-based sessions are enabled (the default), then this method does nothing and just returns the
     * {@code ServletResponse} argument as-is, relying on the default Servlet Container URL rewriting logic.
     *
     * @param request  the incoming ServletRequest
     * @param response the outgoing ServletResponse
     * @param chain    the Servlet Container provided {@code FilterChain} that will receive the returned request.
     * @return the {@code ServletResponse} instance that will be passed to the {@code FilterChain} during request processing.
     * @since 1.0
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected ServletResponse prepareServletResponse(ServletRequest request, ServletResponse response, FilterChain chain) {
        ServletResponse toUse = response;
        if (!isHttpSessions() && (request instanceof SecurityHttpServletRequest) &&
                (response instanceof HttpServletResponse)) {
            //the SecurityHttpServletResponse exists to support URL rewriting for session ids.  This is only needed if
            //using Shiro sessions (i.e. not simple HttpSession based sessions):
            toUse = wrapServletResponse((HttpServletResponse) response, (SecurityHttpServletRequest) request);
        }
        return toUse;
    }

    /**
     * Creates a {@link WebSubject} instance to associate with the incoming request/response pair which will be used
     * throughout the request/response execution.
     *
     * @param request  the incoming {@code ServletRequest}
     * @param response the outgoing {@code ServletResponse}
     * @return the {@code WebSubject} instance to associate with the request/response execution
     * @since 1.0
     */
    protected WebSubject createSubject(ServletRequest request, ServletResponse response) {
        return new WebSubject.Builder(getSecurityManager(), request, response).buildWebSubject();
    }

    /**
     * Updates any 'native'  Session's last access time that might exist to the timestamp when this method is called.
     * If native sessions are not enabled (that is, standard Servlet container sessions are being used) or there is no
     * session ({@code subject.getSession(false) == null}), this method does nothing.
     * <p/>This method implementation merely calls
     * <code>Session.{@link Session#touch() touch}()</code> on the session.
     *
     * @param request  incoming request - ignored, but available to subclasses that might wish to override this method
     * @param response outgoing response - ignored, but available to subclasses that might wish to override this method
     * @since 1.0
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected void updateSessionLastAccessTime(ServletRequest request, ServletResponse response) {
        if (!isHttpSessions()) { //'native' sessions
            Subject subject = SecurityUtils.getSubject();
            //Subject should never _ever_ be null, but just in case:
            if (subject != null) {
                Session session = subject.getSession(false);
                if (session != null) {
                    try {
                        session.touch();
                    } catch (Throwable t) {
                        log.error("session.touch() method invocation has failed.  Unable to update" +
                                "the corresponding session's last access time based on the incoming request.", t);
                    }
                }
            }
        }
    }

    /**
     * {@code doFilterInternal} implementation that sets-up, executes, and cleans-up a Shiro-filtered request.  It
     * performs the following ordered operations:
     * <ol>
     * <li>{@link #prepareServletRequest(ServletRequest, ServletResponse, FilterChain) Prepares}
     * the incoming {@code ServletRequest} for use during Shiro's processing</li>
     * <li>{@link #prepareServletResponse(ServletRequest, ServletResponse, FilterChain) Prepares}
     * the outgoing {@code ServletResponse} for use during Shiro's processing</li>
     * <li> {@link #createSubject(ServletRequest, ServletResponse) Creates} a
     * {@link Subject} instance based on the specified request/response pair.</li>
     * <li>Finally {@link Subject#execute(Runnable) executes} the
     * {@link #updateSessionLastAccessTime(ServletRequest, ServletResponse)} and
     * {@link #executeChain(ServletRequest, ServletResponse, FilterChain)}
     * methods</li>
     * </ol>
     * <p/>
     * The {@code Subject.}{@link Subject#execute(Runnable) execute(Runnable)} call in step #4 is used as an
     * implementation technique to guarantee proper thread binding and restoration is completed successfully.
     *
     * @param servletRequest  the incoming {@code ServletRequest}
     * @param servletResponse the outgoing {@code ServletResponse}
     * @param chain           the container-provided {@code FilterChain} to execute
     * @throws IOException      if an IO error occurs
     * @throws ServletException if an Throwable other than an IOException
     */
    protected void doFilterInternal(ServletRequest servletRequest, ServletResponse servletResponse, final FilterChain chain)
            throws ServletException, IOException {

        Throwable t = null;

        try {
            final ServletRequest request = prepareServletRequest(servletRequest, servletResponse, chain);
            final ServletResponse response = prepareServletResponse(request, servletResponse, chain);

            final Subject subject = createSubject(request, response);

            //noinspection unchecked
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
            //otherwise it's not one of the two exceptions expected by the filter method signature - wrap it in one:
            String msg = "Filtered request failed.";
            throw new ServletException(msg, t);
        }
    }

    /**
     * Returns the {@code FilterChain} to execute for the given request.
     * <p/>
     * The {@code origChain} argument is the
     * original {@code FilterChain} supplied by the Servlet Container, but it may be modified to provide
     * more behavior by pre-pending further chains according to the Shiro configuration.
     * <p/>
     * This implementation returns the chain that will actually be executed by acquiring the chain from a
     * {@link #getFilterChainResolver() filterChainResolver}.  The resolver determines exactly which chain to
     * execute, typically based on URL configuration.  If no chain is returned from the resolver call
     * (returns {@code null}), then the {@code origChain} will be returned by default.
     *
     * @param request   the incoming ServletRequest
     * @param response  the outgoing ServletResponse
     * @param origChain the original {@code FilterChain} provided by the Servlet Container
     * @return the {@link FilterChain} to execute for the given request
     * @since 1.0
     */
    protected FilterChain getExecutionChain(ServletRequest request, ServletResponse response, FilterChain origChain) {
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

    /**
     * Executes a {@link FilterChain} for the given request.
     * <p/>
     * This implementation first delegates to
     * <code>{@link #getExecutionChain(ServletRequest, ServletResponse, FilterChain) getExecutionChain}</code>
     * to allow the application's Shiro configuration to determine exactly how the chain should execute.  The resulting
     * value from that call is then executed directly by calling the returned {@code FilterChain}'s
     * {@link FilterChain#doFilter doFilter} method.  That is:
     * <pre>
     * FilterChain chain = {@link #getExecutionChain}(request, response, origChain);
     * chain.{@link FilterChain#doFilter doFilter}(request,response);</pre>
     *
     * @param request   the incoming ServletRequest
     * @param response  the outgoing ServletResponse
     * @param origChain the Servlet Container-provided chain that may be wrapped further by an application-configured
     *                  chain of Filters.
     * @throws IOException      if the underlying {@code chain.doFilter} call results in an IOException
     * @throws ServletException if the underlying {@code chain.doFilter} call results in a ServletException
     * @since 1.0
     */
    protected void executeChain(ServletRequest request, ServletResponse response, FilterChain origChain)
            throws IOException, ServletException {
        FilterChain chain = getExecutionChain(request, response, origChain);
        chain.doFilter(request, response);
    }
}
