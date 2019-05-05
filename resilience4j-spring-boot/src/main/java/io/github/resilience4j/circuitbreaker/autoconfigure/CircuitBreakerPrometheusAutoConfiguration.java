/*
 * Copyright 2017 Robert Winkler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.resilience4j.circuitbreaker.autoconfigure;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.prometheus.CircuitBreakerExports;
import io.github.resilience4j.prometheus.collectors.CircuitBreakerMetricsCollector;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * {@link org.springframework.boot.autoconfigure.EnableAutoConfiguration
 * Auto-configuration} for resilience4j-metrics.
 */
@Configuration
@AutoConfigureAfter(value = CircuitBreakerAutoConfiguration.class)
@ConditionalOnClass(CircuitBreakerMetricsCollector.class)
@ConditionalOnProperty(value = "resilience4j.circuitbreaker.metrics.enabled", matchIfMissing = true)
public class CircuitBreakerPrometheusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "resilience4j.circuitbreaker.metrics.use_legacy_collector", havingValue = "true")
    public CircuitBreakerExports legacyCircuitBreakerPrometheusCollector(CircuitBreakerRegistry circuitBreakerRegistry) {
        CircuitBreakerExports collector = CircuitBreakerExports.ofCircuitBreakerRegistry(circuitBreakerRegistry);
        collector.register();
        return collector;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
        value = "resilience4j.circuitbreaker.metrics.use_legacy_collector",
        havingValue = "false",
        matchIfMissing = true
    )
    public CircuitBreakerMetricsCollector circuitBreakerPrometheusCollector(CircuitBreakerRegistry circuitBreakerRegistry) {
        CircuitBreakerMetricsCollector collector = CircuitBreakerMetricsCollector.ofCircuitBreakerRegistry(circuitBreakerRegistry);
        collector.register();
        return collector;
    }
}
