package io.joynr.arbitration;

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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import joynr.types.CustomParameter;
import joynr.types.DiscoveryEntryWithMetaInfo;

public class KeywordArbitrationStrategyFunction extends ArbitrationStrategyFunction {

    @Override
    public Set<DiscoveryEntryWithMetaInfo> select(Map<String, String> parameters,
                                                  Collection<DiscoveryEntryWithMetaInfo> capabilities) {
        String requestedKeyword = parameters.get(ArbitrationConstants.KEYWORD_PARAMETER);
        DiscoveryEntryWithMetaInfo capabilityWithKeyword = null;

        for (DiscoveryEntryWithMetaInfo discoveryEntry : capabilities) {
            // Search for a matching keyword parameter
            CustomParameter keywordParameter = findQosParameter(discoveryEntry, ArbitrationConstants.KEYWORD_PARAMETER);
            if (keywordParameter != null) {
                String currentKeyword = keywordParameter.getValue();
                if (currentKeyword.equals(requestedKeyword)) {
                    capabilityWithKeyword = discoveryEntry;
                    break;
                }
            }
        }

        return capabilityWithKeyword == null ? null : Sets.newHashSet(capabilityWithKeyword);
    }
}
