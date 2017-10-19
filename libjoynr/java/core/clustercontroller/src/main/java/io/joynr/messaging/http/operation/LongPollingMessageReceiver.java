package io.joynr.messaging.http.operation;

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

import io.joynr.exceptions.JoynrShutdownException;
import io.joynr.messaging.MessageArrivedListener;
import io.joynr.messaging.MessageReceiver;
import io.joynr.messaging.MessagingSettings;
import io.joynr.messaging.ReceiverStatusListener;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ObjectArrays;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * The HTTP Communication Manager is responsible for opening and closing channels, managing long polls, and making HTTP
 * calls
 */
@Singleton
public class LongPollingMessageReceiver implements MessageReceiver {
    public static final String MESSAGE_RECEIVER_THREADNAME_PREFIX = "MessageReceiverThread";

    private static final Logger logger = LoggerFactory.getLogger(LongPollingMessageReceiver.class);
    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("joynr.MessageReceiver-%d").build();

    protected final MessagingSettings settings;
    // private final MessageSender messageSender;
    protected final LongPollingChannelLifecycle channelMonitor;

    private final String channelId;

    private boolean shutdown = false;

    private Object shutdownSynchronizer = new Object();

    private Set<ChannelCreatedListener> channelCreatedListeners = new HashSet<ChannelCreatedListener>(1);

    @Inject
    public LongPollingMessageReceiver(LongPollingChannelLifecycle channelMonitor, MessagingSettings settings) {
        this.channelMonitor = channelMonitor;
        this.settings = settings;
        this.channelId = channelMonitor.getChannelId();
    }

    @Override
    public synchronized Future<Void> start(MessageArrivedListener messageListener,
                                           ReceiverStatusListener... receiverStatusListeners) {
        synchronized (shutdownSynchronizer) {
            if (shutdown) {
                throw new JoynrShutdownException("Cannot register Message Listener: " + messageListener
                        + ": LongPollingMessageReceiver is already shutting down");
            }
        }

        if (isStarted()) {
            return Futures.immediateFailedFuture(new IllegalStateException("receiver is already started"));
        }

        final SettableFuture<Void> channelCreatedFuture = SettableFuture.create();
        ReceiverStatusListener[] statusListeners = ObjectArrays.concat(new ReceiverStatusListener() {

            @Override
            // Register the ChannelUrl once the receiver is started
            public void receiverStarted() {
                if (channelMonitor.isChannelCreated()) {
                    for (ChannelCreatedListener listener : channelCreatedListeners) {
                        listener.channelCreated(channelMonitor.getChannelUrl());
                    }
                    // Signal that the channel is now created for anyone blocking on the future
                    channelCreatedFuture.set(null);
                }
            }

            @Override
            // Shutdown the receiver if an exception is thrown
            public void receiverException(Throwable e) {
                channelCreatedFuture.setException(e);
                channelMonitor.shutdown();
            }
        }, receiverStatusListeners);

        channelMonitor.startLongPolling(messageListener, statusListeners);
        return channelCreatedFuture;
    }

    @Override
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(
                                                      value = "IS2_INCONSISTENT_SYNC",
                                                      justification = "shutdown is locked using the shutdownSynchronizer object")
    public void shutdown(boolean clear) {
        logger.info("SHUTTING DOWN long polling message receiver");

        if (clear) {
            deleteChannel();
        }

        if (channelMonitor != null) {
            channelMonitor.shutdown();
        }

    }

    @Override
    public String getChannelId() {
        return channelId;
    }

    @Override
    public boolean deleteChannel() {
        // stop the long polling before the channel is deleted
        if (channelMonitor != null) {
            return channelMonitor.deleteChannel(settings.getMaxRetriesCount());
        }
        return false;
    }

    @Override
    public boolean isStarted() {
        return channelMonitor.isStarted();
    }

    @Override
    public boolean isReady() {
        return channelMonitor.isChannelCreated();
    }

    @Override
    public void suspend() {
        logger.info("Suspending channelMonitor");
        channelMonitor.suspend();
    }

    @Override
    public void resume() {
        channelMonitor.resume();
    }

    public void registerChannelCreatedListener(ChannelCreatedListener listener) {
        channelCreatedListeners.add(listener);

    }
}
