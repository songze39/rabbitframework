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
package com.rabbitframework.security.authc.credential;

import com.rabbitframework.security.crypto.hash.AbstractHash;
import junit.framework.TestCase;
import org.junit.Test;

import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.authc.SimpleAuthenticationInfo;
import com.rabbitframework.security.authc.UsernamePasswordToken;
import com.rabbitframework.security.util.ClassUtils;

/**
 * @since Jun 10, 2008 4:47:09 PM
 */
public abstract class AbstractHashedCredentialsMatcherTest extends TestCase {

    public abstract Class<? extends HashedCredentialsMatcher> getMatcherClass();

    public abstract AbstractHash hash(Object credentials);

    @Test
    public void testBasic() {
        CredentialsMatcher matcher = (CredentialsMatcher) ClassUtils.newInstance(getMatcherClass());
        byte[] hashed = hash("password").getBytes();
        AuthenticationInfo account = new SimpleAuthenticationInfo("username", hashed, "realmName");
        AuthenticationToken token = new UsernamePasswordToken("username", "password");
        assertTrue(matcher.doCredentialsMatch(token, account));
    }
}
