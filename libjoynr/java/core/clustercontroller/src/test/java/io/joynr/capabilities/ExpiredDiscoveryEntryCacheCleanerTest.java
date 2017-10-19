package io.joynr.capabilities;

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

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Sets;
import joynr.types.DiscoveryEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * Unit tests for the {@link ExpiredDiscoveryEntryCacheCleaner}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ExpiredDiscoveryEntryCacheCleanerTest {

    @Mock
    private ScheduledExecutorService scheduledExecutorService;

    @Mock
    private DiscoveryEntryStore cache;

    @Mock
    private ExpiredDiscoveryEntryCacheCleaner.CleanupAction cleanupAction;

    @Captor
    private ArgumentCaptor<Runnable> runnableArgumentCaptor;

    private ExpiredDiscoveryEntryCacheCleaner subject;

    @Before
    public void setup() {
        subject = new ExpiredDiscoveryEntryCacheCleaner(scheduledExecutorService, 1);
    }

    @Test
    public void testScheduleCacheForCleanup() {
        subject.scheduleCleanUpForCaches(cleanupAction, cache);
        verify(scheduledExecutorService).scheduleAtFixedRate(any(Runnable.class), eq(1L), eq(1L), eq(TimeUnit.MINUTES));
    }

    @Test
    public void testExpiredEntriesCleanedUp() {
        Set<DiscoveryEntry> allEntries = new HashSet<>();

        DiscoveryEntry liveEntry = mock(DiscoveryEntry.class);
        when(liveEntry.getExpiryDateMs()).thenReturn(System.currentTimeMillis() + 1000L);
        allEntries.add(liveEntry);

        final String expiredParticipantId = "expiredParticipantId";
        DiscoveryEntry expiredEntry = mock(DiscoveryEntry.class);
        when(expiredEntry.getExpiryDateMs()).thenReturn(System.currentTimeMillis() - 1000L);
        when(expiredEntry.getParticipantId()).thenReturn(expiredParticipantId);
        allEntries.add(expiredEntry);

        when(cache.getAllDiscoveryEntries()).thenReturn(allEntries);

        subject.scheduleCleanUpForCaches(cleanupAction, cache);

        verify(scheduledExecutorService).scheduleAtFixedRate(runnableArgumentCaptor.capture(),
                                                             eq(1L),
                                                             eq(1L),
                                                             eq(TimeUnit.MINUTES));
        Runnable cleanupTask = runnableArgumentCaptor.getValue();
        cleanupTask.run();
        verify(cache).getAllDiscoveryEntries();
        verify(cleanupAction).cleanup(Sets.newHashSet(expiredEntry));
    }
}
