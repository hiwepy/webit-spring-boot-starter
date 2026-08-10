/*
 * Copyright (c) 2018, hiwepy (https://github.com/easy-4-java).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package webit.script.spring.boot;

import java.util.Properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import webit.script.support.springmvc3.WebitViewResolver;

/**
 * Configuration properties for Webit Script template engine.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(prefix = "spring.webit")
public class WebitProperties {

	public static final String DEFAULT_TEMPLATE_LOADER_PATH = "classpath:/templates/";

	public static final String DEFAULT_PREFIX = "";

	public static final String DEFAULT_SUFFIX = ".httl";

	/**
	 * Well-known Webit keys which will be passed to Webit's Configuration.
	 */
	private Properties settings = new Properties();

	/**
	 * Comma-separated list of template paths.
	 */
	private String[] templateLoaderPath = new String[] { DEFAULT_TEMPLATE_LOADER_PATH };

	/**
	 * Prefer file system access for template loading. File system access enables
	 * hot detection of template changes.
	 */
	private boolean preferFileSystemAccess = true;

	/**
	 * Whether to check if the template location exists.
	 */
	private boolean checkTemplateLocation = true;

	/**
	 * Whether auto-check file changes.
	 */
	private boolean autoCheck = false;

	public Properties getSettings() {
		return this.settings;
	}

	public void setSettings(Properties settings) {
		this.settings = settings;
	}

	public String[] getTemplateLoaderPath() {
		return this.templateLoaderPath;
	}

	public boolean isPreferFileSystemAccess() {
		return this.preferFileSystemAccess;
	}

	public void setPreferFileSystemAccess(boolean preferFileSystemAccess) {
		this.preferFileSystemAccess = preferFileSystemAccess;
	}

	public void setTemplateLoaderPath(String... templateLoaderPaths) {
		this.templateLoaderPath = templateLoaderPaths;
	}

	public boolean isCheckTemplateLocation() {
		return this.checkTemplateLocation;
	}

	public void setCheckTemplateLocation(boolean checkTemplateLocation) {
		this.checkTemplateLocation = checkTemplateLocation;
	}

	public boolean isAutoCheck() {
		return autoCheck;
	}

	public void setAutoCheck(boolean autoCheck) {
		this.autoCheck = autoCheck;
	}

	/**
	 * Apply properties to a {@link WebitViewResolver}.
	 * @param resolver the view resolver to configure
	 */
	public void applyToMvcViewResolver(WebitViewResolver resolver) {
		resolver.setPrefix(this.DEFAULT_PREFIX);
		resolver.setSuffix(this.DEFAULT_SUFFIX);
	}

}
