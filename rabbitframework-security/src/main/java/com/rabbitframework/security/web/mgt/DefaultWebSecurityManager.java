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
package com.rabbitframework.security.web.mgt;

import java.io.Serializable;
import java.util.Collection;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitframework.security.mgt.DefaultSecurityManager;
import com.rabbitframework.security.mgt.DefaultSubjectDAO;
import com.rabbitframework.security.mgt.SessionStorageEvaluator;
import com.rabbitframework.security.mgt.SubjectDAO;
import com.rabbitframework.security.realm.Realm;
import com.rabbitframework.security.session.mgt.SessionContext;
import com.rabbitframework.security.session.mgt.SessionKey;
import com.rabbitframework.security.session.mgt.SessionManager;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.subject.SubjectContext;
import com.rabbitframework.security.web.servlet.SecurityHttpServletRequest;
import com.rabbitframework.security.web.session.mgt.DefaultWebSessionContext;
import com.rabbitframework.security.web.session.mgt.ServletContainerSessionManager;
import com.rabbitframework.security.web.session.mgt.WebSessionKey;
import com.rabbitframework.security.web.session.mgt.WebSessionManager;
import com.rabbitframework.security.web.subject.WebSubject;
import com.rabbitframework.security.web.subject.WebSubjectContext;
import com.rabbitframework.security.web.subject.support.DefaultWebSubjectContext;
import com.rabbitframework.security.web.util.WebUtils;

/**
 * Default {@link WebSecurityManager WebSecurityManager} implementation used in
 * web-based applications or any application that requires HTTP connectivity
 * (SOAP, http remoting, etc). 默认的web安全管理器
 *
 * @since 0.2
 */
public class DefaultWebSecurityManager extends DefaultSecurityManager implements WebSecurityManager {
	private static final Logger log = LoggerFactory.getLogger(DefaultWebSecurityManager.class);

	public DefaultWebSecurityManager() {
		super();
		((DefaultSubjectDAO) this.subjectDAO).setSessionStorageEvaluator(new DefaultWebSessionStorageEvaluator());
		setSubjectFactory(new DefaultWebSubjectFactory());
		setRememberMeManager(new CookieRememberMeManager());
		setSessionManager(new ServletContainerSessionManager());
	}

	public DefaultWebSecurityManager(Realm singleRealm) {
		this();
		setRealm(singleRealm);
	}

	public DefaultWebSecurityManager(Collection<Realm> realms) {
		this();
		setRealms(realms);
	}

	@Override
	protected SubjectContext createSubjectContext() {
		return new DefaultWebSubjectContext();
	}

	@Override
	// since 1.2.1 for fixing SHIRO-350
	public void setSubjectDAO(SubjectDAO subjectDAO) {
		super.setSubjectDAO(subjectDAO);
		applySessionManagerToSessionStorageEvaluatorIfPossible();
	}

	// since 1.2.1 for fixing SHIRO-350
	@Override
	protected void afterSessionManagerSet() {
		super.afterSessionManagerSet();
		applySessionManagerToSessionStorageEvaluatorIfPossible();
	}

	// since 1.2.1 for fixing SHIRO-350:
	private void applySessionManagerToSessionStorageEvaluatorIfPossible() {
		SubjectDAO subjectDAO = getSubjectDAO();
		if (subjectDAO instanceof DefaultSubjectDAO) {
			SessionStorageEvaluator evaluator = ((DefaultSubjectDAO) subjectDAO).getSessionStorageEvaluator();
			if (evaluator instanceof DefaultWebSessionStorageEvaluator) {
				((DefaultWebSessionStorageEvaluator) evaluator).setSessionManager(getSessionManager());
			}
		}
	}

	@Override
	protected SubjectContext copy(SubjectContext subjectContext) {
		if (subjectContext instanceof WebSubjectContext) {
			return new DefaultWebSubjectContext((WebSubjectContext) subjectContext);
		}
		return super.copy(subjectContext);
	}

	@Override
	public void setSessionManager(SessionManager sessionManager) {
		// this.sessionMode = null;
		if (sessionManager != null && !(sessionManager instanceof WebSessionManager)) {
			if (log.isWarnEnabled()) {
				String msg = "The " + getClass().getName() + " implementation expects SessionManager instances "
						+ "that implement the " + WebSessionManager.class.getName() + " interface.  The "
						+ "configured instance is of type [" + sessionManager.getClass().getName() + "] which does not "
						+ "implement this interface..  This may cause unexpected behavior.";
				log.warn(msg);
			}
		}
		setInternalSessionManager(sessionManager);
	}

	/**
	 * @param sessionManager
	 * @since 1.2
	 */
	private void setInternalSessionManager(SessionManager sessionManager) {
		super.setSessionManager(sessionManager);
	}

	/**
	 * @since 1.0
	 */
	public boolean isHttpSessionMode() {
		SessionManager sessionManager = getSessionManager();
		return sessionManager instanceof WebSessionManager
				&& ((WebSessionManager) sessionManager).isServletContainerSessions();
	}

	@Override
	protected SessionContext createSessionContext(SubjectContext subjectContext) {
		SessionContext sessionContext = super.createSessionContext(subjectContext);
		if (subjectContext instanceof WebSubjectContext) {
			WebSubjectContext wsc = (WebSubjectContext) subjectContext;
			ServletRequest request = wsc.resolveServletRequest();
			ServletResponse response = wsc.resolveServletResponse();
			DefaultWebSessionContext webSessionContext = new DefaultWebSessionContext(sessionContext);
			if (request != null) {
				webSessionContext.setServletRequest(request);
			}
			if (response != null) {
				webSessionContext.setServletResponse(response);
			}

			sessionContext = webSessionContext;
		}
		return sessionContext;
	}

	@Override
	protected SessionKey getSessionKey(SubjectContext context) {
		if (WebUtils.isWeb(context)) {
			Serializable sessionId = context.getSessionId();
			ServletRequest request = WebUtils.getRequest(context);
			ServletResponse response = WebUtils.getResponse(context);
			return new WebSessionKey(sessionId, request, response);
		} else {
			return super.getSessionKey(context);

		}
	}

	@Override
	protected void beforeLogout(Subject subject) {
		super.beforeLogout(subject);
		removeRequestIdentity(subject);
	}

	protected void removeRequestIdentity(Subject subject) {
		if (subject instanceof WebSubject) {
			WebSubject webSubject = (WebSubject) subject;
			ServletRequest request = webSubject.getServletRequest();
			if (request != null) {
				request.setAttribute(SecurityHttpServletRequest.IDENTITY_REMOVED_KEY, Boolean.TRUE);
			}
		}
	}
}
