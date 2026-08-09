package org.apache.shiro.spring.boot.cache;

import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.crazycake.shiro.IRedisManager;
import org.crazycake.shiro.RedisCacheManager;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.AbstractCachingConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Auto-configuration for Shiro Redis cache manager. Activates when the Shiro Redis client is on the classpath
 * and the {@code shiro.cache.type} property is set to "redis".
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 * @since 1.0.0
 */
@Configuration
@AutoConfigureAfter(AbstractCachingConfiguration.class)
@AutoConfigureBefore(ShiroWebAutoConfiguration.class)
@ConditionalOnClass(IRedisManager.class)
@ConditionalOnProperty(prefix = ShiroCacheProperties.PREFIX, value = "type", havingValue = "redis")
@EnableConfigurationProperties({ ShiroRedisCacheProperties.class })
public class ShiroRedisCacheConfiguration {
	
	/**
	 * Creates a Shiro {@link CacheManager} backed by Redis.
	 *
	 * @param properties the Redis cache configuration properties
	 * @param redisManager the Redis manager
	 * @return the configured Shiro cache manager
	 */
	@Bean
	public CacheManager shiroCacheManager(ShiroRedisCacheProperties properties,
			IRedisManager redisManager) {
		
		RedisCacheManager cacheManager = new RedisCacheManager();
		
		cacheManager.setExpire(properties.getExpire()); 
		cacheManager.setKeyPrefix(properties.getKeyPrefix());
		//cacheManager.setKeySerializer(keySerializer);
		cacheManager.setPrincipalIdFieldName(properties.getPrincipalIdFieldName());
		cacheManager.setRedisManager(redisManager);
		//cacheManager.setValueSerializer(valueSerializer);
		
		return cacheManager;
	}

}
