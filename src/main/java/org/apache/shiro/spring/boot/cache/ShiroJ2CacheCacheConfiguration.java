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

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.j2cache.J2CacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.oschina.j2cache.CacheChannel;

/**
 * Auto-configuration for Shiro J2Cache cache manager. Activates when J2Cache is on the classpath
 * and the {@code shiro.cache.type} property is set to "j2cache".
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnClass(net.oschina.j2cache.J2Cache.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "j2cache")
public class ShiroJ2CacheCacheConfiguration {

	/**
	 * Creates a Shiro {@link CacheManager} backed by J2Cache.
	 *
	 * @param channel the J2Cache channel
	 * @return the configured Shiro cache manager
	 */
	@Bean
	@ConditionalOnBean(CacheChannel.class)
    /**
     * <p>Shiro cache manager.</p>
     * @param channel
     * @return the shiro cache manager
     */
	public CacheManager shiroCacheManager(CacheChannel channel) {
		return new J2CacheManager(channel);
	}

}
