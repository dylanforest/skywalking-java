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

package org.apache.skywalking.apm.plugin.jdk.lambda.define;

import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.StaticMethodsInterceptPoint;
import org.apache.skywalking.apm.agent.core.plugin.interceptor.enhance.ClassStaticMethodsEnhancePluginDefine;
import org.apache.skywalking.apm.agent.core.plugin.match.ClassMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.IndirectMatch;
import org.apache.skywalking.apm.agent.core.plugin.match.logical.LogicalMatchOperation;
import org.apache.skywalking.apm.plugin.jdk.lambda.LambdaConfig;
import org.apache.skywalking.apm.plugin.jdk.lambda.match.ExcludeInterfaceMatch;

public class LambdaInstrumentation extends ClassStaticMethodsEnhancePluginDefine {
    private static final String LAMBDA_PREFIX = "lambda$";
    private static final String LAMBDA_METHOD_INTERCEPTOR = "org.apache.skywalking.apm.plugin.jdk.lambda.LambdaMethodInterceptor";

    @Override
    protected ClassMatch enhanceClass() {
        final IndirectMatch prefixMatches = LambdaConfig.prefixesMatchesForJdkLambda();
        if (prefixMatches == null) {
            return null;
        }

        return LogicalMatchOperation.and(new ExcludeInterfaceMatch(), prefixMatches);
    }

    @Override
    public StaticMethodsInterceptPoint[] getStaticMethodsInterceptPoints() {
        return new StaticMethodsInterceptPoint[] {
                new StaticMethodsInterceptPoint() {
                    @Override
                    public ElementMatcher<MethodDescription> getMethodsMatcher() {
                        return ElementMatchers.isPrivate().and(ElementMatchers.isSynthetic()).and(ElementMatchers.nameStartsWith(LAMBDA_PREFIX));
                    }

                    @Override
                    public String getMethodsInterceptor() {
                        return LAMBDA_METHOD_INTERCEPTOR;
                    }

                    @Override
                    public boolean isOverrideArgs() {
                        return false;
                    }
                }
        };
    }
}