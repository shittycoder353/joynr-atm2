package io.joynr.messaging;

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

import io.joynr.runtime.PropertyLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagingPropertiesPersistence {
    private static final Logger LOG = LoggerFactory.getLogger(MessagingPropertiesPersistence.class);
    private File persistenceFile;
    private Properties storage;

    public MessagingPropertiesPersistence(File persistenceFile) {
        this.persistenceFile = persistenceFile;
        makeDirectories(persistenceFile);
        storage = PropertyLoader.loadProperties(persistenceFile);

        generateIfAbsent(MessagingPropertyKeys.CHANNELID);
        generateIfAbsent(MessagingPropertyKeys.RECEIVERID);

        persistProperties();
    }

    public Properties getPersistedProperties() {
        return storage;
    }

    private void persistProperties() {
        FileOutputStream fileOutputStream = null;
        try {
            File parentDirectory = persistenceFile.getParentFile();
            if (parentDirectory != null && !parentDirectory.exists() && !parentDirectory.mkdirs()) {
                throw new IllegalStateException("Could not create parent directory: " + parentDirectory);
            }

            if (!persistenceFile.exists()) {
                boolean created = persistenceFile.createNewFile();
                if (!created) {
                    throw new IllegalStateException("Could not create persistenceFile: " + persistenceFile);
                }
            }

            fileOutputStream = new FileOutputStream(persistenceFile);
            storage.store(fileOutputStream, null);
        } catch (IOException e1) {
            LOG.error("Couldn't write properties file {}", persistenceFile.getPath(), e1);
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                }
            }
        }
    }

    private void makeDirectories(File persistenceFile) {
        File parentFile = persistenceFile.getParentFile();
        if (parentFile != null && parentFile.exists() == false) {
            boolean madeDirs = parentFile.mkdirs();
            if (!madeDirs) {
                throw new IllegalStateException("Couldn't create dir: " + parentFile);
            }
        }
    }

    private void generateIfAbsent(String propertyName) {
        if (!storage.containsKey(propertyName)) {
            String propertyValue = UUID.randomUUID().toString();
            storage.put(propertyName, propertyValue);
        }
    }
}
