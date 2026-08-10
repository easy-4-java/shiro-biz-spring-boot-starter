package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.ehcache.integrations.shiro.EhcacheShiroManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Shiro EhCache 3.x cache manager. Activates when EhCache 3.x is on the classpath
 * and the {@code shiro.cache.type} property is set to "ehcache3".
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnClass(org.ehcache.CacheManager.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "ehcache3")
@EnableConfigurationProperties({ ShiroEhCache3CacheProperties.class })
public class ShiroEhCache3CacheConfiguration {

	/**
	 * Creates a Shiro {@link CacheManager} backed by EhCache 3.x.
	 *
	 * @param properties the EhCache 3 cache configuration properties
	 * @param cacheManager the optional existing EhCache 3 cache manager
	 * @return the configured Shiro cache manager
	 */
	@Bean
	public CacheManager shiroCacheManager(
			ShiroEhCache3CacheProperties properties,
			@Autowired(required = false) org.ehcache.CacheManager cacheManager) {
		
		EhcacheShiroManager shiroCacheManager = new EhcacheShiroManager();
		// 给 CacheManager设置值
		shiroCacheManager.setCacheManager(cacheManager);
		shiroCacheManager.setCacheManagerConfigFile(properties.getCacheManagerConfigFile());

		return shiroCacheManager;
	}

}
