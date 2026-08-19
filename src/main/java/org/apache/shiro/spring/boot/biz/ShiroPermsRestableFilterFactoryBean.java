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
package org.apache.shiro.spring.boot.biz;

import java.util.Map;

/**
 * A {@link ShiroBizFilterFactoryBean} extension that supports dynamic permission-based filter chain
 * definitions. This allows runtime modification of the Shiro filter chain based on permission
 * configurations stored externally (e.g., in a database).
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 * @see <a href="https://www.cnblogs.com/007sx/p/7381475.html">Dynamic Permission Reference</a>
 */
public class ShiroPermsRestableFilterFactoryBean extends ShiroBizFilterFactoryBean  {

	/**
	 * Sets the filter chain definition map, allowing dynamic permission definitions to be merged
	 * with the static filter chain configuration.
	 *
	 * @param filterChainDefinitionMap the map of URL patterns to filter chain definitions
	 */
	@Override
    /**
     * <p>Sets the filter chain definition map.</p>
     * @param filterChainDefinitionMap
     */
	public void setFilterChainDefinitionMap(Map<String, String> filterChainDefinitionMap) {
		
		
		
		super.setFilterChainDefinitionMap(filterChainDefinitionMap);
	}
	

}