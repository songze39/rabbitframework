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

import com.rabbitframework.security.mgt.DefaultSubjectFactory;
import com.rabbitframework.security.mgt.SecurityManager;
import com.rabbitframework.security.session.Session;
import com.rabbitframework.security.subject.PrincipalCollection;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.subject.SubjectContext;
import com.rabbitframework.security.web.subject.WebSubjectContext;
import com.rabbitframework.security.web.subject.support.WebDelegatingSubject;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * A {@code SubjectFactory} implementation that creates
 * {@link WebDelegatingSubject} instances.
 * <p/>
 * {@code WebDelegatingSubject} instances are required if Request/Response
 * objects are to be maintained across threads when using the {@code Subject}
 * {@link Subject#associateWith(java.util.concurrent.Callable) createCallable}
 * and {@link Subject#associateWith(Runnable) createRunnable} methods.
 *
 * @since 1.0
 */
public class DefaultWebSubjectFactory extends DefaultSubjectFactory {

	public DefaultWebSubjectFactory() {
		super();
	}

	public Subject createSubject(SubjectContext context) {
		if (!(context instanceof WebSubjectContext)) {
			return super.createSubject(context);
		}
		WebSubjectContext wsc = (WebSubjectContext) context;
		SecurityManager securityManager = wsc.resolveSecurityManager();
		Session session = wsc.resolveSession();
		boolean sessionEnabled = wsc.isSessionCreationEnabled();
		PrincipalCollection principals = wsc.resolvePrincipals();
		boolean authenticated = wsc.resolveAuthenticated();
		String host = wsc.resolveHost();
		ServletRequest request = wsc.resolveServletRequest();
		ServletResponse response = wsc.resolveServletResponse();

		return new WebDelegatingSubject(principals, authenticated, host, session, sessionEnabled, request, response,
				securityManager);
	}
}
