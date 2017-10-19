package io.joynr.proxy;

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

import static io.joynr.util.AnnotationUtil.getAnnotation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import io.joynr.JoynrVersion;
import joynr.tests.TestWithoutVersionProxy;
import joynr.tests.testProxy;

@RunWith(MockitoJUnitRunner.class)
public class ProxyVersionTest {
    @Test
    public void versionIsSetCorrectly() {
        int expectedMajorVersion = 47;
        int expectedMinorVersion = 11;
        JoynrVersion versionAnnotation = getAnnotation(testProxy.class, JoynrVersion.class);
        assertNotNull(versionAnnotation);
        assertEquals(expectedMajorVersion, versionAnnotation.major());
        assertEquals(expectedMinorVersion, versionAnnotation.minor());
    }

    @Test
    public void defaultVersionIsSetCorrectly() {
        int expectedMajorVersion = 0;
        int expectedMinorVersion = 0;
        JoynrVersion versionAnnotation = getAnnotation(TestWithoutVersionProxy.class, JoynrVersion.class);
        assertEquals(expectedMajorVersion, versionAnnotation.major());
        assertEquals(expectedMinorVersion, versionAnnotation.minor());
    }
}
