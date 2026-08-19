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
package org.apache.shiro.spring.boot.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the Shiro EhCache 2.x cache manager.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@ConfigurationProperties(ShiroEhCache2CacheProperties.PREFIX)
public class ShiroEhCache2CacheProperties {

	public static final String PREFIX = "shiro.cache.ehcache2";
	
    /**
     * Classpath file location of the ehcache CacheManager config file.
     */
    private String cacheManagerConfigFile = "classpath:org/apache/shiro/cache/ehcache/ehcache.xml";

    /**
     * <p>Returns the cache manager config file.</p>
     * @return the get cache manager config file
     */
	public String getCacheManagerConfigFile() {
		return cacheManagerConfigFile;
	}

    /**
     * <p>Sets the cache manager config file.</p>
     * @param cacheManagerConfigFile
     */
	public void setCacheManagerConfigFile(String cacheManagerConfigFile) {
		this.cacheManagerConfigFile = cacheManagerConfigFile;
	}
    
}
