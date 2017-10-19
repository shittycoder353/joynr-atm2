package io.joynr.bounceproxy;

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

import io.joynr.bounceproxy.attachments.AttachmentStorage;
import io.joynr.bounceproxy.attachments.InMemoryAttachmentStorage;
import io.joynr.bounceproxy.info.SingleBounceProxyInformationProvider;
import io.joynr.bounceproxy.service.MessagingServiceImpl;
import io.joynr.messaging.info.BounceProxyInformation;
import io.joynr.messaging.service.MessagingService;

import com.google.inject.AbstractModule;

public class SingleBounceProxyModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MessagingService.class).to(MessagingServiceImpl.class);
        bind(BounceProxyInformation.class).toProvider(SingleBounceProxyInformationProvider.class);

        bind(AttachmentStorage.class).to(InMemoryAttachmentStorage.class).asEagerSingleton();
    }

}
