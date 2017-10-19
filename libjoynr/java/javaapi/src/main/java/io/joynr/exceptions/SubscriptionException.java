package io.joynr.exceptions;

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

import com.fasterxml.jackson.annotation.JsonProperty;

public class SubscriptionException extends JoynrRuntimeException {

    private static final long serialVersionUID = 1L;
    @JsonProperty("subscriptionId")
    private final String subscriptionId;

    public String getSubscriptionId() {
        return this.subscriptionId;
    }

    public SubscriptionException(String subscriptionId) {
        this(subscriptionId, "SubscriptionId = " + subscriptionId);
    }

    public SubscriptionException(String subscriptionId, String errorMsg) {
        super(errorMsg);
        this.subscriptionId = subscriptionId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((subscriptionId == null) ? 0 : subscriptionId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        SubscriptionException other = (SubscriptionException) obj;
        if (subscriptionId == null) {
            if (other.subscriptionId != null)
                return false;
        } else if (!subscriptionId.equals(other.subscriptionId))
            return false;
        return true;
    }
}
