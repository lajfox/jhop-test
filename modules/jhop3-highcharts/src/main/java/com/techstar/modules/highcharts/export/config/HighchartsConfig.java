package com.techstar.modules.highcharts.export.config;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.CompositePropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.env.PropertySourcesPropertyResolver;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.highcharts.export.converter.SVGConverter;
import com.highcharts.export.pool.BlockingQueuePool;
import com.highcharts.export.pool.PoolException;
import com.highcharts.export.pool.ServerObjectFactory;
import com.highcharts.export.server.Server;
import com.highcharts.export.util.TempDir;

@Configuration
@EnableScheduling
// @ComponentScan(basePackages = "com.highcharts.export.converter")
// @PropertySource(value = { "classpath:app-convert.properties",
// "classpath:highcharts.properties" }, ignoreResourceNotFound = true)
public class HighchartsConfig {

	@Bean
	public PropertyResolver highchartsPropertyResolver() throws IOException {

		ResourcePropertySource propertySource = null;
		CompositePropertySource compositePropertySource = new CompositePropertySource("highchartsComposite");

		Resource resource = new ClassPathResource("highcharts.properties");
		if (resource.exists()) {
			propertySource = new ResourcePropertySource(resource);
			compositePropertySource.addPropertySource(propertySource);
		}

		propertySource = new ResourcePropertySource("classpath:app-convert.properties");
		compositePropertySource.addPropertySource(propertySource);

		MutablePropertySources propertySources = new MutablePropertySources();
		propertySources.addFirst(compositePropertySource);

		PropertyResolver propertyResolver = new PropertySourcesPropertyResolver(propertySources);

		return propertyResolver;
	}

	@Bean
	public SVGConverter svgConverter() {
		return new SVGConverter();
	}

	@Bean
	public ServerObjectFactory serverObjectFactory() throws IOException {
		try {
			new TempDir();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		PropertyResolver env = highchartsPropertyResolver();

		ServerObjectFactory serverObjectFactory = new ServerObjectFactory();
		serverObjectFactory.setHost(env.getProperty("host"));
		serverObjectFactory.setBasePort(env.getProperty("port", Integer.class));
		serverObjectFactory.setExec(env.getProperty("exec"));
		serverObjectFactory.setScript(env.getProperty("script"));
		serverObjectFactory.setReadTimeout(env.getProperty("readTimeout", Integer.class));
		serverObjectFactory.setConnectTimeout(env.getProperty("connectTimeout", Integer.class));
		serverObjectFactory.setMaxTimeout(env.getProperty("maxTimeout", Integer.class));

		return serverObjectFactory;
	}

	@Bean
	public BlockingQueuePool<Server> serverPool() throws PoolException, IOException {
		PropertyResolver env = highchartsPropertyResolver();

		BlockingQueuePool<Server> pool = new BlockingQueuePool<Server>(serverObjectFactory(), env.getProperty(
				"poolSize", Integer.class), env.getProperty("maxWait", Integer.class), env.getProperty("retentionTime",
				Integer.class));

		return pool;
	}
}
