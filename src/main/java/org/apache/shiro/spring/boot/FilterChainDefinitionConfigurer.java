/*
 * Copyright (c) 2018, hiwepy (https://github.com/hiwepy).
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
package org.apache.shiro.spring.boot;

import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;

/**
 * Callback interface that allows beans to contribute Shiro filter-chain path definitions to the
 * default {@link DefaultShiroFilterChainDefinition}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public interface FilterChainDefinitionConfigurer {

	/**
	 * Adds path definitions to the given chain definition.
	 *
	 * @param chainDefinition the default Shiro filter chain definition to augment
	 */
	default void configurePathDefinition(DefaultShiroFilterChainDefinition chainDefinition) {
	}

}
