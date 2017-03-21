package org.springframework.github;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.MetricsServlet;
import io.prometheus.client.hotspot.MemoryPoolsExports;
import io.prometheus.client.hotspot.StandardExports;

/**
 * @author Marcin Grzejszczak
 */
@Configuration
class MetricConfiguration {
	@Bean
	CollectorRegistry metricRegistry() {
		return CollectorRegistry.defaultRegistry;
	}

	@Bean ServletRegistrationBean registerPrometheusExporterServlet(CollectorRegistry metricRegistry) {
		return new ServletRegistrationBean(new MetricsServlet(metricRegistry), "/prometheus");
	}

	@Bean
	ExporterRegister exporterRegister() {
		List<Collector> collectors = new ArrayList<>();
		collectors.add(new StandardExports());
		collectors.add(new MemoryPoolsExports());
		return new ExporterRegister(collectors);
	}
}

/**
 * Metric exporter register bean to register a list of exporters to the default
 * registry
 */
class ExporterRegister {

	private List<Collector> collectors;

	public ExporterRegister(List<Collector> collectors) {
		for (Collector collector : collectors) {
			collector.register();
		}
		this.collectors = collectors;
	}

	public List<Collector> getCollectors() {
		return collectors;
	}

}