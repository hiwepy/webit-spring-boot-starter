package webit.script.spring.boot;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;

import webit.script.Engine;
import webit.script.support.springmvc3.WebitViewResolver;


@Configuration
@ConditionalOnClass({ })
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
@EnableConfigurationProperties(WebitProperties.class)
public class WebitAutoConfiguration {
	
	private static final Logger logger = LoggerFactory.getLogger(WebitAutoConfiguration.class);

	private final ApplicationContext applicationContext;

	private final WebitProperties properties;

	public WebitAutoConfiguration(ApplicationContext applicationContext, WebitProperties properties) {
		this.applicationContext = applicationContext;
		this.properties = properties;
	}

	@PostConstruct
	public void checkTemplateLocationExists() {
		if (this.properties.isCheckTemplateLocation()) {
			TemplateLocation templatePathLocation = null;
			List<TemplateLocation> locations = new ArrayList<TemplateLocation>();
			for (String templateLoaderPath : this.properties.getTemplateLoaderPath()) {
				TemplateLocation location = new TemplateLocation(templateLoaderPath);
				locations.add(location);
				if (location.exists(this.applicationContext)) {
					templatePathLocation = location;
					break;
				}
			}
			if (templatePathLocation == null) {
				logger.warn("Cannot find template location(s): " + locations
						+ " (please add some templates, "
						+ "check your Beetl configuration, or set "
						+ "spring.Beetl.checkTemplateLocation=false)");
			}
		}
	}

	protected static class BeetlConfiguration {

		@Autowired
		protected WebitProperties properties;

		protected void applyProperties(BeetlConfiguration factory) {
			/*factory.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
			factory.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
			factory.setDefaultEncoding(this.properties.getCharsetName());*/
			Properties settings = new Properties();
			settings.putAll(this.properties.getSettings());
			//factory.setFreemarkerSettings(settings);
		}

	}
	
	@Configuration
	@ConditionalOnNotWebApplication
	public static class BeetlNonWebConfiguration extends BeetlConfiguration {

		/*@Bean
		@ConditionalOnMissingBean
		public FreeMarkerConfigurationFactoryBean freeMarkerConfiguration() {
			FreeMarkerConfigurationFactoryBean freeMarkerFactoryBean = new FreeMarkerConfigurationFactoryBean();
			applyProperties(freeMarkerFactoryBean);
			return freeMarkerFactoryBean;
		}*/

	}

	@Configuration
	@ConditionalOnClass({ Servlet.class, Engine.class })
	@ConditionalOnWebApplication
	public static class BeetlWebConfiguration extends BeetlConfiguration {
		
		@Bean
		@ConditionalOnMissingBean(name = "beetlViewResolver")
		@ConditionalOnProperty(name = "spring.beetl.enabled", matchIfMissing = true)
		public WebitViewResolver beetlViewResolver() {
			WebitViewResolver resolver = new WebitViewResolver();
			this.properties.applyToMvcViewResolver(resolver);
			return resolver;
		}

		@Bean
		@ConditionalOnMissingBean
		@ConditionalOnEnabledResourceChain
		public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
			return new ResourceUrlEncodingFilter();
		}

	}
	
}
