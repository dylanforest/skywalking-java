/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.plugin.jdk.lambda;

import org.apache.skywalking.apm.agent.core.plugin.match.IndirectMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.PrefixMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.logical.LogicalMatchOperation;

import java.util.ArrayList;
import java.util.List;

public class LambdaConfig {
    public static IndirectMatch prefixesMatchesForJdkLambda() {
        final String jointPrefixes = JDKLambdaPluginConfig.Plugin.JdkLambda.LAMBDA_METHOD_PREFIXES;

        if (jointPrefixes == null || jointPrefixes.trim().isEmpty()) {
            return null;
        }

        final String[] prefixes = jointPrefixes.split(",");
        final List<PrefixMatch> prefixMatches = new ArrayList<>();

        for (final String prefix : prefixes) {
            prefixMatches.add(PrefixMatch.nameStartsWith(prefix));
        }

        if (prefixMatches.isEmpty()) {
            return null;
        }

        return LogicalMatchOperation.or(prefixMatches.toArray(new PrefixMatch[0]));
    }
}
