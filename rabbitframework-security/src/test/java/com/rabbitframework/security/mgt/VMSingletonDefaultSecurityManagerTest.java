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
package com.rabbitframework.security.mgt;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.rabbitframework.security.SecurityUtils;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.authc.UsernamePasswordToken;
import com.rabbitframework.security.config.Ini;
import com.rabbitframework.security.mgt.DefaultSecurityManager;
import com.rabbitframework.security.realm.text.IniRealm;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.util.ThreadContext;

import static org.junit.Assert.assertTrue;


/**
 * @since May 8, 2008 12:26:23 AM
 */
public class VMSingletonDefaultSecurityManagerTest {

    @Before
    public void setUp() {
        ThreadContext.remove();
    }

    @After
    public void tearDown() {
        ThreadContext.remove();
    }

    @Test
    public void testVMSingleton() {
        DefaultSecurityManager sm = new DefaultSecurityManager();
        Ini ini = new Ini();
        Ini.Section section = ini.addSection(IniRealm.USERS_SECTION_NAME);
        section.put("guest", "guest");
        sm.setRealm(new IniRealm(ini));
        SecurityUtils.setSecurityManager(sm);

        try {
            Subject subject = SecurityUtils.getSubject();

            AuthenticationToken token = new UsernamePasswordToken("guest", "guest");
            subject.login(token);
            subject.getSession().setAttribute("key", "value");
            assertTrue(subject.getSession().getAttribute("key").equals("value"));

            subject = SecurityUtils.getSubject();

            assertTrue(subject.isAuthenticated());
            assertTrue(subject.getSession().getAttribute("key").equals("value"));
        } finally {
            sm.destroy();
            //SHIRO-270:
            SecurityUtils.setSecurityManager(null);
        }
    }
}
