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

public class JoynrMessageNotSentException extends JoynrRuntimeException {

    public JoynrMessageNotSentException(String errorMsg) {
        super(errorMsg);
    }

    public JoynrMessageNotSentException(String errorMsg, Throwable cause) {
        super(errorMsg, cause);
    }

    private static final long serialVersionUID = -4092692826208168737L;

}
