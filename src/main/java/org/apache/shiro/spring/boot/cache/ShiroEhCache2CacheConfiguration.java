package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
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
 * Auto-configuration for Shiro EhCache 2.x cache manager. Activates when EhCache 2.x is on the classpath
 * and the {@code shiro.cache.type} property is set to "ehcache".
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnClass(net.sf.ehcache.CacheManager.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "ehcache")
@EnableConfigurationProperties({ ShiroEhCache2CacheProperties.class })
public class ShiroEhCache2CacheConfiguration {

	/**
	 * Creates a Shiro {@link CacheManager} backed by EhCache 2.x.
	 *
	 * @param properties the EhCache 2 cache configuration properties
	 * @param cacheManager the optional existing EhCache 2 cache manager
	 * @return the configured Shiro cache manager
	 */
	@Bean
	public CacheManager shiroCacheManager(
			ShiroEhCache2CacheProperties properties,
			@Autowired(required = false) net.sf.ehcache.CacheManager cacheManager) {
		
		EhCacheManager shiroCacheManager = new EhCacheManager();
		// 给 CacheManager设置值
		shiroCacheManager.setCacheManager(cacheManager);
		shiroCacheManager.setCacheManagerConfigFile(properties.getCacheManagerConfigFile());
		
		

		return shiroCacheManager;
	}

}
