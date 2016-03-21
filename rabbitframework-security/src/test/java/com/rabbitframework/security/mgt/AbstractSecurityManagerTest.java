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

import com.rabbitframework.security.mgt.SecurityManager;
import com.rabbitframework.security.subject.Subject;
import com.rabbitframework.security.subject.support.SubjectThreadState;
import com.rabbitframework.security.util.ThreadContext;
import com.rabbitframework.security.util.ThreadState;

/**
 * @since 1.0
 */
public abstract class AbstractSecurityManagerTest {

    protected ThreadState threadState;

    @After
    public void tearDown() {
        ThreadContext.remove();
    }

    protected Subject newSubject(SecurityManager securityManager) {
        Subject subject = new Subject.Builder(securityManager).buildSubject();
        threadState = new SubjectThreadState(subject);
        threadState.bind();
        return subject;
    }
}
