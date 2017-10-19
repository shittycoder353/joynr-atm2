package io.joynr.accesscontrol;

/*
 * #%L
 * %%
 * Copyright (C) 2011 - 2017 BMW Car IT GmbH
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import joynr.infrastructure.DacTypes.TrustLevel;
import joynr.ImmutableMessage;

/**
 * Checks incoming messages permissions
 */
public interface AccessController {
    /**
     * Does the given request message has permission to reach the provider?
     *
     * @param message The message to check
     * @return true if the message has permission, false otherwise
     */
    void hasConsumerPermission(final ImmutableMessage message, final HasConsumerPermissionCallback callback);

    /**
     * Does the provider with given userId and given trust level, has permission to expose given domain interface?
     *
     * @param userId The provider userId
     * @param trustLevel The trustLevel for given userId
     * @param domain The domain where provider interface belongs to
     * @param interfaceName The interface provider wants to register
     * @return true if the message has permission, false otherwise
     */
    boolean hasProviderPermission(String userId, TrustLevel trustLevel, String domain, String interfaceName);
}
