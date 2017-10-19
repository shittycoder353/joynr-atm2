package io.joynr.messaging.bounceproxy.filter;

import joynr.ImmutableMessage;

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

import joynr.MutableMessage;

import org.atmosphere.cpr.AtmosphereResource;
import org.atmosphere.cpr.BroadcastFilter.BroadcastAction.ACTION;
import org.atmosphere.cpr.PerRequestBroadcastFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class MessageSerializationFilter implements PerRequestBroadcastFilter {
    private static final Logger logger = LoggerFactory.getLogger(MessageSerializationFilter.class);
    private static final ObjectMapper mapper = new ObjectMapper();
    {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
    }

    @Override
    public BroadcastAction filter(AtmosphereResource atmosphereResource, Object originalMessage, Object message) {
        return filter(originalMessage, message);
    }

    @Override
    public BroadcastAction filter(Object originalMessage, Object message) {
        if (message instanceof MutableMessage) {
            logger.trace("filter {}", message);
            return new BroadcastAction(ACTION.CONTINUE, serialize((MutableMessage) message));
        } else if (message instanceof ImmutableMessage) {
            logger.trace("filter {}", message);
            return new BroadcastAction(ACTION.CONTINUE, ((ImmutableMessage) message).getSerializedMessage());
        }
        return new BroadcastAction(ACTION.CONTINUE, message);
    }

    private Object serialize(MutableMessage message) {
        try {
            return message.getImmutableMessage().getSerializedMessage();
        } catch (Exception e) {
            logger.error("error serializing message", e);
        }
        return message;
    }
}
