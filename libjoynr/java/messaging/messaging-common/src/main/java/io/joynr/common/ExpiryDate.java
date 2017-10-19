package io.joynr.common;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Instances of this class represent absolute time stamps since 01 Jan 1970 00:00:00 .
 * 
 */

public class ExpiryDate {

    private long value;
    private long relativeTtl;
    private long creationTime;

    /**
     * 
     * @param relativeTtl milliseconds until the ttl expires
     * @return an ExpiryDate with creationTime = current time and value = creationTime + relativeTtl
     */
    public static ExpiryDate fromRelativeTtl(long relativeTtl) {
        long creationTime = System.currentTimeMillis();
        long expiryDate;
        try {
            expiryDate = Math.addExact(creationTime, relativeTtl);
        } catch (ArithmeticException exception) {
            expiryDate = Long.MAX_VALUE;
        }
        // do not exceed Javascript max safe integer (2^53-1)
        if (expiryDate > 9007199254740991L) {
            expiryDate = 9007199254740991L;
        }
        return new ExpiryDate(relativeTtl, expiryDate, creationTime);
    }

    /**
     * NOTE: relative Ttl can be negative if the ExpiryDate passed in was in the past
     * @param expiryDate time measured in milliseconds, between the current time and midnight, January 1, 1970 UTC
     * @return an ExpiryDate object with creationTime = current time, and relativeTtl = ExpiryDate - creationTime;
     */
    public static ExpiryDate fromAbsolute(long expiryDate) {
        long creationTime = System.currentTimeMillis();
        long relativeTtl;
        try {
            relativeTtl = Math.subtractExact(expiryDate, creationTime);
        } catch (ArithmeticException exception) {
            relativeTtl = Long.MIN_VALUE;
        }
        return new ExpiryDate(relativeTtl, expiryDate, creationTime);
    }

    /**
     * 
     * @param relativeTtl the absolute time will be set based on this value relative to the point in time when the object is created
     */
    private ExpiryDate(long relativeTtl, long expiryDate, long creationTime) {
        this.relativeTtl = relativeTtl;
        value = expiryDate;
        this.creationTime = creationTime;

    }

    public long getValue() {
        return value;
    }

    /**
     * 
     * @return the original relative ttl used to create this object
     */
    public long getRelativeTtl() {
        return relativeTtl;
    }

    /**
     * 
     * @return time when the ExpiryDate was created, set as System.currentTimeMillis()
     */
    public long getCreationTime() {
        return creationTime;
    }

    @Override
    public String toString() {
        DateFormat dateFormatter = new SimpleDateFormat("dd/MM HH:mm:ss:sss");
        return "ExpiryDate in ms: " + value + " ExpiryDate: " + dateFormatter.format(value) + " relativeTtl:"
                + relativeTtl + " creationTime: " + dateFormatter.format(creationTime);
    }

}
