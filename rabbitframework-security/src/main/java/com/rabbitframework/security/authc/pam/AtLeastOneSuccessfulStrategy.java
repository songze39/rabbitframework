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
package com.rabbitframework.security.authc.pam;

import com.rabbitframework.security.authc.AuthenticationException;
import com.rabbitframework.security.authc.AuthenticationInfo;
import com.rabbitframework.security.authc.AuthenticationToken;
import com.rabbitframework.security.util.CollectionUtils;

/**
 * 只要有一个 Realm 验证成功即可,和 {@link FirstSuccessfulStrategy} 不同,返回所有 Realm 身份验证成功的认证信息;
 */
public class AtLeastOneSuccessfulStrategy extends AbstractAuthenticationStrategy {

    /**
     * Ensures that the <code>aggregate</code> method argument is not <code>null</code> and
     * <code>aggregate.{@link AuthenticationInfo#getPrincipals() getPrincipals()}</code>
     * is not <code>null</code>, and if either is <code>null</code>, throws an AuthenticationException to indicate
     * that none of the realms authenticated successfully.
     */
    public AuthenticationInfo afterAllAØttempts(AuthenticationToken token, AuthenticationInfo aggregate) throws AuthenticationException {
        //we know if one or more were able to succesfully authenticate if the aggregated account object does not
        //contain null or empty data:
        if (aggregate == null || CollectionUtils.isEmpty(aggregate.getPrincipals())) {
            throw new AuthenticationException("Authentication token of type [" + token.getClass() + "] " +
                    "could not be authenticated by any configured realms.  Please ensure that at least one realm can " +
                    "authenticate these tokens.");
        }

        return aggregate;
    }
}
