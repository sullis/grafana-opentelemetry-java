/*
 * Copyright Grafana Labs
 * SPDX-License-Identifier: Apache-2.0
 */

package com.grafana.extensions;

import com.grafana.extensions.cloud.GrafanaCloudConfigCustomizer;
import com.grafana.extensions.logging.LoggingExporterConfigCustomizer;
import com.grafana.extensions.modules.EnabledInstrumentationModulesCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizer;
import io.opentelemetry.sdk.autoconfigure.spi.AutoConfigurationCustomizerProvider;
import java.util.HashMap;
import java.util.Map;

public class GrafanaAutoConfigCustomizerProvider implements AutoConfigurationCustomizerProvider {

  @Override
  public void customize(AutoConfigurationCustomizer autoConfiguration) {
    autoConfiguration
        .addPropertiesSupplier(GrafanaAutoConfigCustomizerProvider::getDefaultProperties)
        .addPropertiesSupplier(EnabledInstrumentationModulesCustomizer::getDefaultProperties)
        .addPropertiesCustomizer(EnabledInstrumentationModulesCustomizer::customizeProperties)
        .addPropertiesCustomizer(LoggingExporterConfigCustomizer::customizeProperties)
        .addPropertiesCustomizer(GrafanaCloudConfigCustomizer::customizeProperties);
  }

  private static Map<String, String> getDefaultProperties() {
    HashMap<String, String> map = new HashMap<>();
    map.put("otel.semconv-stability.opt-in", "http");
    map.put("otel.instrumentation.micrometer.base-time-unit", "s");
    map.put("otel.instrumentation.log4j-appender.experimental-log-attributes", "true");
    map.put("otel.instrumentation.logback-appender.experimental-log-attributes", "true");
    return map;
  }
}